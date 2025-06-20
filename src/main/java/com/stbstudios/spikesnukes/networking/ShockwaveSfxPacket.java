package com.stbstudios.spikesnukes.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShockwaveSfxPacket {
    public ShockwaveSfxPacket() {

    }

    public static void encode(com.stbstudios.spikesnukes.networking.ShockwaveSfxPacket packet, FriendlyByteBuf byteBuf) {

    }

    public static com.stbstudios.spikesnukes.networking.ShockwaveSfxPacket decode(FriendlyByteBuf byteBuf) {
        return new com.stbstudios.spikesnukes.networking.ShockwaveSfxPacket();
    }

    public static void handle(com.stbstudios.spikesnukes.networking.ShockwaveSfxPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        context.setPacketHandled(true);
    }
}
