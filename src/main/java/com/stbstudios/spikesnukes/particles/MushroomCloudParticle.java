package com.stbstudios.spikesnukes.particles;

import com.stbstudios.spikesnukes.math.Math2;
import com.stbstudios.spikesnukes.math.Vector2D;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class MushroomCloudParticle extends TextureSheetParticle {
    //Global Stuff
    public static int VARIANT1_RESET_TICK = 140;
    private int particleVariant = 0;
    private final double rotationAngle;
    private final double originX, originY, originZ;
    private double localX, localY, localZ;
    private final double offsetX, offsetZ;

    //Part A Stuff
    private int tickCounter;

    //Part B Stuff
    private double swirlAngle;
    private double swirlRadius;
    private double swirlIncrement;

    //Particle Init
    protected MushroomCloudParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, 0, 0, 0);
        this.lifetime = 2000;
        this.gravity = 0.0f;
        this.friction = 1.0f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = (float) Math2.getRandomNumber(2.0, 6.5);
        this.hasPhysics = false;

        //Custom Stuff
        //Yes I hacked a bit and used vx and vy as custom params in my method, but try looking up how to pass custom args to a particle...
        //You'll see why I did what I did.
        this.particleVariant = (int) vy; //Variant 0 = Swirly Wirly , Variant 1 = Smoke Pillar
        //I could implement a cool system to make the particles uniformly spread across a circle, but random is more better and funner ;)
        double randomRotation = Math.random() * 2.0 * Math.PI; this.rotationAngle = randomRotation;
        double radiusOffset = (particleVariant == 1) ? 20 : 0;

        this.offsetZ = Math.cos(randomRotation) * radiusOffset;
        this.offsetX = -Math.sin(randomRotation) * radiusOffset;

        this.originX = x;
        this.originY = y;
        this.originZ = z;
        this.localX = x;
        this.localY = y;
        this.localZ = z;

        //Variant Init
        if (particleVariant == 0) {
            this.swirlRadius = 0.3;
            this.swirlIncrement = 0.025;
            for (double i = 0; i < vx; i += swirlIncrement) {
                this.localY += Math.cos(i) * swirlRadius;
                this.localZ += Math.sin(i) * swirlRadius * 1.5;
            }
            this.swirlAngle = vx;
        } else if (particleVariant == 1) {
            for (int i = 0; i <= (int) vx; i++) {
                partA();
                this.tickCounter++;
            }
        }

        this.setPos(localX, localY, localZ);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void partB() {
        swirlAngle += swirlIncrement;
        if (swirlAngle >= 2 * Math.PI) swirlAngle -= 2 * Math.PI;

        float rStart, rEnd, gStart, gEnd, bStart, bEnd;
        if (swirlAngle < Math.PI) {
            rStart = 255; rEnd = 115;
            gStart = 75; gEnd = 115;
            bStart = 0; bEnd = 115;
        } else {
            rStart = 125; rEnd = 255;
            gStart = 115; gEnd = 75;
            bStart = 115; bEnd = 0;
        }
        float progress = (float) ((swirlAngle % Math.PI) / Math.PI);
        this.rCol = Math2.interpolateColor(rStart,rEnd,progress);
        this.gCol = Math2.interpolateColor(gStart,gEnd,progress);
        this.bCol = Math2.interpolateColor(bStart,bEnd,progress);

        double verticalMovement = Math.cos(swirlAngle) * swirlRadius;
        double horizontalMovement = Math.sin(swirlAngle) * swirlRadius * 1.5;
        localY += verticalMovement;
        localZ += horizontalMovement;
        double relativeX = localX - originX;
        double relativeY = localY - originY;
        double relativeZ = localZ - originZ;

        Vector2D rotatedXZ = Math2.rotateCoordinate2D(relativeX, relativeZ, rotationAngle).offset(offsetX, offsetZ).offset(originX, originZ);
        this.setPos(rotatedXZ.x, originY + relativeY, rotatedXZ.y);
    }

    public void partA() {
        double timeInSeconds = (double) tickCounter / 20;
        double particleHeight = Math.pow(3, 2 * timeInSeconds - 3);
        double previousParticleHeight = Math.pow(3, 2 * timeInSeconds - 3 - 0.1);

        float rStart = 115, rEnd = 255;
        float gStart = 115, gEnd = 75;
        float bStart = 115, bEnd = 0;
        float progress = (float) tickCounter / VARIANT1_RESET_TICK;
        this.rCol = Math2.interpolateColor(rStart,rEnd,progress);
        this.gCol = Math2.interpolateColor(gStart,gEnd,progress);
        this.bCol = Math2.interpolateColor(bStart,bEnd,progress);

        double deltaX = 1.0 / 20.0;
        double deltaY = particleHeight - previousParticleHeight;
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX = (deltaX / magnitude) * 0.5;
        deltaY = (deltaY / magnitude) * 0.5;

        localY += deltaY;
        localZ -= deltaX;
        double relativeX = localX - originX;
        double relativeY = localY - originY;
        double relativeZ = localZ - originZ;
        double rotatedX = relativeX * Math.cos(rotationAngle) - relativeZ * Math.sin(rotationAngle);
        double rotatedZ = relativeX * Math.sin(rotationAngle) + relativeZ * Math.cos(rotationAngle);
        rotatedX += offsetX;
        rotatedZ += offsetZ;

        this.setPos(originX + rotatedX, originY + relativeY, originZ + rotatedZ);
    }

    @Override
    public void tick() {
        if (tickCounter >= VARIANT1_RESET_TICK && particleVariant == 1) {
            tickCounter = 0;
            localX = originX;
            localY = originY;
            localZ = originZ;
            this.setPos(localX + offsetX, localY, localZ + offsetZ);
        }
        super.tick();
        if (particleVariant == 0) {
            partB();
        } else if (particleVariant == 1) {
            partA();
        }
        tickCounter++;
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
