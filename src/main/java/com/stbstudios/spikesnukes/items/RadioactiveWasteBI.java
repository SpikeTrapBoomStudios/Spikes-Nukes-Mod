package com.stbstudios.spikesnukes.items;

import com.stbstudios.spikesnukes.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;

public class RadioactiveWasteBI extends BlockItem {
    public static final Properties PROPERTIES = new Properties()
            .stacksTo(64);

    public RadioactiveWasteBI() {
        super(ModBlocks.RADIOACTIVE_WASTE.get(), PROPERTIES);
    }
}
