package com.stbstudios.spikesnukes.items;

import com.stbstudios.spikesnukes.networking.BasicSmokeExplosionPacket;
import com.stbstudios.spikesnukes.networking.NetworkHandler;
import com.stbstudios.spikesnukes.sounds.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.PacketDistributor;

public class LaserDetonatorItem extends Item {
    public static final Properties PROPERTIES = new Properties()
            .stacksTo(2);

    public LaserDetonatorItem() {
        super(PROPERTIES);
    }

    private BlockPos raycast(Vec3 originPos, Vec3 dir, Level level) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(0,0,0);
        for (float i=0;i<500;i+=.5f) {
            Vec3 scaledDir = new Vec3((int)Math.round(dir.x*i),(int)Math.round(dir.y*i),(int)Math.round(dir.z*i));
            int newX = (int) (originPos.x + scaledDir.x);
            int newY = (int) (originPos.y + scaledDir.y);
            int newZ = (int) (originPos.z + scaledDir.z);
            mutableBlockPos.set(new BlockPos(newX, newY, newZ));
            if (level.getBlockState(mutableBlockPos).getBlock() != Blocks.AIR) {
                return mutableBlockPos.immutable();
            }
        }
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (level.isClientSide()) {
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(ModSounds.BUTTON_CLICK.get(), 2.0F, 1.0F)
            );
            return InteractionResultHolder.pass(itemStack);
        } else {
            Vec3 lookAngle = player.getLookAngle();
            BlockPos rayHitPos = raycast(player.getEyePosition(), lookAngle, level);
            if (rayHitPos!=null) {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new BasicSmokeExplosionPacket(rayHitPos.getX(), rayHitPos.getY(), rayHitPos.getZ()));
            }
        }

        return InteractionResultHolder.success(itemStack);
    }
}
