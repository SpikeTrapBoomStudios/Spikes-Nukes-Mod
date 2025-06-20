package com.stbstudios.spikesnukes.events;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.mechanics.explosives.bases.NukeBase;
import com.stbstudios.spikesnukes.items.ModItems;
import com.stbstudios.spikesnukes.networking.NetworkHandler;
import com.stbstudios.spikesnukes.networking.SmokeParticlePacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DemonHeartDropEvent {
    public static float yieldKT = 60;

    @SubscribeEvent
    public static void whenHeartDropped(ItemTossEvent event) {
        ItemStack itemDropped = event.getEntity().getItem();
        Level level = event.getPlayer().level();
        Vec3 eyePos = event.getPlayer().getEyePosition();

        if (itemDropped.getItem() == ModItems.DEMON_HEART.get() && !level.isClientSide()) {
            NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                    new SmokeParticlePacket(eyePos.x, eyePos.y, eyePos.z));
            event.setCanceled(true);
            event.getPlayer().getInventory().add(itemDropped);
        }
    }
}
