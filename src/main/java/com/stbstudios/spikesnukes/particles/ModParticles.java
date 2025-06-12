package com.stbstudios.spikesnukes.particles;

import com.mojang.datafixers.kinds.IdF;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "spikesnukes");

    public static final RegistryObject<SimpleParticleType> MUSHROOM_CLOUD = PARTICLES.register("mushroom_cloud", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BASIC_EXPLOSION_PARTICLE = PARTICLES.register("basic_explosion_particle", () -> new SimpleParticleType(true));

    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MUSHROOM_CLOUD.get(), MushroomCloudParticle.Provider::new);
        event.registerSpriteSet(BASIC_EXPLOSION_PARTICLE.get(), BasicExplosionParticle.Provider::new);
    }
}
