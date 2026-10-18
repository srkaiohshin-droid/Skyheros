package br.com.skyheros.entity;

import br.com.skyheros.SkyherosMod;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SkyherosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FlightRigTestEvents {
    private FlightRigTestEvents() {}

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                FlightRigRegistry.FLIGHT_RIG_TEST.get(),
                Monster.createMonsterAttributes().build()
        );
    }
}
