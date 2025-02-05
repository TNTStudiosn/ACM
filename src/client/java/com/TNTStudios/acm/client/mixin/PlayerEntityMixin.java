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

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void controlHornetMovement(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasVehicle() && player.getVehicle() instanceof HornetEntity hornet) {
            double forward = 0.0;
            double sideways = 0.0;

            // Verificamos si el jugador es del cliente para acceder a la entrada
            if (player instanceof ClientPlayerEntity clientPlayer) {
                forward = MinecraftClient.getInstance().options.forwardKey.isPressed() ? 1 : 0;
                sideways = MinecraftClient.getInstance().options.leftKey.isPressed() ? 1 : 0;
                sideways -= MinecraftClient.getInstance().options.rightKey.isPressed() ? 1 : 0;
            }

            // Suavizado del giro
            float targetYaw = player.getYaw();
            hornet.setYaw(hornet.getYaw() + (targetYaw - hornet.getYaw()) * 0.1f);

            // Movimiento basado en la dirección de la cámara
            float yawRad = (float) Math.toRadians(hornet.getYaw());
            double dx = -MathHelper.sin(yawRad) * forward * 0.3 + MathHelper.cos(yawRad) * sideways * 0.3;
            double dz = MathHelper.cos(yawRad) * forward * 0.3 + MathHelper.sin(yawRad) * sideways * 0.3;

            // Inercia suave
            Vec3d currentVelocity = hornet.getVelocity();
            Vec3d targetVelocity = new Vec3d(dx, currentVelocity.y, dz);
            Vec3d smoothedVelocity = currentVelocity.add(targetVelocity.subtract(currentVelocity).multiply(0.1));

            hornet.setVelocity(smoothedVelocity);
        }
    }
}
