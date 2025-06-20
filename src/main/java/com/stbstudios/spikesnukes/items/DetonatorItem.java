package com.stbstudios.spikesnukes.items;

import com.stbstudios.spikesnukes.blocks.FatManNukeBE;
import com.stbstudios.spikesnukes.blocks.ModBlocks;
import com.stbstudios.spikesnukes.blocks.ModBlocksEntity;
import com.stbstudios.spikesnukes.mechanics.radioactivity.RadioactiveWasteManager;
import com.stbstudios.spikesnukes.sounds.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DetonatorItem extends Item {
    public static final Properties PROPERTIES = new Properties()
            .stacksTo(2);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.isClientSide()) {
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(ModSounds.BUTTON_CLICK.get(), 2.0F, 1.0F)
            );
            return InteractionResultHolder.pass(itemStack);
        }

        CompoundTag tag = itemStack.getTag();
        if (tag==null) return InteractionResultHolder.pass(itemStack);
        if (tag.contains("bombX")) {
            BlockPos blockPos = new BlockPos(tag.getInt("bombX"),tag.getInt("bombY"),tag.getInt("bombZ"));
            if (level.getBlockState(blockPos).getBlock() == ModBlocks.FAT_MAN_NUKE.get()) {
                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                System.out.println(blockEntity);
                if (blockEntity instanceof FatManNukeBE) {
                    ((FatManNukeBE) blockEntity).explode();
                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();

        if (player == null) return InteractionResult.PASS;
        if (player.isCrouching()) {
            ItemStack itemStack = player.getItemInHand(useOnContext.getHand());
            itemStack.getOrCreateTag().putInt("bombX", useOnContext.getClickedPos().getX());
            itemStack.getOrCreateTag().putInt("bombY", useOnContext.getClickedPos().getY());
            itemStack.getOrCreateTag().putInt("bombZ", useOnContext.getClickedPos().getZ());
            if (useOnContext.getHand() == InteractionHand.OFF_HAND) {
                useOnContext.getLevel().setBlock(useOnContext.getClickedPos(), ModBlocks.RADIOACTIVE_WASTE.get().defaultBlockState(), 3);
                RadioactiveWasteManager.radioactiveWasteQueue.add(new RadioactiveWasteManager.WasteKey(useOnContext.getLevel().dimension(), useOnContext.getClickedPos().asLong(), 100));
            }
        }

        return InteractionResult.CONSUME;
    }


    public DetonatorItem() {
        super(PROPERTIES);
    }
}
