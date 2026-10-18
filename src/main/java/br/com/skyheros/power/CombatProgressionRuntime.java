package br.com.skyheros.power;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.Vec3;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class CombatProgressionRuntime {
    private CombatProgressionRuntime() {}

    private static boolean hasAny(String text, String... terms) {
        for (String term : terms) if (text.contains(term)) return true;
        return false;
    }

    public static String execute(ServerPlayer player, PowerDefinition power) {
        String id = power.getId().toLowerCase(Locale.ROOT);
        if (!id.startsWith("combo_")) return null;

        String key = (id + " " + power.getName() + " "
                + power.getTheme() + " " + power.getMechanic() + " "
                + power.getBehavior()).toLowerCase(Locale.ROOT);
        String mechanic = power.getMechanic().toLowerCase(Locale.ROOT);
        double strength = Math.max(1.0, Math.min(8.0, power.getMagnitude()));
        Vec3 look = player.getLookAngle().normalize();

        if (mechanic.equals("transformation")) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 240, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 240, 1));
            return power.getName() + " ativado por 12 segundos.";
        }

        if (mechanic.equals("support")) {
            List<ServerPlayer> allies = player.level().getEntitiesOfClass(
                    ServerPlayer.class, player.getBoundingBox().inflate(8.0),
                    entity -> entity.isAlive());
            for (ServerPlayer ally : allies) {
                if (hasAny(key, "regeneration", "revitalizing", "cleanse", "sacred")) {
                    ally.heal(4.0F);
                    ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
                }
                if (hasAny(key, "shield", "guardian", "barrier", "sacred")) {
                    ally.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, 1));
                    ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 160, 0));
                }
                if (hasAny(key, "speed", "rally", "warcry", "coordination", "focus")) {
                    ally.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
                    ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160, 0));
                }
                if (hasAny(key, "cleanse")) {
                    ally.removeEffect(MobEffects.POISON);
                    ally.removeEffect(MobEffects.WITHER);
                    ally.removeEffect(MobEffects.WEAKNESS);
                    ally.removeEffect(MobEffects.BLINDNESS);
                    ally.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                }
            }
            return power.getName() + " aplicado a " + allies.size() + " jogador(es) próximo(s).";
        }

        if (mechanic.equals("defense")) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 240, 1));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 240, 1));
            if (hasAny(key, "fireproof", "elemental_ward")) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 240, 0));
            }
            if (hasAny(key, "frost")) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 0));
            }
            if (hasAny(key, "regeneration_matrix", "auto_recovery", "adaptive_armor")) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 160, 0));
            }
            return power.getName() + ": defesa reforçada por 12 segundos.";
        }

        if (mechanic.equals("passive")) {
            if (hasAny(key, "toxin_purge")) {
                player.removeEffect(MobEffects.POISON);
                player.removeEffect(MobEffects.WITHER);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0));
                if (hasAny(key, "shadow", "stealth")) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 160, 0));
                }
                if (hasAny(key, "recovery")) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
                }
            }
            return power.getName() + " ativado temporariamente.";
        }

        if (mechanic.equals("movement")) {
            if (hasAny(key, "blink", "phase_dash", "spatial_snap")) {
                double distance = key.contains("spatial_snap") ? 10.0 : 6.0;
                Vec3 destination = player.position().add(look.scale(distance));
                player.teleportTo(destination.x, destination.y, destination.z);
                player.fallDistance = 0;
                return power.getName() + ": deslocamento executado.";
            }
            if (hasAny(key, "speed", "afterimage", "sonic_dash")) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 2));
            }
            if (hasAny(key, "leap", "skybound", "drift", "gravity_sling")) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
                player.setDeltaMovement(player.getDeltaMovement().add(
                        look.x * strength * 0.45, Math.max(0.5, strength * 0.18),
                        look.z * strength * 0.45));
                player.hurtMarked = true;
            }
            return power.getName() + ": movimento especial ativado.";
        }

        if (mechanic.equals("utility")) {
            if (hasAny(key, "energy_recharge")) {
                String restored = PowerProgressionRuntime.restoreEnergy(player, 25);
                return "Energia restaurada. " + restored;
            }
            if (hasAny(key, "power_surge")) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
                return "Surto de poder ativado.";
            }
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
            List<LivingEntity> detected = player.level().getEntitiesOfClass(
                    LivingEntity.class, player.getBoundingBox().inflate(16.0),
                    entity -> entity != player && entity.isAlive());
            for (LivingEntity entity : detected) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 120, 0));
            }
            return "Varredura concluída: " + detected.size() + " entidade(s) detectada(s).";
        }

        if (mechanic.equals("environment")) {
            List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                    LivingEntity.class, player.getBoundingBox().inflate(8.0),
                    entity -> entity != player && entity.isAlive());

            if (hasAny(key, "stormfront")) {
                player.serverLevel().setWeatherParameters(0, 600, true, true);
            } else if (hasAny(key, "frost_domain")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 3));
                    entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                }
            } else if (hasAny(key, "seismic")) {
                for (LivingEntity entity : nearby) {
                    entity.hurt(player.damageSources().playerAttack(player), 4.0F);
                    Vec3 away = entity.position().subtract(player.position()).normalize();
                    entity.push(away.x, 0.45, away.z);
                    entity.hurtMarked = true;
                }
            } else if (hasAny(key, "gravitational")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 50, 0));
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                }
            } else {
                for (LivingEntity entity : nearby) {
                    entity.setSecondsOnFire(4);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                }
            }
            return power.getName() + " afetou a área próxima (" + nearby.size() + " criatura(s)).";
        }

        if (mechanic.equals("summon")) {
            EntityType<? extends Mob> type = EntityType.WOLF;
            if (hasAny(key, "iron_sentinel")) type = EntityType.IRON_GOLEM;
            else if (hasAny(key, "skeleton_guard")) type = EntityType.SKELETON;
            else if (hasAny(key, "swarm")) type = EntityType.BEE;

            int count = hasAny(key, "pack", "swarm", "hounds") ? 3 : 1;
            int spawned = 0;
            for (int i = 0; i < count; i++) {
                Mob ally = type.create(player.level());
                if (ally == null) continue;
                double angle = 2.0 * Math.PI * i / count;
                ally.moveTo(player.getX() + Math.cos(angle) * 2.0,
                        player.getY(), player.getZ() + Math.sin(angle) * 2.0,
                        player.getYRot(), 0.0F);
                if (ally instanceof Wolf wolf) {
                    wolf.setOwnerUUID(player.getUUID());
                    wolf.setTame(true);
                }
                if (player.level().addFreshEntity(ally)) spawned++;
            }
            return spawned + " aliado(s) invocado(s).";
        }

        LivingEntity target = findTarget(player, 16.0);

        if (mechanic.equals("control")) {
            if (target == null) return "Nenhum alvo na mira a até 16 blocos.";
            if (hasAny(key, "gravity", "vortex", "telekinetic")) {
                target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 1));
            }
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
            if (hasAny(key, "repulsion")) {
                Vec3 away = target.position().subtract(player.position()).normalize();
                target.push(away.x * 1.5, 0.5, away.z * 1.5);
                target.hurtMarked = true;
            }
            return power.getName() + " controlou " + target.getName().getString() + ".";
        }

        if (mechanic.equals("attack")) {
            if (target == null) return "Nenhum alvo na mira a até 16 blocos.";

            float damage = (float)Math.max(3.0, Math.min(20.0, strength * 2.5));
            if (hasAny(key, "ultimate", "finisher", "annihilation", "worldbreaker",
                    "hypernova", "collapse", "singularity")) {
                damage = Math.min(24.0F, damage + 5.0F);
            }

            boolean hit = target.hurt(player.damageSources().playerAttack(player), damage);
            if (!hit) return "O alvo não recebeu dano.";

            if (hasAny(key, "fire", "flame", "magma", "solar", "phoenix", "steam")) {
                target.setSecondsOnFire(5);
            }
            if (hasAny(key, "ice", "frost", "cryo", "zero", "glacial")) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
            }
            if (hasAny(key, "electric", "lightning", "storm", "thunder")) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
            }
            if (hasAny(key, "venom", "toxic", "blood")) {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            }
            if (hasAny(key, "shadow", "void", "entropy", "spectral")) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
            }
            if (hasAny(key, "drain", "vampiric")) player.heal(Math.min(5.0F, damage * 0.4F));

            if (hasAny(key, "shockwave", "tsunami", "meteor", "nova", "barrage",
                    "vortex", "hypernova", "singularity", "cascade", "wave",
                    "collapse", "fusion", "convergence")) {
                List<LivingEntity> others = player.level().getEntitiesOfClass(
                        LivingEntity.class, target.getBoundingBox().inflate(3.0),
                        entity -> entity != player && entity != target && entity.isAlive());
                for (LivingEntity entity : others) {
                    entity.hurt(player.damageSources().playerAttack(player), damage * 0.5F);
                }
            }
            return power.getName() + " acertou " + target.getName().getString()
                    + " e causou " + damage + " de dano.";
        }

        String universalResult = UniversalPowerRuntime.execute(player, power);
        if (universalResult != null) return universalResult;
        return "Combo sem executor universal.";
    }

    private static LivingEntity findTarget(ServerPlayer player, double range) {
        Vec3 look = player.getLookAngle().normalize();
        List<LivingEntity> entities = player.level().getEntitiesOfClass(
                LivingEntity.class, player.getBoundingBox().inflate(range),
                entity -> entity != player && entity.isAlive());
        return entities.stream().filter(entity -> {
            Vec3 diff = entity.getEyePosition().subtract(player.getEyePosition());
            double distance = diff.length();
            return distance > 0.1 && distance <= range
                    && look.dot(diff.normalize()) > 0.65
                    && player.hasLineOfSight(entity);
        }).min(Comparator.comparingDouble(player::distanceTo)).orElse(null);
    }
}