package com.TNTStudios.acm.client.model;

import com.TNTStudios.acm.entity.HornetEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class HornetModel extends GeoModel<HornetEntity> {
    @Override
    public Identifier getModelResource(HornetEntity entity) {
        return new Identifier("acm", "geo/hornet_gekolib.geo.json");
    }

    @Override
    public Identifier getTextureResource(HornetEntity entity) {
        return new Identifier("acm", "textures/entity/hornet.png");
    }

    @Override
    public Identifier getAnimationResource(HornetEntity entity) {
        return new Identifier("acm", "animations/hornet.animation.json");
    }
}
