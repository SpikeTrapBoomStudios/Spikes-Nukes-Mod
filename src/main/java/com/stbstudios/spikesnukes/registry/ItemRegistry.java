package com.stbstudios.spikesnukes.registry;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.items.DemonHeartItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpikesNukesMod.MOD_ID);

    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart", DemonHeartItem::new);
}
