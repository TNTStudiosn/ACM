package com.TNTStudios.acm;

import com.TNTStudios.acm.entity.HornetEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<HornetEntity> HORNET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("acm", "hornet"),
            EntityType.Builder.create(HornetEntity::new, SpawnGroup.MISC)
                    .setDimensions(3.0f, 2.0f)
                    .build("hornet")
    );

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(HORNET, HornetEntity.createAttributes());
    }
}
