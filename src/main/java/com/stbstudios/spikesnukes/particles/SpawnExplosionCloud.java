package com.stbstudios.spikesnukes.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpawnExplosionCloud {
    @OnlyIn(Dist.CLIENT)
    public static void makeCloud(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        for (double i=0;i<250;i++) {
            level.addParticle(ModParticles.BASIC_EXPLOSION_PARTICLE.get(), x, y, z, 0, 0, 0);
        }
    }
}
