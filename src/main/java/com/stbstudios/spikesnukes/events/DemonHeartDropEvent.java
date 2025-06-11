package com.stbstudios.spikesnukes.events;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.explosives.nukes.NukeBase;
import com.stbstudios.spikesnukes.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DemonHeartDropEvent {
    public static int radius = 100;
    public static int power = 10000;

    @SubscribeEvent
    public static void whenHeartDropped(ItemTossEvent event) {
        ItemStack itemDropped = event.getEntity().getItem();
        Level level = event.getPlayer().level();
        Vec3 eyePos = event.getPlayer().getEyePosition();

        if (itemDropped.getItem() == ItemRegistry.DEMON_HEART.get()) {
            NukeBase newNuke = new NukeBase(level, eyePos, radius, power);
            newNuke.detonate();
            event.setCanceled(true);
            event.getPlayer().getInventory().add(itemDropped);
        }
    }
}
