package com.stbstudios.spikesnukes.explosives.vfx;

import com.stbstudios.spikesnukes.particles.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpawnMushCloud {
    public static int amountA1 = 40;
    public static int amountA2 = 20;
    public static int amountB2 = 8;

    @OnlyIn(Dist.CLIENT)
    public static void makeMushCloud(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        int c = 0;
        for (double i=0;i<Math.PI*2;i+=Math.PI/(amountA2)) {
            for (int j = 0; j < amountA1; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+250, z, i, 0, 0);
                c++;
            }
        }
        for (double i=0;i<Math.PI*2;i+=Math.PI/(amountA2)) {
            for (int j = 0; j < amountA1; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+250, z, i, 3, 0);
                c++;
            }
        }
        for (double i=0;i<Math.PI*2;i+=Math.PI/(amountA2)) {
            for (int j = 0; j < amountA1; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+250, z, i, 4, 0);
                c++;
            }
        }
        for (double i=0;i<Math.PI*2;i+=Math.PI/5) {
            for (int j = 0; j < 20; j++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(), x, y+135, z, i, 1, 0);
                c++;
            }
        }
        for (double j = 0; j<380; j+=10) {
            for (int i=0;i<amountB2;i++) {
                level.addParticle(ModParticles.MUSHROOM_CLOUD.get(),x,y - 80,z,Math.abs(380-j),2,0);
                c++;
            }
        }
        FlashBangScreenEffect.flash();
    }
}
