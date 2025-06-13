package com.stbstudios.spikesnukes;

import com.stbstudios.spikesnukes.blocks.ModBlocks;
import com.stbstudios.spikesnukes.blocks.ModBlocksEntity;
import com.stbstudios.spikesnukes.networking.NetworkHandler;
import com.stbstudios.spikesnukes.particles.ModParticles;
import com.stbstudios.spikesnukes.registry.ItemRegistry;
import com.stbstudios.spikesnukes.sounds.ModSounds;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpikesNukesMod.MOD_ID)
public class SpikesNukesMod {
    public static final String MOD_ID = "spikesnukes";

    public SpikesNukesMod() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.ITEMS.register(bus);
        ModParticles.PARTICLES.register(bus);
        ModSounds.register(bus);
        ModBlocks.register(bus);
        ModBlocksEntity.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    //CLIENT INIT
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(NetworkHandler::register);
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            ModParticles.registerFactories(event);
        }
    }
}