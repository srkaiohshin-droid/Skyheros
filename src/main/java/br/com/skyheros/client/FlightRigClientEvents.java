package br.com.skyheros.client;

import br.com.skyheros.SkyherosMod;
import br.com.skyheros.client.render.FlightRigTestRenderer;
import br.com.skyheros.entity.FlightRigRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = SkyherosMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class FlightRigClientEvents {
    private FlightRigClientEvents() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                FlightRigRegistry.FLIGHT_RIG_TEST.get(),
                FlightRigTestRenderer::new
        );
    }
}
