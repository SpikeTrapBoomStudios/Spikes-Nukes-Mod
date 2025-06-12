package com.stbstudios.spikesnukes.particles;

import com.stbstudios.spikesnukes.math.Math2;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;


public class BasicExplosionParticle extends TextureSheetParticle {
    private int elapsedTicks = 0;
    private float speed = 4.8f;
    private Vec3 shootDir = Vec3.ZERO;

    protected BasicExplosionParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, 0, 0, 0);
        this.lifetime = (int) Math2.getRandomNumber(200, 260);
        this.gravity = 0.0f;
        this.friction = 1.0f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = (float) Math2.getRandomNumber(1.5, 3.5);
        this.hasPhysics = false;

        float grayVal = (float) Math2.getRandomNumber(0.2,.4);
        this.rCol = grayVal;
        this.gCol = grayVal;
        this.bCol = grayVal;

        this.shootDir = new Vec3(Math2.getRandomNumber(-1f, 1f),Math2.getRandomNumber(-1f, 2f),Math2.getRandomNumber(-1f, 1f));

        this.setPos(x, y, z);
    }

    @Override
    public void tick() {
        elapsedTicks++;
        super.tick();
        if (this.lifetime - elapsedTicks <= 80) {
            this.alpha = Math2.lerp(this.alpha, 0f, .2f);
        }

        double newX = this.x + this.shootDir.x * speed;
        double newY = this.y + this.shootDir.y * speed;
        double newZ = this.z + this.shootDir.z * speed;

        speed = Math2.lerp(speed, .1f, .3f);
        setPos(newX, newY, newZ);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            BasicExplosionParticle particle = new BasicExplosionParticle(level, x, y, z, vx, vy, vz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
