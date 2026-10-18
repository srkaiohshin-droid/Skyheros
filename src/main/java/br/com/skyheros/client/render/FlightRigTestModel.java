package br.com.skyheros.client.render;

import br.com.skyheros.SkyherosMod;
import br.com.skyheros.entity.FlightRigTestEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlightRigTestModel extends GeoModel<FlightRigTestEntity> {
    @Override
    public ResourceLocation getModelResource(FlightRigTestEntity entity) {
        return new ResourceLocation(
                SkyherosMod.MOD_ID,
                "geo/flight_rig.geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(FlightRigTestEntity entity) {
        return new ResourceLocation(
                SkyherosMod.MOD_ID,
                "textures/entity/flight_rig.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(FlightRigTestEntity entity) {
        return new ResourceLocation(
                SkyherosMod.MOD_ID,
                "animations/flight_rig.animation.json"
        );
    }
}
