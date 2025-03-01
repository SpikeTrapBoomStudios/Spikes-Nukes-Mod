package com.stbstudios.spikesnukes.networking;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import net.minecraft.client.particle.SmokeParticle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final String PROTOCOL_VER = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SpikesNukesMod.MOD_ID,"main"),
            () -> PROTOCOL_VER,
            PROTOCOL_VER::equals,
            PROTOCOL_VER::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, SmokeParticlePacket.class, SmokeParticlePacket::encode, SmokeParticlePacket::decode, SmokeParticlePacket::handle);
    }
}
