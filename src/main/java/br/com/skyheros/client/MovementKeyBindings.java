package br.com.skyheros.client;

import br.com.skyheros.SkyherosMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SkyherosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MovementKeyBindings {
    public static final KeyMapping FLIGHT = new KeyMapping(
        "key.skyheros.flight", GLFW.GLFW_KEY_G, "key.categories.skyheros"
    );
    public static final KeyMapping SPEED = new KeyMapping(
        "key.skyheros.speed", GLFW.GLFW_KEY_H, "key.categories.skyheros"
    );
    public static final KeyMapping LEAP = new KeyMapping(
        "key.skyheros.leap", GLFW.GLFW_KEY_J, "key.categories.skyheros"
    );

    private MovementKeyBindings() {}

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(FLIGHT);
        event.register(SPEED);
        event.register(LEAP);
    }
}