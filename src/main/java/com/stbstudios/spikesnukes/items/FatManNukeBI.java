package com.stbstudios.spikesnukes.items;

import com.stbstudios.spikesnukes.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;

public class FatManNukeBI extends BlockItem {
    public static final Properties PROPERTIES = new Properties()
            .stacksTo(1);

    public FatManNukeBI() {
        super(ModBlocks.FAT_MAN_NUKE.get(), PROPERTIES);
    }
}
