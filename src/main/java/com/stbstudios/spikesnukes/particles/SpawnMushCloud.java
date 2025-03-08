package com.stbstudios.spikesnukes.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpawnMushCloud {
    public static int amount = 50;

    @OnlyIn(Dist.CLIENT)
    public static void makePillar(ClientLevel level, double x,double y,double z,double vx,double vy,double vz) {
        for (double i=0;i<Math.PI*2;i+=Math.PI/24) {
            for (int j = 0; j < amount; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+60, z, i, 0, 0);
            }
        }
        for (double j = 0; j<100*((double) MushroomCloudParticle.VARIANT1_RESET_TICK /100); j+=2) {
            for (int i=0;i<14;i++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(),x,y,z,j,1,0);
            }
        }
    }
}
