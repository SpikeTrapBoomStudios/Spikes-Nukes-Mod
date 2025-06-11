package com.stbstudios.spikesnukes.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpawnMushCloud {
    public static int amountA1 = 40;
    public static int amountA2 = 18;
    public static int amountB1 = 95;
    public static int amountB2 = 7;

    @OnlyIn(Dist.CLIENT)
    public static void makeMushCloud(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        for (double i=0;i<Math.PI*2;i+=Math.PI/amountA2) {
            for (int j = 0; j < amountA1; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+60, z, i, 0, 0);
            }
        }
        for (double j = 0; j<amountB1*((double) MushroomCloudParticle.VARIANT1_RESET_TICK /100); j+=2) {
            for (int i=0;i<amountB2;i++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(),x,y,z,j,1,0);
            }
        }
    }
}
