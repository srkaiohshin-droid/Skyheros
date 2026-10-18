package br.com.skyheros.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FlightRigTestEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Integer> ANIMATION_STATE =
            SynchedEntityData.defineId(
                    FlightRigTestEntity.class,
                    EntityDataSerializers.INT
            );

    private static final RawAnimation HOVER =
            RawAnimation.begin().thenLoop("animation.skyheros.hover");
    private static final RawAnimation TAKEOFF =
            RawAnimation.begin().thenPlay("animation.skyheros.takeoff");
    private static final RawAnimation FORWARD =
            RawAnimation.begin().thenLoop("animation.skyheros.fly_forward");
    private static final RawAnimation LANDING = RawAnimation.begin().thenPlayAndHold("animation.skyheros.landing");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlightRigTestEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        setNoAi(true);
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ANIMATION_STATE, 0);
    }

    @Override
    protected void registerGoals() {
        // Entidade de teste estacionária: não anda nem ataca.
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "flight_test", 6, state -> {
            return switch (entityData.get(ANIMATION_STATE)) {
                case 1 -> state.setAndContinue(TAKEOFF);
                case 2 -> state.setAndContinue(FORWARD);
                case 3 -> state.setAndContinue(LANDING);
                default -> state.setAndContinue(HOVER);
            };
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return super.mobInteract(player, hand);
        }

        if (!level().isClientSide) {
            int next = (entityData.get(ANIMATION_STATE) + 1) % 4;
            entityData.set(ANIMATION_STATE, next);

            String name = switch (next) {
                case 1 -> "Decolagem";
                case 2 -> "Voo para frente";
                case 3 -> "Pouso";
                default -> "Pairar";
            };

            player.displayClientMessage(
                    Component.literal("Skyheros Flight Rig: " + name),
                    true
            );
        }

        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
