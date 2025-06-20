package com.stbstudios.spikesnukes.particles;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.stbstudios.spikesnukes.math.Math2;
import com.stbstudios.spikesnukes.math.Vector2D;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

//EXPECTS: vx to be the instance order number (unique) and vy to be the variant (0 = Gas Swirl, 1 = Mushroom Shaft)
public class MushroomCloudParticle extends TextureSheetParticle {
    //Global Stuff
    private int particleVariant = 0;
    private final double rotationAngle;
    private final double originX, originY, originZ;
    private double localX, localY, localZ;
    private final double offsetX, offsetZ;

    //Mushroom Shaft Stuff
    private int tickCounter;
    private final float shaftAccelSpeed = 2f;

    //Gas Swirl Stuff
    private double swirlAngle;
    private double swirlRadius = 2.0;
    private double swirlIncrement = .05;

    //Particle Init
    protected MushroomCloudParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, 0, 0, 0);
        this.lifetime = 15 * 20;
        this.gravity = 0.0f;
        this.friction = 1.0f;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.quadSize = (float) Math2.getRandomNumber(15.0, 18.0);
        this.hasPhysics = false;

        //Custom Stuff
        //Yes I hacked a bit and used vx and vy as custom params in my method, but try looking up how to pass custom args to a particle...
        //You'll see why I did what I did.
        this.particleVariant = (int) vy; //Variant 0 = Swirly Wirly , Variant 1 = Smoke Pillar
        //I could implement a cool system to make the particles uniformly spread across a circle, but random is more better and funner ;)
        double randomRotation = Math.random() * 2.0 * Math.PI; this.rotationAngle = randomRotation;
        double radiusOffset = (particleVariant == 2) ? 50 : 60;
        if (particleVariant == 1) radiusOffset = 90;

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
            //GAS SWIRL
            this.swirlRadius = 1.5;
            this.swirlIncrement = .025;
            for (double i = 0; i < vx; i += swirlIncrement) {
                this.localY += Math.cos(i) * swirlRadius;
                this.localZ += Math.sin(i) * swirlRadius * 4.0;
            }
            this.swirlAngle = vx;
        } else if (particleVariant == 1) {
            //GAS SWIRL MINI
            this.swirlRadius = .65;
            this.swirlIncrement = 0.2;
            for (double i = 0; i < vx; i += swirlIncrement) {
                this.localY += Math.cos(i) * swirlRadius;
                this.localZ += Math.sin(i) * swirlRadius * 2.0;
            }
            this.swirlAngle = vx;
        } else if (particleVariant == 2) {
            //MUSHROOM SHAFT
            for (int i = 0; i <= (int) vx/2; i++) {
                mushroomCloudShaft();
                this.tickCounter++;
            }
        } else if (particleVariant == 3) {
            //GAS SWIRL INNER 1
            this.swirlRadius = 1.5;
            this.swirlIncrement = 0.025;
            for (double i = 0; i < vx; i += swirlIncrement) {
                this.localY += Math.cos(i) * swirlRadius;
                this.localZ += Math.sin(i) * swirlRadius * 2.5;
            }
            this.swirlAngle = vx;
        } else if (particleVariant == 4) {
            //GAS SWIRL INNER 2
            this.swirlRadius = 1.5;
            this.swirlIncrement = 0.025;
            for (double i = 0; i < vx; i += swirlIncrement) {
                this.localY += Math.cos(i) * swirlRadius;
                this.localZ += Math.sin(i) * swirlRadius * 3.2;
            }
            this.swirlAngle = vx;
        }

        this.setPos(localX, localY, localZ);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void mushroomCloudGasSwirl() {
        swirlAngle += swirlIncrement;
        if (swirlAngle >= 2 * Math.PI) swirlAngle -= 2 * Math.PI;

        float rStart, rEnd, gStart, gEnd, bStart, bEnd;
        if (swirlAngle < Math.PI) {
            rStart = 145; rEnd = 115;
            gStart = 56; gEnd = 115;
            bStart = 24; bEnd = 115;
        } else {
            rStart = 125; rEnd = 145;
            gStart = 115; gEnd = 56;
            bStart = 115; bEnd = 24;
        }
        float progress = (float) ((swirlAngle % Math.PI) / Math.PI);
        this.rCol = Math2.interpolateColor(rStart,rEnd,progress);
        this.gCol = Math2.interpolateColor(gStart,gEnd,progress);
        this.bCol = Math2.interpolateColor(bStart,bEnd,progress);

        double verticalMovement = Math.cos(swirlAngle) * swirlRadius;
        double horizontalMovement = 0;
        if (particleVariant == 0) {
            horizontalMovement = Math.sin(swirlAngle) * swirlRadius * 4.0;
        } else if (particleVariant == 1) {
            horizontalMovement = Math.sin(swirlAngle) * swirlRadius * 2.0;
        } else if (particleVariant == 3) {
            horizontalMovement = Math.sin(swirlAngle) * swirlRadius * 2.5;
        } else if (particleVariant == 4) {
            horizontalMovement = Math.sin(swirlAngle) * swirlRadius * 3.2;
        }
        localY += verticalMovement;
        localZ += horizontalMovement;
        double relativeX = localX - originX;
        double relativeY = localY - originY;
        double relativeZ = localZ - originZ;

        Vector2D rotatedXZ = Math2.rotateCoordinate2D(relativeX, relativeZ, rotationAngle).offset(offsetX, offsetZ).offset(originX, originZ);
        this.setPos(rotatedXZ.x, originY + relativeY, rotatedXZ.y);
    }

    public double shaftExpEq(double x) {
        return Math.pow(6, 1.5 * x - 2);
    }

    public void mushroomCloudShaft() {
        double timeInSeconds = (double) tickCounter / 20;
        double particleHeight = shaftExpEq(timeInSeconds);
        double previousParticleHeight = shaftExpEq(timeInSeconds - .05);

        float rStart = 115, rEnd = 145;
        float gStart = 115, gEnd = 56;
        float bStart = 115, bEnd = 24;
        float progress = (float) (this.y-originY) / 335;
        this.rCol = Math2.interpolateColor(rStart,rEnd,progress);
        this.gCol = Math2.interpolateColor(gStart,gEnd,progress);
        this.bCol = Math2.interpolateColor(bStart,bEnd,progress);

        double deltaX = 1.0 / 20.0;
        if (Math.abs(particleHeight - previousParticleHeight) >= 20) {
            previousParticleHeight = particleHeight - 1;
        }
        double deltaY = particleHeight - previousParticleHeight;
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX = (deltaX / magnitude) * .5;
        deltaY = (deltaY / magnitude) * shaftAccelSpeed;
        deltaY = Math.abs(deltaY);

        localY += deltaY;
        localZ -= deltaX;
        double relativeX = localX - originX;
        double relativeY = localY - originY;
        double relativeZ = localZ - originZ;

        Vector2D rotatedXZ = Math2.rotateCoordinate2D(relativeX, relativeZ, rotationAngle).offset(offsetX, offsetZ).offset(originX, originZ);
        this.setPos(rotatedXZ.x, originY + relativeY, rotatedXZ.y);
    }

    @Override
    public void tick() {
        if (level.getRawBrightness(BlockPos.containing(this.getPos()), 0) <= 0) {
            this.alpha = 0f;
        } else {
            this.alpha = 1f;
        }
        if ((this.y-originY >= 355) && particleVariant == 2) {
            tickCounter = 0;
            localX = originX;
            localY = originY;
            localZ = originZ;
            this.setPos(localX + offsetX, localY, localZ + offsetZ);
        }
        super.tick();
        if (level.getRawBrightness(BlockPos.containing(this.getPos()), 0) <= 0) {
            this.alpha = 0f;
        } else {
            this.alpha = 1f;
        }

        if (particleVariant == 0 || particleVariant == 1 || particleVariant == 3 || particleVariant == 4) {
            mushroomCloudGasSwirl();
        } else if (particleVariant == 2) {
            mushroomCloudShaft();
        }
        tickCounter++;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            MushroomCloudParticle particle = new MushroomCloudParticle(level, x, y, z, vx, vy, vz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
