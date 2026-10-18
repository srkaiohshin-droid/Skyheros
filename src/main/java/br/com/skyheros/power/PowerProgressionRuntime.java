package br.com.skyheros.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class PowerProgressionRuntime {
    private static final String KEY = "skyheros_progression";
    private static final int MAX_ENERGY = 100;

    private PowerProgressionRuntime() {}

    private static CompoundTag state(ServerPlayer player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(KEY)) root.put(KEY, new CompoundTag());
        return root.getCompound(KEY);
    }

    private static void save(ServerPlayer player, CompoundTag data) {
        player.getPersistentData().put(KEY, data);
    }

    private static int refresh(ServerPlayer player, CompoundTag data) {
        long now = player.level().getGameTime();

        if (!data.contains("energy")) data.putInt("energy", MAX_ENERGY);
        if (!data.contains("last_energy_tick")) data.putLong("last_energy_tick", now);

        long last = data.getLong("last_energy_tick");
        long elapsed = Math.max(0L, now - last);
        int regenerated = (int)Math.min(MAX_ENERGY, elapsed / 20L);
        int energy = Math.min(MAX_ENERGY, data.getInt("energy") + regenerated);

        data.putInt("energy", energy);
        if (energy >= MAX_ENERGY) data.putLong("last_energy_tick", now);
        else data.putLong("last_energy_tick", last + regenerated * 20L);

        save(player, data);
        return energy;
    }

    public static String checkEnergy(ServerPlayer player, PowerDefinition power) {
        CompoundTag data = state(player);
        int energy = refresh(player, data);
        int cost = Math.max(0, Math.min(MAX_ENERGY, (int)Math.ceil(power.getEnergyCost())));

        if (energy < cost) {
            return "Energia insuficiente: " + energy + "/" + MAX_ENERGY
                    + ". Este poder custa " + cost + ". Regenera 1 ponto por segundo.";
        }
        return null;
    }

    public static String restoreEnergy(ServerPlayer player, int amount) {
        CompoundTag data = state(player);
        int energy = refresh(player, data);
        energy = Math.min(MAX_ENERGY, energy + Math.max(0, amount));
        data.putInt("energy", energy);
        data.putLong("last_energy_tick", player.level().getGameTime());
        save(player, data);
        return "Energia: " + energy + "/" + MAX_ENERGY + ".";
    }

    public static String recordSuccessfulUse(ServerPlayer player, PowerDefinition power) {
        CompoundTag data = state(player);
        int energy = refresh(player, data);
        int cost = Math.max(0, Math.min(MAX_ENERGY, (int)Math.ceil(power.getEnergyCost())));
        energy = Math.max(0, energy - cost);
        data.putInt("energy", energy);
        data.putLong("last_energy_tick", player.level().getGameTime());

        String powerKey = power.getId().replace(':', '_').replace('.', '_');
        CompoundTag mastery = data.getCompound("mastery");
        int uses = mastery.getInt(powerKey) + 1;
        mastery.putInt(powerKey, uses);
        data.put("mastery", mastery);

        int level = Math.min(10, 1 + uses / 10);
        String message = "Energia " + energy + "/" + MAX_ENERGY
                + " | Maestria " + level + " (" + uses + " usos)";

        if (uses % 10 == 0) {
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            message += " | Marco de maestria alcançado!";
        }

        long now = player.level().getGameTime();
        long previous = data.getLong("last_combo_tick");
        int combo = (previous > 0 && now >= previous && now - previous <= 100)
                ? data.getInt("combo_count") + 1 : 1;

        data.putLong("last_combo_tick", now);
        if (combo >= 3) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 80, 0));
            data.putInt("combo_count", 0);
            message += " | COMBO x3! Bônus de velocidade e dano por 4 segundos.";
        } else {
            data.putInt("combo_count", combo);
            message += " | Combo " + combo + "/3";
        }

        save(player, data);
        return message;
    }

    public static String checkAbilityEnergy(ServerPlayer player, int rawCost) {
        int cost = Math.max(0, Math.min(MAX_ENERGY, rawCost));
        CompoundTag data = state(player);
        int energy = refresh(player, data);

        if (energy < cost) {
            return "Energia insuficiente: " + energy + "/" + MAX_ENERGY
                    + ". Esta habilidade custa " + cost
                    + ". Regenera 1 ponto por segundo.";
        }

        return null;
    }

    public static String recordAbilityUse(
            ServerPlayer player, String abilityId, int rawCost) {

        CompoundTag data = state(player);
        int energy = refresh(player, data);
        int cost = Math.max(0, Math.min(MAX_ENERGY, rawCost));

        if (energy < cost) {
            return "Energia insuficiente para concluir o uso.";
        }

        energy -= cost;
        data.putInt("energy", energy);
        data.putLong("last_energy_tick", player.level().getGameTime());

        String masteryKey = ("ability_" + abilityId)
                .replace(':', '_')
                .replace('.', '_')
                .replace('-', '_');

        CompoundTag mastery = data.getCompound("mastery");
        int uses = mastery.getInt(masteryKey) + 1;
        mastery.putInt(masteryKey, uses);
        data.put("mastery", mastery);

        int level = Math.min(10, 1 + uses / 10);

        String message = "Energia " + energy + "/" + MAX_ENERGY
                + " | Maestria da habilidade " + level
                + " (" + uses + " usos)";

        if (uses % 10 == 0) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION, 100, 1));
            message += " | Marco de maestria alcancado!";
        }

        save(player, data);
        return message;
    }
}