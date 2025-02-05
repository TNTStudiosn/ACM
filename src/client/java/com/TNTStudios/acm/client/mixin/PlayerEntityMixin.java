package com.TNTStudios.acm.client.mixin;

import com.TNTStudios.acm.entity.HornetEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private double speed = 0.0;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void controlHornetMovement(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasVehicle() && player.getVehicle() instanceof HornetEntity hornet) {
            double forward = 0.0;
            double sideways = 0.0;

            if (player instanceof ClientPlayerEntity) {
                MinecraftClient client = MinecraftClient.getInstance();
                forward = client.options.forwardKey.isPressed() ? 1 : 0;
                sideways = (client.options.leftKey.isPressed() ? 1 : 0) - (client.options.rightKey.isPressed() ? 1 : 0);
            }

            // Movimiento con aceleración/desaceleración suave
            double targetSpeed = forward * 0.5;
            speed += (targetSpeed - speed) * 0.1;

            float yawRad = (float) Math.toRadians(player.getYaw());
            double dx = -MathHelper.sin(yawRad) * speed + MathHelper.cos(yawRad) * sideways * 0.3;
            double dz = MathHelper.cos(yawRad) * speed + MathHelper.sin(yawRad) * sideways * 0.3;

            Vec3d currentVelocity = hornet.getVelocity();
            Vec3d targetVelocity = new Vec3d(dx, currentVelocity.y, dz);
            Vec3d smoothedVelocity = currentVelocity.add(targetVelocity.subtract(currentVelocity).multiply(0.15));

            hornet.setVelocity(smoothedVelocity);
        }
    }
}