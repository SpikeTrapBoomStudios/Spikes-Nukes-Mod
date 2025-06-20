package com.stbstudios.spikesnukes.blocks;

import com.stbstudios.spikesnukes.mechanics.explosives.bases.NukeBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FatManNukeBE extends BlockEntity {
    public static float yieldKT = 10f;

    public FatManNukeBE(BlockPos blockPos, BlockState blockState) {
        super(ModBlocksEntity.FAT_MAN_NUKE_BE.get(), blockPos, blockState);
    }

    public void explode() {
        Level world = this.level;
        BlockPos pos = this.worldPosition;
        NukeBase thisNuke = new NukeBase(world, pos.getCenter(), yieldKT);
        thisNuke.detonate();
    }
}
