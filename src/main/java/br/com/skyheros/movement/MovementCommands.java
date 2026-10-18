package br.com.skyheros.movement;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "skyheros")
public final class MovementCommands {
    private MovementCommands() {}

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("skyheros")
                .then(Commands.literal("fly").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    var abilities = player.getAbilities();

                    boolean enabled = !abilities.mayfly;
                    abilities.mayfly = enabled;

                    if (!enabled) {
                        abilities.flying = false;
                    }

                    player.onUpdateAbilities();
                    context.getSource().sendSuccess(
                        () -> Component.literal(
                            enabled
                                ? "Skyheros: voo ativado."
                                : "Skyheros: voo desativado."
                        ),
                        false
                    );
                    return 1;
                }))
                .then(Commands.literal("speed").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();

                    player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED,
                        20 * 10,
                        1,
                        false,
                        true,
                        true
                    ));

                    context.getSource().sendSuccess(
                        () -> Component.literal(
                            "Skyheros: supervelocidade ativada por 10 segundos."
                        ),
                        false
                    );
                    return 1;
                }))
                .then(Commands.literal("leap").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Vec3 look = player.getLookAngle();
                    Vec3 current = player.getDeltaMovement();

                    player.setDeltaMovement(
                        current.x + look.x * 1.25,
                        Math.max(current.y, 0.0) + 0.85,
                        current.z + look.z * 1.25
                    );
                    player.hurtMarked = true;

                    context.getSource().sendSuccess(
                        () -> Component.literal(
                            "Skyheros: salto aprimorado!"
                        ),
                        false
                    );
                    return 1;
                }))
        );
    }
}