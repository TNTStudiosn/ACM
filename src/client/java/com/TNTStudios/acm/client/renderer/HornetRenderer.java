package com.TNTStudios.acm.client.renderer;

import com.TNTStudios.acm.client.model.HornetModel;
import com.TNTStudios.acm.entity.HornetEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class HornetRenderer extends GeoEntityRenderer<HornetEntity> {
    public HornetRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HornetModel());
        addRenderLayer(new GeoRenderLayer<>(this) {});
    }
}
