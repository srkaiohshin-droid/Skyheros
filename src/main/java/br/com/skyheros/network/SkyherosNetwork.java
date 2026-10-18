package br.com.skyheros.network;

import br.com.skyheros.SkyherosMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkDirection;

public final class SkyherosNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(SkyherosMod.MOD_ID, "main"),
        () -> PROTOCOL,
        PROTOCOL::equals,
        PROTOCOL::equals
    );
    private static boolean registered = false;

    private SkyherosNetwork() {}

    public static synchronized void register() {
        if (registered) return;
        CHANNEL.messageBuilder(MovementAbilityPacket.class, 0, NetworkDirection.PLAY_TO_SERVER)
            .encoder(MovementAbilityPacket::encode)
            .decoder(MovementAbilityPacket::decode)
            .consumerMainThread(MovementAbilityPacket::handle)
            .add();
        registered = true;
    }
}