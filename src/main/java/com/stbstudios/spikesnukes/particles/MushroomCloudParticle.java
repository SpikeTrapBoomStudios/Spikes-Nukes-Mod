package com.stbstudios.spikesnukes.particles;

import com.stbstudios.spikesnukes.math.Math2;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class MushroomCloudParticle extends TextureSheetParticle {
    public static int resetTick = 140;

    private int mpType = 0;
    private double angle;
    private final double xRot;
    private final double ox;
    private final double oy;
    private final double oz;
    private double px,py,pz;
    private final double offX,offZ;
    private int elapsedTicks;
    private double swirlRadius;
    private double swirlInc;

    protected MushroomCloudParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, 0, 0, 0);
        this.mpType = (int) vy;

        this.lifetime = 2000;
        this.gravity = 0.0f;
        this.friction = 1.0f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        double txRot = Math.random() * 2.0 * Math.PI;

        double offsetRadius = 0;
        if (mpType==1) {
            offsetRadius = 20;
        }

        this.offZ = Math.cos(txRot)*offsetRadius;
        this.offX = -Math.sin(txRot)*offsetRadius;

        this.xRot = txRot;

        this.ox = x;
        this.oy = y;
        this.oz = z;
        this.px = x;
        this.py = y;
        this.pz = z;

        if (mpType==0) {
            this.swirlRadius = 0.3;
            this.swirlInc = 0.025;
            for (double i = 0; i < vx; i += swirlInc) {
                this.py += Math.cos(i) * swirlRadius;
                this.pz += Math.sin(i) * swirlRadius * 1.5;
            }
            this.angle = vx;
        } else if (mpType==1) {
            for (int i=0;i<=vx;i++) {
                partA();
                this.elapsedTicks+=1;
            }
        }
        this.quadSize = (float) (Math2.getRandomNumber(2.5,6.5));
        this.hasPhysics = false;
        this.setPos(px,py,pz);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void partB() {
        // Increment the swirl angle and keep it within 0 to 2π range
        angle += swirlInc;
        if (angle >= 2 * Math.PI) angle -= 2 * Math.PI;

        // Determine color interpolation based on the angle range
        float rStart, rEnd, gStart, gEnd, bStart, bEnd;
        if (angle < Math.PI) {
            rStart = 255; rEnd = 115;
            gStart = 75; gEnd = 115;
            bStart = 0; bEnd = 115;
        } else {
            rStart = 125; rEnd = 255;
            gStart = 115; gEnd = 75;
            bStart = 115; bEnd = 0;
        }

        // Interpolate color based on the angle
        float progress = (float) ((angle % Math.PI) / Math.PI);
        this.rCol = (rStart + progress * (rEnd - rStart)) / 255;
        this.gCol = (gStart + progress * (gEnd - gStart)) / 255;
        this.bCol = (bStart + progress * (bEnd - bStart)) / 255;

        // Calculate movement based on swirl motion
        double verticalMovement = Math.cos(angle) * swirlRadius;
        double horizontalMovement = Math.sin(angle) * swirlRadius * 1.5; // Slightly stretched swirl in z-direction

        py += verticalMovement;
        pz += horizontalMovement;

        // Compute relative position adjustments
        double relativeX = px - ox;
        double relativeY = py - oy;
        double relativeZ = pz - oz;

        // Apply rotation transformation (around x-axis)
        double rotatedX = relativeX * Math.cos(xRot) - relativeZ * Math.sin(xRot);
        double rotatedZ = relativeX * Math.sin(xRot) + relativeZ * Math.cos(xRot);

        // Apply additional offsets
        rotatedX += offX;
        rotatedZ += offZ;

        // Set final particle position
        this.setPos(ox + rotatedX, oy + relativeY, oz + rotatedZ);
    }

    public void partA() {
        // Convert elapsed ticks into a scaled time unit
        double timeInSeconds = (double) elapsedTicks / 20;

        // Calculate particle height based on an exponential function
        double particleHeight = Math.pow(3, 2 * timeInSeconds - 3);
        double previousParticleHeight = Math.pow(3, 2 * timeInSeconds - 3 - 0.1); // Offset by 1/10 for previous height

        // Define color interpolation start and end values
        float rStart = 115, rEnd = 255;
        float gStart = 115, gEnd = 75;
        float bStart = 115, bEnd = 0;

        // Interpolate colors over time based on elapsedTicks
        float progress = (float) elapsedTicks / resetTick;
        this.rCol = ((rStart + progress * (rEnd - rStart)) / 255);
        this.gCol = ((gStart + progress * (gEnd - gStart)) / 255);
        this.bCol = ((bStart + progress * (bEnd - bStart)) / 255);

        // Compute directional movement based on height change
        double deltaX = 1.0 / 20.0;
        double deltaY = particleHeight - previousParticleHeight;
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalize and scale movement
        deltaX = (deltaX / magnitude) * 0.5;
        deltaY = (deltaY / magnitude) * 0.5;

        // Update particle position along the defined path
        py += deltaY;
        pz -= deltaX;

        // Relative position calculations for transformation
        double relativeX = px - ox;
        double relativeY = py - oy;
        double relativeZ = pz - oz;

        // Apply rotation transformation (around x-axis)
        double rotatedX = relativeX * Math.cos(xRot) - relativeZ * Math.sin(xRot);
        double rotatedZ = relativeX * Math.sin(xRot) + relativeZ * Math.cos(xRot);

        // Apply additional offsets
        rotatedX += offX;
        rotatedZ += offZ;

        // Set final particle position
        this.setPos(ox + rotatedX, oy + relativeY, oz + rotatedZ);
    }

    @Override
    public void tick() {
        if (elapsedTicks>=resetTick && mpType==1) {
            elapsedTicks = 0;
            this.px = ox;
            this.py = oy;
            this.pz = oz;
            this.setPos(px+offX,py,pz+offZ);
        }
        super.tick();
        if (mpType==0) {
            partB();
        } else if (mpType==1) {
            partA();
        }
        elapsedTicks+=1;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            MushroomCloudParticle particle = new MushroomCloudParticle(level, x, y, z, vx, vy, vz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
