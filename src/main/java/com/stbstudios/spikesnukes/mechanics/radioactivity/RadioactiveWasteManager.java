package com.stbstudios.spikesnukes.mechanics.radioactivity;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.blocks.ModBlocks;
import com.stbstudios.spikesnukes.math.Math2;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Iterator;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RadioactiveWasteManager {
    public record WasteKey(ResourceKey<Level> levelResourceKey, Long blockPos, int intensity) {};
    public static HashSet<WasteKey> radioactiveWasteActive = new HashSet<>();
    public static HashSet<WasteKey> radioactiveWasteQueue = new HashSet<>();

    public static HashSet<Block> ALLOWED_TO_WASTE = new HashSet<Block>();
    static {
        ALLOWED_TO_WASTE.add(Blocks.STONE);
        ALLOWED_TO_WASTE.add(Blocks.ANDESITE);
        ALLOWED_TO_WASTE.add(Blocks.GRASS_BLOCK);
        ALLOWED_TO_WASTE.add(Blocks.DEEPSLATE);
        ALLOWED_TO_WASTE.add(Blocks.GRANITE);
        ALLOWED_TO_WASTE.add(Blocks.DIORITE);
        ALLOWED_TO_WASTE.add(Blocks.DIRT_PATH);
        ALLOWED_TO_WASTE.add(Blocks.GRAVEL);
        ALLOWED_TO_WASTE.add(Blocks.FARMLAND);
        ALLOWED_TO_WASTE.add(Blocks.SAND);
    }

    private static void scanNeighbors(Level level, BlockPos blockPos, int intensity) {
        BlockState RADIOACTIVE_WASTE_STATE = ModBlocks.RADIOACTIVE_WASTE.get().defaultBlockState();
        BlockPos upstairsNeighbor = new BlockPos(blockPos.getX(),blockPos.getY()+1,blockPos.getZ()); //I hate the upstairs neighbors
        BlockPos downstairsNeighbor = new BlockPos(blockPos.getX(),blockPos.getY()-1,blockPos.getZ());
        boolean upstairsNeighborPresent = level.getBlockState(upstairsNeighbor).getBlock() != Blocks.AIR;
        boolean downstairsNeighborPresent = level.getBlockState(downstairsNeighbor).getBlock() != Blocks.AIR;
        boolean hasNeighbor = false;
        if (!upstairsNeighborPresent && !downstairsNeighborPresent) {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
        } else if (!upstairsNeighborPresent || !downstairsNeighborPresent) {
            for (var x=blockPos.getX()-1;x<=blockPos.getX()+1;x++) {
                for (var z=blockPos.getZ()-1;z<=blockPos.getZ()+1;z++) {
                    BlockPos neighborPos = new BlockPos(x, blockPos.getY(), z);
                    if (ALLOWED_TO_WASTE.contains(level.getBlockState(neighborPos).getBlock())) {
                        hasNeighbor = true;
                        level.setBlock(neighborPos, RADIOACTIVE_WASTE_STATE, 3);
                        WasteKey newWasteKey = new WasteKey(level.dimension(), neighborPos.asLong(), intensity - (int)Math.round(Math2.getRandomNumber(1,2)));
                        radioactiveWasteQueue.add(newWasteKey);
                    }
                }
            }
        }
        if (hasNeighbor && upstairsNeighborPresent) {
            if (ALLOWED_TO_WASTE.contains(level.getBlockState(upstairsNeighbor).getBlock())) {
                level.setBlock(upstairsNeighbor, RADIOACTIVE_WASTE_STATE, 3);
                WasteKey newWasteKey = new WasteKey(level.dimension(), upstairsNeighbor.asLong(), intensity - (int)Math.round(Math2.getRandomNumber(1,2)));
                radioactiveWasteQueue.add(newWasteKey);
            }
        }
        if (hasNeighbor && downstairsNeighborPresent) {
            if (ALLOWED_TO_WASTE.contains(level.getBlockState(downstairsNeighbor).getBlock())) {
                level.setBlock(downstairsNeighbor, RADIOACTIVE_WASTE_STATE, 3);
                WasteKey newWasteKey = new WasteKey(level.dimension(), downstairsNeighbor.asLong(), intensity - (int)Math.round(Math2.getRandomNumber(1,2)));
                radioactiveWasteQueue.add(newWasteKey);
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent tickEvent) {
        if (tickEvent.phase != TickEvent.Phase.START) return;
        radioactiveWasteActive.addAll(radioactiveWasteQueue);
        radioactiveWasteQueue.clear();
        MinecraftServer server = tickEvent.getServer();

        Iterator<WasteKey> wasteIterator = radioactiveWasteActive.iterator();
        int c = 0;
        while (wasteIterator.hasNext() && c<200) {
            WasteKey wasteKey = wasteIterator.next();
            Level level = server.getLevel(wasteKey.levelResourceKey);
            if (level != null && wasteKey.intensity > 0) {
                BlockPos blockPos = BlockPos.of(wasteKey.blockPos);
                if (level.getBlockState(blockPos).getBlock() == ModBlocks.RADIOACTIVE_WASTE.get()) {
                    scanNeighbors(level, blockPos, wasteKey.intensity);
                }
            }
            wasteIterator.remove();
            c++;
        }
    }
}
