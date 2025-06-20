package com.stbstudios.spikesnukes.items;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpikesNukesMod.MOD_ID);

    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart", DemonHeartItem::new);
    public static final RegistryObject<Item> DETONATOR = ITEMS.register("detonator", DetonatorItem::new);
    public static final RegistryObject<Item> FAT_MAN_BLOCK_ITEM = ITEMS.register("fat_man_block", FatManNukeBI::new);
    public static final RegistryObject<Item> RADIOACTIVE_WASTE_ITEM = ITEMS.register("radioactive_waste", RadioactiveWasteBI::new);
    public static final RegistryObject<Item> LASER_DETONATOR = ITEMS.register("laser_detonator", LaserDetonatorItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
