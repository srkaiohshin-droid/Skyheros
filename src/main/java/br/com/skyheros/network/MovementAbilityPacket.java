package br.com.skyheros.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public final class MovementAbilityPacket {
    public static final int FLIGHT = 0;
    public static final int SPEED = 1;
    public static final int LEAP = 2;

    private final int action;

    public MovementAbilityPacket(int action) {
        this.action = action;
    }

    public static void encode(MovementAbilityPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.action);
    }

    public static MovementAbilityPacket decode(FriendlyByteBuf buffer) {
        return new MovementAbilityPacket(buffer.readVarInt());
    }

    public static void handle(MovementAbilityPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || player.isSpectator()) return;

            switch (packet.action) {
                case FLIGHT -> {
                    if (player.getAbilities().instabuild) {
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("O voo já está disponível no Criativo."),
                            true
                        );
                        return;
                    }

                    if (player.getAbilities().mayfly) {
                        player.getAbilities().flying = false;
                        player.getAbilities().mayfly = false;
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("Voo de herói desativado."),
                            true
                        );
                    } else {
                        player.getAbilities().mayfly = true;
                        player.getAbilities().flying = true;
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("Voo de herói ativado."),
                            true
                        );
                    }
                    player.onUpdateAbilities();
                }
                case SPEED -> {
                    if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                        player.removeEffect(MobEffects.MOVEMENT_SPEED);
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("Supervelocidade desativada."),
                            true
                        );
                    } else {
                        player.addEffect(new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED, 20 * 60 * 5, 2, false, false, true
                        ));
                        player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal("Supervelocidade ativada."),
                            true
                        );
                    }
                }
                case LEAP -> {
                    Vec3 look = player.getLookAngle();
                    Vec3 velocity = player.getDeltaMovement();
                    player.setDeltaMovement(
                        velocity.x + look.x * 1.15,
                        Math.max(velocity.y, 0.85),
                        velocity.z + look.z * 1.15
                    );
                    player.fallDistance = 0;
                    player.hurtMarked = true;
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                    player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("Salto aprimorado!"),
                        true
                    );
                }
                default -> player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Habilidade de movimentação desconhecida."),
                    true
                );
            }
        });
        context.setPacketHandled(true);
    }
}