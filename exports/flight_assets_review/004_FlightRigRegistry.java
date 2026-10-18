package br.com.skyheros.entity;

import br.com.skyheros.SkyherosMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FlightRigRegistry {
    private FlightRigRegistry() {}

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkyherosMod.MOD_ID);

    public static final RegistryObject<EntityType<FlightRigTestEntity>> FLIGHT_RIG_TEST =
            ENTITY_TYPES.register("flight_rig_test", () ->
                    EntityType.Builder.<FlightRigTestEntity>of(
                                    FlightRigTestEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.8F, 2.0F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build(SkyherosMod.MOD_ID + ":flight_rig_test")
            );

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
