package com.stbstudios.spikesnukes.mobeffects;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SpikesNukesMod.MOD_ID);

    public static final RegistryObject<MobEffect> RADIOACTIVE_EFFECT = EFFECTS.register("radioactive", RadioactiveEffect::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
