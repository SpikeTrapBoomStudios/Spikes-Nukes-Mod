package com.stbstudios.spikesnukes.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class RadioactiveWaste extends Block {
    public static final BlockBehaviour.Properties PROPERTIES = BlockBehaviour.Properties
            .copy(Blocks.STONE);

    public RadioactiveWaste() {
        super(PROPERTIES);
    }
}
