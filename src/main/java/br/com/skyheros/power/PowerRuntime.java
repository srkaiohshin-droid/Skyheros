package br.com.skyheros.power;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public final class PowerRuntime {
    private PowerRuntime() {}

    public static String execute(ServerPlayer player, PowerDefinition power) {
        String comboResult = CombatProgressionRuntime.execute(player, power);
        if (comboResult != null) return comboResult;
        String specialResult = SpecialPowerRuntime.execute(player, power);
        if (specialResult != null) return specialResult;
        String behavior = power.getBehavior().toLowerCase();
        String mechanic = power.getMechanic().toLowerCase();
        double magnitude = Math.max(1.0, Math.min(8.0, power.getMagnitude()));
        Vec3 look = player.getLookAngle().normalize();

        if (behavior.contains("flight") || mechanic.equals("flight")) {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
            return "Voo ativado. Use o controle de voo do Minecraft.";
        }

        if (behavior.contains("teleport")) {
            Vec3 destination = player.position().add(look.scale(5.0));
            player.teleportTo(destination.x, destination.y, destination.z);
            player.fallDistance = 0;
            return "Teleporte executado.";
        }

        if (behavior.contains("dash") || behavior.contains("leap")
                || behavior.contains("drift") || behavior.contains("wall_run")
                || behavior.contains("gale") || behavior.contains("ember_trail")) {
            Vec3 impulse = look.scale(magnitude * 0.55)
                    .add(0, Math.max(0.25, magnitude * 0.16), 0);
            player.setDeltaMovement(player.getDeltaMovement().add(impulse));
            player.hurtMarked = true;
            return "Impulso de movimento executado.";
        }

        if (behavior.contains("heal") || behavior.contains("regenerat")
                || behavior.contains("regrowth")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, 20 * 8, 1));
            player.heal((float) Math.min(8.0, magnitude * 2.0));
            return "Regeneracao ativada.";
        }

        if (behavior.contains("shield") || behavior.contains("armor")
                || behavior.contains("resistance") || behavior.contains("guard")
                || behavior.contains("skin") || behavior.contains("cushion")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 20 * 10, 1));
            return "Defesa reforcada por 10 segundos.";
        }

        if (behavior.contains("vision") || behavior.contains("sonar")
                || behavior.contains("scan") || behavior.contains("tracking")
                || behavior.contains("sense")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION, 20 * 15, 0));
            return "Percepcao aprimorada por 15 segundos.";
        }

        if (behavior.contains("strength") || behavior.contains("muscle_boost")
                || behavior.contains("overcharge")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST, 20 * 10, 1));
            return "Forca aumentada por 10 segundos.";
        }

        if (behavior.contains("cloak") || behavior.contains("veil")
                || behavior.contains("invisib")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.INVISIBILITY, 20 * 8, 0));
            return "Camuflagem ativada por 8 segundos.";
        }

        if (behavior.contains("fire_resistance")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE, 20 * 15, 0));
            return "Resistencia ao fogo ativada.";
        }

        List<LivingEntity> targets = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(12.0),
                entity -> entity != player && entity.isAlive()
        );

        LivingEntity target = targets.stream()
                .filter(entity -> {
                    Vec3 toTarget = entity.getEyePosition()
                            .subtract(player.getEyePosition());
                    double distance = toTarget.length();
                    return distance > 0.1 && distance <= 12.0
                            && look.dot(toTarget.normalize()) > 0.65;
                })
                .min(Comparator.comparingDouble(player::distanceTo))
                .orElse(null);

        boolean attackLike = behavior.contains("projectile")
                || behavior.contains("bolt")
                || behavior.contains("beam")
                || behavior.contains("blast")
                || behavior.contains("strike")
                || behavior.contains("attack")
                || behavior.contains("burst")
                || behavior.contains("wave")
                || behavior.contains("scream")
                || behavior.contains("missile")
                || behavior.contains("shards")
                || behavior.contains("lance")
                || behavior.contains("thorn")
                || behavior.contains("meteor")
                || behavior.contains("jet")
                || behavior.contains("whip")
                || behavior.contains("crush")
                || behavior.contains("pulse")
                || behavior.contains("push")
                || behavior.contains("force")
                || behavior.contains("slam");

        if (attackLike) {
            if (target == null) {
                return "Nenhum alvo na mira. Aponte para uma criatura a ate 12 blocos.";
            }

            float damage = (float) Math.max(2.0, Math.min(12.0, magnitude * 2.0));
            boolean damaged = target.hurt(
                    player.damageSources().playerAttack(player), damage);

            // Efeito elemental: poderes de fogo incendeiam o alvo.
            String powerId = power.getId().toLowerCase();
            if (damaged && (
                    powerId.contains("fireball")
                    || behavior.contains("fireball")
                    || ((behavior.contains("fire")
                        || behavior.contains("flame")
                        || behavior.contains("burn")
                        || behavior.contains("inferno")
                        || behavior.contains("ember"))
                        && attackLike))) {
                target.setSecondsOnFire(5);
            }

            if (behavior.contains("freeze") || behavior.contains("ice")
                    || behavior.contains("frost")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 4, 2));
            } else if (behavior.contains("electric")
                    || behavior.contains("lightning")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 1));
            }

            return damaged
                    ? "Poder acertou " + target.getName().getString()
                        + " e causou " + damage + " de dano."
                    : "O alvo nao recebeu dano. Pode ser invulneravel ou estar protegido.";
        }

        if (behavior.contains("bind") || behavior.contains("hold")
                || behavior.contains("snare") || behavior.contains("immobilize")
                || behavior.contains("slowdown")) {
            if (target == null) {
                return "Nenhum alvo na mira. Aponte para uma criatura a ate 12 blocos.";
            }
            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
            return "Alvo desacelerado por 5 segundos.";
        }

        String universalResult = UniversalPowerRuntime.execute(player, power);
        if (universalResult != null) return universalResult;
        return "Poder sem executor universal.";
    }
}