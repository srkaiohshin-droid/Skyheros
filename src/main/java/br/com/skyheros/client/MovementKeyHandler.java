package br.com.skyheros.client;

import br.com.skyheros.SkyherosMod;
import br.com.skyheros.network.MovementAbilityPacket;
import br.com.skyheros.network.SkyherosNetwork;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyherosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class MovementKeyHandler {
    private MovementKeyHandler() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;

        while (MovementKeyBindings.FLIGHT.consumeClick()) {
            SkyherosNetwork.CHANNEL.sendToServer(
                new MovementAbilityPacket(MovementAbilityPacket.FLIGHT)
            );
        }
        while (MovementKeyBindings.SPEED.consumeClick()) {
            SkyherosNetwork.CHANNEL.sendToServer(
                new MovementAbilityPacket(MovementAbilityPacket.SPEED)
            );
        }
        while (MovementKeyBindings.LEAP.consumeClick()) {
            SkyherosNetwork.CHANNEL.sendToServer(
                new MovementAbilityPacket(MovementAbilityPacket.LEAP)
            );
        }
    }
}