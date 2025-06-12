package com.stbstudios.spikesnukes.networking;

import com.stbstudios.spikesnukes.particles.SpawnExplosionCloud;
import com.stbstudios.spikesnukes.particles.SpawnMushCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BasicSmokeExplosionPacket {
    private final double x,y,z;

    public BasicSmokeExplosionPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(BasicSmokeExplosionPacket packet, FriendlyByteBuf byteBuf) {
        byteBuf.writeDouble(packet.x);
        byteBuf.writeDouble(packet.y);
        byteBuf.writeDouble(packet.z);
    }

    public static BasicSmokeExplosionPacket decode(FriendlyByteBuf byteBuf) {
        return new BasicSmokeExplosionPacket(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
    }

    public static void handle(BasicSmokeExplosionPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                SpawnExplosionCloud.makeCloud(minecraft.level,packet.x,packet.y,packet.z,0.0,0.0,0.0);
            }
        });
        context.setPacketHandled(true);
    }
}
