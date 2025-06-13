package com.stbstudios.spikesnukes.blocks;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocksEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SpikesNukesMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<FatManNukeBE>> FAT_MAN_NUKE_BE =
            BLOCK_ENTITIES.register("fat_man_block",
                    () -> BlockEntityType.Builder.of(
                            FatManNukeBE::new,
                            ModBlocks.FAT_MAN_NUKE.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
