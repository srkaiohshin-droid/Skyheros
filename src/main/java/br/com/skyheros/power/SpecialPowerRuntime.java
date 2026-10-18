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

public final class SpecialPowerRuntime {
    private SpecialPowerRuntime() {}

    private static boolean hasAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) return true;
        }
        return false;
    }

    public static String execute(ServerPlayer player, PowerDefinition power) {
        String id = power.getId().toLowerCase(Locale.ROOT);
        if (!id.startsWith("spec_")) return null;

        String key = (id + " " + power.getName() + " "
                + power.getMechanic() + " " + power.getBehavior())
                .toLowerCase(Locale.ROOT);

        String mechanic = power.getMechanic().toLowerCase(Locale.ROOT);
        double magnitude = Math.max(1.0, Math.min(8.0, power.getMagnitude()));
        Vec3 look = player.getLookAngle().normalize();

        // TRANSFORMACOES: reforcos temporarios.
        if (mechanic.equals("transformation")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST, 20 * 12, 1));
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 20 * 12, 0));

            if (hasAny(key, "speedster")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 20 * 12, 3));
                player.addEffect(new MobEffectInstance(
                        MobEffects.JUMP, 20 * 12, 2));
            }
            if (hasAny(key, "hulk", "titan", "giant", "berserker", "rage")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST, 20 * 12, 2));
            }
            if (hasAny(key, "phoenix", "elemental", "cosmic", "omega")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.FIRE_RESISTANCE, 20 * 12, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.ABSORPTION, 20 * 12, 1));
            }
            if (hasAny(key, "shadow", "micro")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.INVISIBILITY, 20 * 6, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 20 * 12, 1));
            }
            if (hasAny(key, "diamond_skin", "adaptive_evolution")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 20 * 12, 2));
            }
            return power.getName() + " ativado por 12 segundos.";
        }

        // HABILIDADES PASSIVAS ATIVAVEIS.
        if (mechanic.equals("passive")) {
            if (hasAny(key, "aqua_breathing", "deep_sea")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.WATER_BREATHING, 20 * 30, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.DOLPHINS_GRACE, 20 * 30, 0));
                return "Adaptacao aquatica por 30 segundos.";
            }
            if (hasAny(key, "thermal_immunity")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.FIRE_RESISTANCE, 20 * 30, 0));
                return "Protecao termica por 30 segundos.";
            }
            if (hasAny(key, "toxic_immunity", "poison_purge")) {
                player.removeEffect(MobEffects.POISON);
                player.removeEffect(MobEffects.WITHER);
                player.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION, 20 * 5, 0));
                return "Purificacao ativada.";
            }
            if (hasAny(key, "regenerative", "rapid_recovery")) {
                player.heal(4.0F);
                player.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION, 20 * 12, 1));
                return "Recuperacao acelerada por 12 segundos.";
            }
            if (hasAny(key, "reflexes", "wall_crawling")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 20 * 15, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.JUMP, 20 * 15, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING, 20 * 15, 0));
                return "Mobilidade aprimorada por 15 segundos.";
            }
            if (hasAny(key, "danger_instinct", "predator_senses",
                    "xray_perception", "magnetic_sense")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.NIGHT_VISION, 20 * 20, 0));
                List<LivingEntity> sensed = player.level().getEntitiesOfClass(
                        LivingEntity.class, player.getBoundingBox().inflate(16.0),
                        entity -> entity != player && entity.isAlive());
                for (LivingEntity entity : sensed) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.GLOWING, 20 * 6, 0));
                }
                return "Sentidos aprimorados. Entidades detectadas: "
                        + sensed.size() + ".";
            }
            if (hasAny(key, "kinetic_absorption", "energy_reserves")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.ABSORPTION, 20 * 15, 2));
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 20 * 10, 0));
                return "Reserva defensiva ativada.";
            }
            if (hasAny(key, "night_stalker", "silent_movement")) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.INVISIBILITY, 20 * 8, 0));
                return "Camuflagem ativada por 8 segundos.";
            }
            if (hasAny(key, "mind_shield", "status_immunity")) {
                player.removeEffect(MobEffects.BLINDNESS);
                player.removeEffect(MobEffects.WEAKNESS);
                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                player.removeEffect(MobEffects.POISON);
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 20 * 10, 0));
                return "Protecao contra efeitos negativos ativada.";
            }
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 20 * 10, 0));
            return "Habilidade passiva ativada.";
        }

        // MOVIMENTO E TELEPORTE.
        if (hasAny(key, "short_range_blink", "long_range_blink",
                "rift_jump", "portal_step", "dimensional_shift",
                "pocket_dimension", "phasing_step")) {
            double distance = key.contains("long_range_blink") ? 16.0 : 8.0;
            Vec3 destination = player.position().add(look.scale(distance));
            player.teleportTo(destination.x, destination.y, destination.z);
            player.fallDistance = 0;
            return "Deslocamento espacial executado.";
        }

        if (hasAny(key, "super_sprint", "speed_force_dash")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, 20 * 8, 2));
            player.setDeltaMovement(player.getDeltaMovement().add(
                    look.x * magnitude * 0.5, 0.15, look.z * magnitude * 0.5));
            player.hurtMarked = true;
            return "Impulso de supervelocidade executado.";
        }

        if (hasAny(key, "air_glide", "mega_leap", "gravity_leap",
                "wall_runner", "aerial_dive", "air_dash")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.SLOW_FALLING, 20 * 8, 0));
            player.setDeltaMovement(player.getDeltaMovement().add(
                    look.x * magnitude * 0.45,
                    Math.max(0.35, magnitude * 0.15),
                    look.z * magnitude * 0.45));
            player.hurtMarked = true;
            return "Movimento especial executado.";
        }

        // DEFESA E CONSTRUTOS.
        if (mechanic.equals("defense")
                || hasAny(key, "energy_construct", "force_construct")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 20 * 12, 1));
            player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION, 20 * 12, 1));
            return "Protecao temporaria ativada.";
        }

        if (hasAny(key, "astral_projection")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.INVISIBILITY, 20 * 8, 0));
            player.addEffect(new MobEffectInstance(
                    MobEffects.SLOW_FALLING, 20 * 12, 0));
            return "Projecao astral simulada ativada.";
        }

        if (hasAny(key, "phase_shift", "phase_cloak", "invisibility_field",
                "decoy_cloak")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.INVISIBILITY, 20 * 8, 0));
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, 20 * 5, 0));
            return "Camuflagem e protecao temporarias ativadas.";
        }

        // CLIMA.
        if (mechanic.equals("environment")) {
            if (hasAny(key, "storm_call", "lightning_storm_domain", "rain_domain")) {
                boolean thunder = !key.contains("rain_domain");
                player.serverLevel().setWeatherParameters(0, 20 * 30, true, thunder);
                return thunder
                        ? "Tempestade ativada por aproximadamente 30 segundos."
                        : "Chuva ativada por aproximadamente 30 segundos.";
            }

            List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                    LivingEntity.class, player.getBoundingBox().inflate(8.0),
                    entity -> entity != player && entity.isAlive());

            if (hasAny(key, "fog_domain", "sandstorm_domain")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.BLINDNESS, 20 * 4, 0));
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN, 20 * 4, 1));
                }
            } else if (hasAny(key, "blizzard_domain")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN, 20 * 6, 3));
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.WEAKNESS, 20 * 5, 0));
                }
            } else if (hasAny(key, "heatwave_domain")) {
                for (LivingEntity entity : nearby) entity.setSecondsOnFire(5);
            } else if (hasAny(key, "time_stop", "temporal_stasis", "time_dilation")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN, 20 * 4, 4));
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.WEAKNESS, 20 * 4, 0));
                }
            } else if (hasAny(key, "gravity_field", "low_gravity_field")) {
                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.LEVITATION, 20 * 2, 0));
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN, 20 * 4, 1));
                }
            } else if (hasAny(key, "magnetic_field")) {
                for (LivingEntity entity : nearby) {
                    Vec3 toward = player.position()
                            .subtract(entity.position()).normalize();
                    entity.push(toward.x * 0.7, 0.15, toward.z * 0.7);
                    entity.hurtMarked = true;
                }
            } else if (hasAny(key, "earthquake_pulse", "seismic_domain")) {
                for (LivingEntity entity : nearby) {
                    entity.hurt(player.damageSources().playerAttack(player), 4.0F);
                    Vec3 away = entity.position()
                            .subtract(player.position()).normalize();
                    entity.push(away.x * 1.1, 0.5, away.z * 1.1);
                    entity.hurtMarked = true;
                }
            }
            return "Efeito ambiental aplicado a " + nearby.size() + " criatura(s).";
        }

        // INVOCACOES: criaturas nativas como aliados.
        if (mechanic.equals("summon")) {
            EntityType<? extends Mob> type = EntityType.WOLF;
            if (hasAny(key, "iron_golem", "guardian_golem", "golem")) {
                type = EntityType.IRON_GOLEM;
            } else if (hasAny(key, "snow_golem")) {
                type = EntityType.SNOW_GOLEM;
            } else if (hasAny(key, "skeleton", "archer", "bone_legion")) {
                type = EntityType.SKELETON;
            } else if (hasAny(key, "bee", "swarm")) {
                type = EntityType.BEE;
            }

            int count = hasAny(key, "horde", "legion", "swarm",
                    "pack", "archers", "guardians") ? 3 : 1;
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

            return spawned > 0
                    ? spawned + " aliado(s) invocado(s)."
                    : "Nao foi possivel invocar criaturas.";
        }

        // ALVO PARA CONTROLE E COMBATE.
        LivingEntity target = findTarget(player, 16.0);

        if (mechanic.equals("control")) {
            if (target == null) return "Nenhum alvo na mira.";
            if (key.contains("gravity_reverse")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.LEVITATION, 20 * 3, 1));
            } else {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, 20 * 4, 0));
            }
            return "Controle aplicado a " + target.getName().getString() + ".";
        }

        // ATAQUES ESPECIAIS.
        if (mechanic.equals("attack")) {
            if (target == null) return "Nenhum alvo na mira a ate 16 blocos.";

            float damage = (float) Math.max(2.0, Math.min(18.0, magnitude * 2.0));
            boolean hit = target.hurt(
                    player.damageSources().playerAttack(player), damage);

            if (!hit) return "O alvo nao recebeu dano.";

            if (hasAny(key, "fire", "flame", "phoenix", "solar_flare",
                    "plasma_storm", "inferno")) {
                target.setSecondsOnFire(5);
            }
            if (hasAny(key, "ice", "frost", "blizzard", "glacial", "zero")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, 20 * 4, 0));
            }
            if (hasAny(key, "lightning", "thunder", "electric", "neutron")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 2));
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, 20 * 3, 0));
            }
            if (hasAny(key, "sonic", "fear", "neural_shock")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, 20 * 4, 0));
            }
            if (hasAny(key, "gravity", "black_hole", "telekinetic")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 3));
                Vec3 away = target.position().subtract(player.position()).normalize();
                target.push(away.x * 0.8, 0.3, away.z * 0.8);
                target.hurtMarked = true;
            }
            if (hasAny(key, "vampiric", "energy_drain", "lifesteal")) {
                player.heal(Math.min(4.0F, damage * 0.35F));
            }
            if (hasAny(key, "toxic", "acid", "venom")) {
                target.addEffect(new MobEffectInstance(
                        MobEffects.POISON, 20 * 5, 0));
            }

            if (hasAny(key, "nova", "meteor", "bomb", "storm",
                    "quantum_blast", "antimatter", "barrage")) {
                List<LivingEntity> nearby = player.level().getEntitiesOfClass(
                        LivingEntity.class, target.getBoundingBox().inflate(3.0),
                        entity -> entity != player && entity != target && entity.isAlive());
                for (LivingEntity entity : nearby) {
                    entity.hurt(player.damageSources().playerAttack(player), damage * 0.5F);
                }
            }

            return power.getName() + " acertou " + target.getName().getString()
                    + " e causou " + damage + " de dano.";
        }

        return null;
    }

    private static LivingEntity findTarget(ServerPlayer player, double range) {
        Vec3 look = player.getLookAngle().normalize();
        List<LivingEntity> entities = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(range),
                entity -> entity != player && entity.isAlive()
        );

        return entities.stream()
                .filter(entity -> {
                    Vec3 difference = entity.getEyePosition()
                            .subtract(player.getEyePosition());
                    double distance = difference.length();
                    return distance > 0.1 && distance <= range
                            && look.dot(difference.normalize()) > 0.65
                            && player.hasLineOfSight(entity);
                })
                .min(Comparator.comparingDouble(player::distanceTo))
                .orElse(null);
    }
}