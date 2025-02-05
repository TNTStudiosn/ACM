package com.TNTStudios.acm.client;

import com.TNTStudios.acm.client.renderer.HornetRenderer;
import com.TNTStudios.acm.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.HORNET, HornetRenderer::new);
    }
}
