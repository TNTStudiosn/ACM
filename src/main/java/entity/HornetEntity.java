package com.TNTStudios.acm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HornetEntity extends PathAwareEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private Vec3d previousVelocity = Vec3d.ZERO;
    private float smoothYaw;

    public HornetEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.smoothYaw = this.getYaw();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Controladores de animación si es necesario
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.hasPassenger(player)) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasPassengers() && this.getFirstPassenger() instanceof PlayerEntity player) {
            // Actualización constante del Yaw, sin depender del movimiento
            float targetYaw = player.getYaw();
            smoothYaw = smoothRotation(smoothYaw, targetYaw, 5.0f); // Suavizado del giro (ajustado)

            this.setYaw(smoothYaw);
            this.prevYaw = this.getYaw();

            // Movimiento independiente de la rotación
            float yawRad = (float) Math.toRadians(this.getYaw());
            double forward = player.forwardSpeed * 0.3;
            double sideways = player.sidewaysSpeed * 0.3;

            double dx = -MathHelper.sin(yawRad) * forward + MathHelper.cos(yawRad) * sideways;
            double dz = MathHelper.cos(yawRad) * forward + MathHelper.sin(yawRad) * sideways;

            // Suavizado del movimiento (inercia)
            Vec3d targetVelocity = new Vec3d(dx, 0, dz);
            previousVelocity = previousVelocity.add(targetVelocity.subtract(previousVelocity).multiply(0.1));

            // Simulación de bamboleo ligero (helicóptero en vuelo)
            double oscillation = MathHelper.sin(this.age * 0.1f) * 0.02;

            this.setVelocity(previousVelocity.add(0, oscillation, 0));
            this.move(MovementType.SELF, this.getVelocity());
        }
    }

    // Suavizado del giro con interpolación angular correcta
    private float smoothRotation(float current, float target, float speed) {
        float delta = MathHelper.wrapDegrees(target - current);
        return current + MathHelper.clamp(delta, -speed, speed);
    }


}