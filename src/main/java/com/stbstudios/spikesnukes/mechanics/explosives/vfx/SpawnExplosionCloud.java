package com.stbstudios.spikesnukes.mechanics.explosives.vfx;

import com.stbstudios.spikesnukes.math.Math2;
import com.stbstudios.spikesnukes.particles.ModParticles;
import com.stbstudios.spikesnukes.particles.SpikesParticleEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpawnExplosionCloud {
    @OnlyIn(Dist.CLIENT)
    public static void makeCloud(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        for (double i=0;i<4000;i++) {
            SpikesParticleEngine.spawnParticle(x+(int)Math2.getRandomNumber(-15,15), y+(int)Math2.getRandomNumber(0,15), z+(int)Math2.getRandomNumber(-15,15), 1f);
            //level.addParticle(ModParticles.BASIC_EXPLOSION_PARTICLE.get(), x, y, z, 0, 0, 0);
        }
        //FlashBangScreenEffect.flash();
    }
}
