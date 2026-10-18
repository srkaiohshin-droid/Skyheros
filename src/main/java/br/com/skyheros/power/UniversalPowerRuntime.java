package br.com.skyheros.power;

import java.util.List;
import java.util.Locale;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

public final class UniversalPowerRuntime {
    private UniversalPowerRuntime() {}

    public static String execute(ServerPlayer player, PowerDefinition power) {
        if (player == null || power == null) return null;

        String id = lower(power.getId());
        String name = lower(power.getName());
        String theme = lower(power.getTheme());
        String mechanic = lower(power.getMechanic());
        String behavior = lower(power.getBehavior());

        String text = id + " " + name + " " + theme + " "
            + mechanic + " " + behavior;

        double magnitude = clamp(power.getMagnitude(), 1.0, 20.0);
        float damage = (float) clamp(magnitude * 1.4, 2.0, 18.0);
        int amplifier = (int) clamp(Math.floor(magnitude / 5.0), 0, 2);
        int duration = (int) clamp(120 + power.getCooldownTicks() / 2.0, 100, 600);

        if (hasAny(text, "summon", "invoc", "sentinel", "construct")) {
            return summonGuardian(player);
        }

        if (hasAny(text, "team_regeneration", "team healing", "team_heal")
                || hasAny(mechanic, "heal", "healing", "regeneration", "recovery")
                || hasAny(text, "rejuvenation", "restoration", "lifebloom")) {
            return heal(player, text, duration, amplifier, magnitude);
        }

        if (hasAny(mechanic, "shield", "barrier", "guard", "armor", "defense")
                || hasAny(text, "protection", "defensive aura", "defense field")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE, duration, Math.min(2, amplifier)));
            player.addEffect(new MobEffectInstance(
                MobEffects.ABSORPTION, duration, Math.min(2, amplifier)));
            applyTheme(player, theme, duration, amplifier);
            return power.getName() + ": protecao e absorcao ativadas.";
        }

        if ("scan".equals(mechanic)
                || hasAny(mechanic, "sense", "perception", "detect")
                || hasAny(text, "enhanced vision", "super senses", "perception")) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, duration, 0));

            List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(16.0),
                entity -> entity != player && entity.isAlive()
            );

            int detected = 0;
            for (LivingEntity entity : nearby) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                detected++;
            }

            return power.getName() + ": percepcao ativada; entidades detectadas: "
                + detected + ".";
        }

        if (hasAny(mechanic, "dash", "surge", "overdrive", "mobility", "velocity")
                || hasAny(text, "super speed", "movement", "flight", "teleport", "phase shift")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED, duration, Math.min(3, amplifier + 1)));
            player.addEffect(new MobEffectInstance(
                MobEffects.JUMP, duration, Math.min(2, amplifier)));
            player.addEffect(new MobEffectInstance(
                MobEffects.SLOW_FALLING, duration, 0));

            Vec3 look = player.getLookAngle().normalize();
            double boost = clamp(0.45 + magnitude * 0.06, 0.5, 1.8);

            player.setDeltaMovement(
                player.getDeltaMovement().add(
                    look.x * boost,
                    Math.max(0.15, look.y * boost + 0.15),
                    look.z * boost
                )
            );

            applyTheme(player, theme, duration, amplifier);
            return power.getName() + ": impulso e mobilidade ativados.";
        }

        if (hasAny(mechanic, "bind", "control", "stasis", "snare", "freeze")
                || hasAny(text, "mind_control", "time_stop", "paralyze", "web trap")) {
            LivingEntity target = getTarget(player, 16.0);

            if (target == null) {
                return "Nenhum alvo na mira. Aponte para uma criatura a ate 16 blocos.";
            }

            target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN, duration, Math.min(5, amplifier + 2)));
            target.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS, Math.min(duration, 200), amplifier));

            if (hasAny(text, "gravity", "levitation")) {
                target.addEffect(new MobEffectInstance(
                    MobEffects.LEVITATION, 50, Math.min(2, amplifier)));
            }

            applyTargetTheme(target, theme);
            return power.getName() + " controlou " + target.getName().getString() + ".";
        }

        if (hasAny(mechanic, "nova", "wave", "barrage", "field", "ultimate",
                "blast", "pulse")) {
            List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(6.0),
                entity -> entity != player && entity.isAlive() && !entity.isAlliedTo(player)
            );

            int hits = 0;
            for (LivingEntity entity : nearby) {
                if (entity.hurt(player.damageSources().playerAttack(player), damage)) {
                    applyTargetTheme(entity, theme);
                    hits++;
                }
            }

            applyTheme(player, theme, 100, amplifier);
            return power.getName() + ": ataque em area; alvos atingidos: " + hits + ".";
        }

        if (hasAny(mechanic, "bolt", "beam", "strike", "drain")
                || power.requiresTarget()
                || hasAny(text, "projectile", "laser attack", "energy attack")) {
            LivingEntity target = getTarget(player, 16.0);

            if (target == null) {
                return "Nenhum alvo na mira. Aponte para uma criatura a ate 16 blocos.";
            }

            boolean hit = target.hurt(
                player.damageSources().playerAttack(player), damage);

            if (!hit) {
                return "O alvo nao recebeu dano.";
            }

            applyTargetTheme(target, theme);

            if ("drain".equals(mechanic) || hasAny(text, "vampiric", "life drain")) {
                player.heal(Math.min(6.0F, damage * 0.4F));
            }

            return power.getName() + " acertou "
                + target.getName().getString() + " causando " + damage + " de dano.";
        }

        if ("focus".equals(mechanic) || hasAny(text, "overcharge", "power focus")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_BOOST, duration, Math.min(2, amplifier)));
            player.addEffect(new MobEffectInstance(
                MobEffects.DIG_SPEED, duration, Math.min(2, amplifier)));
            applyTheme(player, theme, duration, amplifier);
            return power.getName() + ": foco e potencia ampliados.";
        }

        // Fallback funcional: nenhuma habilidade catalogada fica sem efeito.
        player.addEffect(new MobEffectInstance(
            MobEffects.ABSORPTION, duration, Math.min(2, amplifier)));
        player.addEffect(new MobEffectInstance(
            MobEffects.DAMAGE_BOOST, Math.min(duration, 200), amplifier));

        applyTheme(player, theme, duration, amplifier);
        return power.getName() + ": surto de poder ativado.";
    }

    private static String heal(
            ServerPlayer player, String text, int duration,
            int amplifier, double magnitude) {

        boolean team = hasAny(text, "team", "ally", "allies", "group");
        int affected = 0;

        if (team) {
            List<ServerPlayer> allies = player.level().getEntitiesOfClass(
                ServerPlayer.class,
                player.getBoundingBox().inflate(8.0),
                entity -> entity.isAlive()
            );

            for (ServerPlayer ally : allies) {
                ally.heal((float) Math.min(8.0, Math.max(2.0, magnitude)));
                ally.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, duration, Math.min(2, amplifier)));
                affected++;
            }

            return "Restauracao de equipe aplicada a " + affected + " jogador(es).";
        }

        player.heal((float) Math.min(8.0, Math.max(2.0, magnitude)));
        player.addEffect(new MobEffectInstance(
            MobEffects.REGENERATION, duration, Math.min(2, amplifier)));

        return "Recuperacao e regeneracao ativadas.";
    }

    private static String summonGuardian(ServerPlayer player) {
        IronGolem ally = EntityType.IRON_GOLEM.create(player.level());

        if (ally == null) {
            return "Nao foi possivel criar o guardiao neste momento.";
        }

        Vec3 look = player.getLookAngle().normalize();

        ally.moveTo(
            player.getX() + look.x * 2.5,
            player.getY(),
            player.getZ() + look.z * 2.5,
            player.getYRot(),
            0.0F
        );

        ally.setPlayerCreated(true);
        ally.setPersistenceRequired();

        if (player.level().addFreshEntity(ally)) {
            return "Guardiao invocado com sucesso.";
        }

        return "A invocacao falhou. Tente novamente em uma area livre.";
    }

    private static LivingEntity getTarget(ServerPlayer player, double range) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = start.add(look.scale(range));
        AABB box = player.getBoundingBox()
                .expandTowards(look.scale(range)).inflate(1.2D);

        var hit = ProjectileUtil.getEntityHitResult(
                player, start, end, box,
                entity -> entity != player
                        && entity.isPickable()
                        && !entity.isSpectator()
                        && entity instanceof LivingEntity,
                range * range);

        if (hit == null) return null;
        if (!(hit.getEntity() instanceof LivingEntity target)) return null;
        if (!player.hasLineOfSight(target)) return null;
        return target;
    }

    private static void applyTargetTheme(LivingEntity target, String theme) {
        if (hasAny(theme, "fire", "solar", "plasma", "heat")) {
            target.setSecondsOnFire(5);
        }

        if (hasAny(theme, "ice", "frost", "cold")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN, 120, 2));
        }

        if (hasAny(theme, "storm", "lightning", "electric")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS, 100, 0));
            target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING, 80, 0));
        }

        if (hasAny(theme, "nature", "toxic", "venom")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.POISON, 100, 0));
        }

        if (hasAny(theme, "shadow", "void")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.BLINDNESS, 60, 0));
        }
        if (hasAny(theme, "blood", "death")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.WITHER, 60, 0));
            target.addEffect(new MobEffectInstance(
                MobEffects.POISON, 100, 0));
        }

        if (hasAny(theme, "cosmic", "space", "quantum", "dimension", "gravity")) {
            target.addEffect(new MobEffectInstance(
                MobEffects.LEVITATION, 40, 0));
            target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING, 100, 0));
            target.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS, 80, 0));
        }

    }

    private static void applyTheme(
            ServerPlayer player, String theme, int duration, int amplifier) {

        if (hasAny(theme, "fire", "solar", "plasma")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.FIRE_RESISTANCE, duration, 0));
        }

        if (hasAny(theme, "ice", "frost", "cosmic")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE, duration, 0));
        }

        if (hasAny(theme, "storm", "kinetic")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED, duration, Math.min(2, amplifier)));
        }

        if (hasAny(theme, "shadow")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.INVISIBILITY, Math.min(duration, 160), 0));
        }

        if (hasAny(theme, "radiant", "nature")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION, Math.min(duration, 240), 0));
        }

        if (hasAny(theme, "cosmic", "radiant")) {
            player.addEffect(new MobEffectInstance(
                MobEffects.SLOW_FALLING, duration, 0));
        }
    }

    private static String lower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private static boolean hasAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) return true;
        }
        return false;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}