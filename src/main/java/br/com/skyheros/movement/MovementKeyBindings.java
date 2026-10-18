package br.com.skyheros.movement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public final class MovementKeyBindings {
    private MovementKeyBindings() {}

    public static final KeyMapping FLIGHT = new KeyMapping(
        "key.skyheros.flight",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        "key.categories.skyheros"
    );

    public static final KeyMapping SPEED = new KeyMapping(
        "key.skyheros.speed",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_H,
        "key.categories.skyheros"
    );

    public static final KeyMapping LEAP = new KeyMapping(
        "key.skyheros.leap",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_J,
        "key.categories.skyheros"
    );

    @Mod.EventBusSubscriber(
        modid = "skyheros",
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
    )
    public static final class Registration {
        private Registration() {}

        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(FLIGHT);
            event.register(SPEED);
            event.register(LEAP);
        }
    }

    @Mod.EventBusSubscriber(
        modid = "skyheros",
        value = Dist.CLIENT
    )
    public static final class ClientEvents {
        private ClientEvents() {}

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }

            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player == null || minecraft.getConnection() == null) {
                return;
            }

            while (FLIGHT.consumeClick()) {
                minecraft.getConnection().sendCommand("skyheros fly");
            }

            while (SPEED.consumeClick()) {
                minecraft.getConnection().sendCommand("skyheros speed");
            }

            while (LEAP.consumeClick()) {
                minecraft.getConnection().sendCommand("skyheros leap");
            }
        }
    }
}