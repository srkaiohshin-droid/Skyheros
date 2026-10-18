package br.com.skyheros.ability;

import br.com.skyheros.power.PowerProgressionRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AbilityRuntime {
    private static final Map<String, Long> COOLDOWNS =
            new ConcurrentHashMap<>();

    private AbilityRuntime() {}

    public static int activate(ServerPlayer player, String id) {
        String normalizedId = id == null ? "" : id.toLowerCase(Locale.ROOT);
        AbilityDefinition ability = AbilityRegistry.get(normalizedId).orElse(null);

        if (ability == null) {
            player.sendSystemMessage(Component.literal(
                    "Habilidade desconhecida: " + normalizedId));
            return 0;
        }

        long now = player.level().getGameTime();
        String cooldownKey = player.getUUID() + ":" + ability.id();
        long until = COOLDOWNS.getOrDefault(cooldownKey, 0L);

        if (now < until) {
            player.sendSystemMessage(Component.literal(
                    "Habilidade em recarga: " + ((until - now + 19) / 20) + " s"));
            return 0;
        }

        int cost = energyCost(ability);
        String energyError = PowerProgressionRuntime.checkAbilityEnergy(player, cost);

        if (energyError != null) {
            player.sendSystemMessage(Component.literal(energyError));
            return 0;
        }

        boolean success = execute(player, ability);

        if (!success) {
            player.sendSystemMessage(Component.literal(
                    "Nao foi possivel usar " + ability.name()
                    + ". Mire em um alvo valido ou aproxime-se da area de efeito."));
            return 0;
        }

        COOLDOWNS.put(cooldownKey, now + ability.cooldownTicks());

        String progression = PowerProgressionRuntime.recordAbilityUse(
                player, ability.id(), cost);

        player.sendSystemMessage(Component.literal(
                "Habilidade: " + ability.name() + " | " + progression));
        return 1;
    }

    private static int energyCost(AbilityDefinition ability) {
        String[] methodNames = {
            "energyCost", "getEnergyCost", "energy", "getEnergy",
            "cost", "getCost"
        };

        for (String methodName : methodNames) {
            try {
                Method method = ability.getClass().getMethod(methodName);
                Object result = method.invoke(ability);
                if (result instanceof Number number) {
                    return Math.max(0, Math.min(100, number.intValue()));
                }
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                // Tenta outros nomes de acesso.
            }
        }

        for (Class<?> type = ability.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                String name = field.getName().toLowerCase(Locale.ROOT);

                if (!name.contains("energy") && !name.equals("cost")) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    Object result = field.get(ability);
                    if (result instanceof Number number) {
                        return Math.max(0, Math.min(100, number.intValue()));
                    }
                } catch (ReflectiveOperationException | RuntimeException ignored) {
                    // Continua procurando.
                }
            }
        }

        return 5;
    }

    private static boolean execute(ServerPlayer player, AbilityDefinition ability) {
        String behavior = ability.behavior() == null
                ? "" : ability.behavior().toUpperCase(Locale.ROOT);

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 end = eye.add(look.scale(24.0D));

        switch (behavior) {
            case "WEB_SWING": {
                BlockHitResult hit = player.level().clip(new ClipContext(
                        eye, end, ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE, player));

                if (hit.getType() == HitResult.Type.MISS) return false;

                Vec3 anchor = Vec3.atCenterOf(hit.getBlockPos());
                Vec3 toward = anchor.subtract(player.position());
                if (toward.lengthSqr() < 1.0D) return false;

                player.setDeltaMovement(player.getDeltaMovement()
                        .add(toward.normalize().scale(0.72D))
                        .add(0.0D, 0.28D, 0.0D));
                player.fallDistance = 0.0F;
                player.hurtMarked = true;
                return true;
            }

            case "WEB_SHOT":
            case "WEB_PULL":
            case "WEB_BIND": {
                LivingEntity target = findTarget(player, 24.0D);
                if (target == null) return false;

                if ("WEB_SHOT".equals(behavior)) {
                    target.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SLOWDOWN, 50, 1));
                    return true;
                }

                if ("WEB_PULL".equals(behavior)) {
                    Vec3 pull = player.position().subtract(target.position());
                    if (pull.lengthSqr() < 0.01D) return false;
                    target.setDeltaMovement(target.getDeltaMovement()
                            .add(pull.normalize().scale(0.8D)));
                    target.hurtMarked = true;
                    return true;
                }

                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 100, 4));
                target.addEffect(new MobEffectInstance(
                        MobEffects.JUMP, 100, 128));
                return true;
            }

            case "SPIDER_SENSE":
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 160, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.NIGHT_VISION, 160, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 80, 0));
                return true;

            case "ENHANCED_LEAP":
                player.setDeltaMovement(player.getDeltaMovement()
                        .add(look.x * 0.65D, 0.85D, look.z * 0.65D));
                player.fallDistance = 0.0F;
                player.hurtMarked = true;
                return true;

            case "SUPER_STRENGTH":
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST, 160, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 80, 0));
                return true;

            default:
                return executeGenerated(player, ability, behavior);
        }
    }

    private static boolean executeGenerated(
            ServerPlayer player, AbilityDefinition ability, String behavior) {

        String id = ability.id().toLowerCase(Locale.ROOT);
        int colon = id.lastIndexOf(':');
        if (colon >= 0) id = id.substring(colon + 1);

        int separator = id.indexOf('_');
        String theme = separator > 0 ? id.substring(0, separator) : "energy";
        int cost = energyCost(ability);
        int duration = 160;

        switch (behavior) {
            case "PROJECTILE":
                return attackTarget(player, theme, cost, 1.0F);

            case "BEAM":
                return attackTarget(player, theme, cost, 1.25F);

            case "DRAIN":
                return attackTarget(player, theme, cost, 1.1F);

            case "BURST":
                return areaAttack(player, theme, cost, 3.5D, 3.0F);

            case "EXPLOSION":
                return areaAttack(player, theme, cost, 4.5D, 3.8F);

            case "WAVE":
                return areaAttack(player, theme, cost, 5.0D, 3.2F);

            case "AURA":
                applyPlayerTheme(player, theme, duration, cost);
                for (LivingEntity target : nearby(player, player.position(), 6.0D)) {
                    applyTargetTheme(player, target, theme, cost);
                    if (isOffensiveAura(theme)) {
                        target.hurt(player.damageSources().playerAttack(player), 2.0F);
                    }
                }
                return true;

            case "SHIELD":
            case "BARRIER":
            case "RESISTANCE":
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, duration, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.ABSORPTION, duration, 1));
                applyPlayerTheme(player, theme, duration, cost);
                return true;

            case "DASH": {
                Vec3 look = player.getLookAngle().normalize();
                double boost = 0.8D + Math.min(1.0D, cost * 0.025D);
                player.setDeltaMovement(player.getDeltaMovement().add(
                        look.x * boost, Math.max(0.30D, look.y * boost + 0.25D),
                        look.z * boost));
                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING, 100, 0));
                player.hurtMarked = true;
                applyPlayerTheme(player, theme, 100, cost);
                return true;
            }

            case "FLIGHT":
                player.addEffect(new MobEffectInstance(
                        MobEffects.LEVITATION, 70, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING, 140, 0));
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 100, 1));
                applyPlayerTheme(player, theme, 140, cost);
                return true;

            case "LEAP": {
                Vec3 look = player.getLookAngle().normalize();
                double boost = 0.6D + Math.min(0.9D, cost * 0.025D);
                player.setDeltaMovement(player.getDeltaMovement().add(
                        look.x * boost, 0.75D + boost * 0.3D, look.z * boost));
                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING, 120, 0));
                player.fallDistance = 0.0F;
                player.hurtMarked = true;
                return true;
            }

            case "SPEED":
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 240, cost >= 30 ? 2 : 1));
                applyPlayerTheme(player, theme, 240, cost);
                return true;

            case "STRENGTH":
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST, 240, cost >= 30 ? 2 : 1));
                applyPlayerTheme(player, theme, 240, cost);
                return true;

            case "SENSE":
            case "VISION":
                player.addEffect(new MobEffectInstance(
                        MobEffects.NIGHT_VISION, 300, 0));
                for (LivingEntity entity : nearby(player, player.position(), 16.0D)) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.GLOWING, 120, 0));
                }
                applyPlayerTheme(player, theme, 180, cost);
                return true;

            case "HEAL":
                player.heal(Math.min(10.0F, 4.0F + cost * 0.12F));
                player.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION, 160, cost >= 30 ? 1 : 0));
                applyPlayerTheme(player, theme, 160, cost);
                return true;

            case "BIND": {
                LivingEntity target = findTarget(player, 20.0D);
                if (target == null) return false;
                target.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, duration, 3));
                target.addEffect(new MobEffectInstance(
                        MobEffects.WEAKNESS, 100, 0));
                applyTargetTheme(player, target, theme, cost);
                if (hasAny(theme, "cosmic", "space", "gravity", "quantum", "dimension")) {
                    target.addEffect(new MobEffectInstance(
                            MobEffects.LEVITATION, 50, 0));
                }
                return true;
            }

            case "PULL":
            case "PUSH": {
                LivingEntity target = findTarget(player, 20.0D);
                if (target == null) return false;

                Vec3 direction = "PULL".equals(behavior)
                        ? player.position().subtract(target.position())
                        : target.position().subtract(player.position());

                if (direction.lengthSqr() < 0.01D) return false;

                Vec3 impulse = direction.normalize().scale(1.1D);
                target.setDeltaMovement(target.getDeltaMovement().add(
                        impulse.x, "PUSH".equals(behavior) ? 0.45D : 0.20D,
                        impulse.z));
                target.hurtMarked = true;
                applyTargetTheme(player, target, theme, cost);
                return true;
            }

            case "SUMMON":
            case "CONSTRUCT":
                return summon(player, theme);

            case "TRANSFORM":
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST, 240, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_RESISTANCE, 240, 1));
                player.addEffect(new MobEffectInstance(
                        MobEffects.ABSORPTION, 240, 1));
                applyPlayerTheme(player, theme, 240, cost);
                return true;

            case "UTILITY":
                player.removeEffect(MobEffects.POISON);
                player.removeEffect(MobEffects.WITHER);
                player.removeEffect(MobEffects.WEAKNESS);
                player.removeEffect(MobEffects.BLINDNESS);
                player.addEffect(new MobEffectInstance(
                        MobEffects.NIGHT_VISION, 240, 0));
                applyPlayerTheme(player, theme, 200, cost);
                for (LivingEntity entity : nearby(player, player.position(), 12.0D)) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.GLOWING, 100, 0));
                }
                return true;

            default:
                return false;
        }
    }

    private static boolean attackTarget(
            ServerPlayer player, String theme, int cost, float multiplier) {

        LivingEntity target = findTarget(player, 24.0D);
        if (target == null) return false;

        float damage = Math.min(18.0F,
                (3.5F + Math.min(8.0F, cost * 0.12F)) * multiplier);

        if (!target.hurt(player.damageSources().playerAttack(player), damage)) {
            return false;
        }

        applyTargetTheme(player, target, theme, cost);
        return true;
    }

    private static boolean areaAttack(
            ServerPlayer player, String theme, int cost,
            double radius, float multiplier) {

        LivingEntity aimed = findTarget(player, 24.0D);
        Vec3 look = player.getLookAngle().normalize();
        Vec3 center = aimed == null
                ? player.position().add(look.scale(5.0D))
                : aimed.position();

        AABB area = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius);

        List<LivingEntity> targets = player.level().getEntitiesOfClass(
                LivingEntity.class, area,
                entity -> entity != player && entity.isAlive()
                        && !entity.isAlliedTo(player));

        float damage = Math.min(16.0F,
                (3.0F + Math.min(8.0F, cost * 0.12F)) * multiplier);

        for (LivingEntity target : targets) {
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                applyTargetTheme(player, target, theme, cost);
            }
        }

        applyPlayerTheme(player, theme, 100, cost);
        return true;
    }

    private static boolean summon(ServerPlayer player, String theme) {
        EntityType<? extends Mob> type =
                hasAny(theme, "metal", "earth", "technology", "cosmic",
                        "space", "crystal", "energy", "quantum")
                ? EntityType.IRON_GOLEM : EntityType.WOLF;

        Mob ally = type.create(player.level());
        if (ally == null) return false;

        Vec3 look = player.getLookAngle().normalize();
        ally.moveTo(
                player.getX() + look.x * 2.0D,
                player.getY(),
                player.getZ() + look.z * 2.0D,
                player.getYRot(), 0.0F);

        ally.setPersistenceRequired();

        if (ally instanceof Wolf wolf) {
            wolf.setOwnerUUID(player.getUUID());
            wolf.setTame(true);
        }

        if (ally instanceof IronGolem golem) {
            golem.setPlayerCreated(true);
        }

        return player.level().addFreshEntity(ally);
    }

    private static void applyTargetTheme(
            ServerPlayer player, LivingEntity target, String theme, int cost) {

        if (hasAny(theme, "fire", "lava", "solar", "plasma")) {
            target.setSecondsOnFire(4);
        }

        if (hasAny(theme, "ice", "water", "crystal")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        }

        if (hasAny(theme, "lightning", "storm", "sonic", "sound")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
            target.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS, 80, 0));
        }

        if (hasAny(theme, "plant", "nature", "radiation")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.POISON, 100, 0));
        }

        if (hasAny(theme, "shadow", "illusion", "dream")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.BLINDNESS, 60, 0));
        }

        if (hasAny(theme, "cosmic", "space", "quantum", "dimension", "gravity", "void")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.LEVITATION, 40, 0));
            target.addEffect(new MobEffectInstance(
                    MobEffects.GLOWING, 100, 0));
            target.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS, 80, 0));
        }

        if (hasAny(theme, "blood", "death")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.WITHER, 40, 0));
            target.addEffect(new MobEffectInstance(
                    MobEffects.POISON, 80, 0));
            player.heal(Math.min(3.0F, 0.5F + cost * 0.025F));
        }

        if (hasAny(theme, "magnetic", "metal", "technology")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.GLOWING, 80, 0));
            target.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS, 80, 0));
        }

        if (hasAny(theme, "psychic", "magic", "time")) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            target.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS, 80, 0));
        }

        if (hasAny(theme, "wind", "kinetic")) {
            Vec3 away = target.position().subtract(player.position());
            if (away.lengthSqr() > 0.01D) {
                Vec3 push = away.normalize().scale(0.55D);
                target.push(push.x, 0.18D, push.z);
                target.hurtMarked = true;
            }
        }
    }

    private static void applyPlayerTheme(
            ServerPlayer player, String theme, int duration, int cost) {

        if (hasAny(theme, "blood")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, duration, 0));
            player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION, duration, 1));
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_BOOST, duration, 0));
        }

        if (hasAny(theme, "cosmic", "space", "quantum", "dimension", "void", "gravity")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE, duration, 1));
            player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION, duration, 1));
            player.addEffect(new MobEffectInstance(
                    MobEffects.SLOW_FALLING, duration, 0));
        }

        if (hasAny(theme, "fire", "lava", "plasma", "solar")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE, duration, 0));
        }

        if (hasAny(theme, "water")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.WATER_BREATHING, duration, 0));
        }

        if (hasAny(theme, "plant", "nature", "life", "light")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, duration, 0));
        }

        if (hasAny(theme, "storm", "lightning", "sonic", "wind", "kinetic")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, duration, 1));
        }

        if (hasAny(theme, "shadow", "illusion", "dream")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.INVISIBILITY, Math.min(duration, 160), 0));
        }

        if (hasAny(theme, "technology", "metal", "magnetic", "crystal")) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.ABSORPTION, duration, 1));
        }
    }

    private static List<LivingEntity> nearby(
            ServerPlayer player, Vec3 center, double radius) {

        AABB box = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius);

        return player.level().getEntitiesOfClass(
                LivingEntity.class, box,
                entity -> entity != player && entity.isAlive()
                        && !entity.isAlliedTo(player));
    }

    private static LivingEntity findTarget(ServerPlayer player, double range) {
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

    private static boolean isOffensiveAura(String theme) {
        return hasAny(theme, "blood", "fire", "lava", "plasma", "radiation",
                "storm", "lightning", "death", "void", "cosmic", "ice");
    }

    private static boolean hasAny(String text, String... terms) {
        String normalized = text == null ? "" : text.toLowerCase(Locale.ROOT);

        for (String term : terms) {
            if (normalized.contains(term)) return true;
        }

        return false;
    }
}