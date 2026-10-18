package br.com.skyheros.client.render;

import br.com.skyheros.entity.FlightRigTestEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FlightRigTestRenderer extends GeoEntityRenderer<FlightRigTestEntity> {
    public FlightRigTestRenderer(EntityRendererProvider.Context context) {
        super(context, new FlightRigTestModel());
    }
}
