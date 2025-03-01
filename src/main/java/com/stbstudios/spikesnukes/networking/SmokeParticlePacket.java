package com.stbstudios.spikesnukes.networking;

import com.stbstudios.spikesnukes.particles.ModParticles;
import com.stbstudios.spikesnukes.particles.SpawnMushCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SmokeParticlePacket {
    private final double x,y,z;

    public SmokeParticlePacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(SmokeParticlePacket packet, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(packet.x);
        byteBuf.writeDouble(packet.y);
        byteBuf.writeDouble(packet.z);
    }

    public static SmokeParticlePacket decode(FriendlyByteBuf byteBuf) {
        return new SmokeParticlePacket(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
    }

    public static void handle(SmokeParticlePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        System.out.println("client received packet");
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                SpawnMushCloud.makePillar(minecraft.level,packet.x,packet.y,packet.z,0.0,0.0,0.0);
            }
        });
        context.setPacketHandled(true);
    }
}
