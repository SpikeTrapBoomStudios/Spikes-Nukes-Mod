package com.stbstudios.spikesnukes.events;

import com.ibm.icu.text.MessagePattern;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.networking.NetworkHandler;
import com.stbstudios.spikesnukes.networking.SmokeParticlePacket;
import com.stbstudios.spikesnukes.particles.ModParticles;
import com.stbstudios.spikesnukes.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DemonHeartDropEvent {

    private static final HashSet<BlockPos> blockKillQueue = new HashSet<>();
    private static final HashSet<Vec3> rayQueue = new HashSet<>();
    private static Level level = null;
    private static boolean doBlockKill = false;
    private static Vec3 eyePos = Vec3.ZERO;

    public static int rayCount = 150000;
    public static int radius = 175;
    public static float stretch = 0.8f;

    @SubscribeEvent
    public static void whenHeartDropped(ItemTossEvent event) {
        ItemStack itemDropped = event.getEntity().getItem();
        level = event.getPlayer().level();
        eyePos = event.getPlayer().getEyePosition();

        if (itemDropped.getItem() == ItemRegistry.DEMON_HEART.get()) {
            //CompletableFuture.runAsync(() -> {
                Queue<BlockPos> asyncBlockKillQueue = new LinkedList<>();

                final double goldenRatio = (1 + Math.sqrt(5)) / 2;
                final double numRays = (double) rayCount;

                Queue<Vec3> allRays = new LinkedList<>();
                for (int i=0;i<numRays;i++) {
                    double bias = 0.2;
                    double phi = Math.acos(1.0 - ((2*(i+0.5))/numRays));
                    double theta = (2*Math.PI*i)/goldenRatio;

                    double iX = Math.sin(phi) * Math.cos(theta);
                    double iY = Math.cos(phi);
                    double iZ = Math.sin(phi) * Math.sin(theta);

                    Vec3 iDir = new Vec3(iX, iY, iZ).normalize();

                    rayQueue.add(iDir);
                }

            event.setCanceled(true);
            event.getPlayer().getInventory().add(itemDropped);
        } else if (itemDropped.getItem() == Items.DIAMOND) {
            if (level instanceof ServerLevel) {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new SmokeParticlePacket(eyePos.x, eyePos.y, eyePos.z));
            }
            event.setCanceled(true);
            event.getPlayer().getInventory().add(itemDropped);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent tick) {
        if (tick.phase == TickEvent.Phase.START) {
            if (doBlockKill) {
                int c = 0;
                Iterator<BlockPos> iterator = blockKillQueue.iterator();
                if (blockKillQueue.isEmpty()) {
                    doBlockKill = false;
                    return;
                }
                while (iterator.hasNext() && c < 10000) {
                    BlockPos pos = iterator.next();
                    iterator.remove();  // Removes the current element from the set
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    c++;
                }
            } else {
                int c = 0;
                Iterator<Vec3> iterator = rayQueue.iterator();
                if (rayQueue.isEmpty()) {
                    doBlockKill = true;
                    return;
                }
                while (iterator.hasNext() && c < 25000) {
                    Vec3 thisRay = iterator.next();
                    iterator.remove();
                    float power = 2000.0f;
                    float range = (float) radius;
                    for (float j=0f;j<=range;j++) {
                        double nX = thisRay.x * j * 0.5;
                        double nZ = thisRay.z * j * 0.5;
                        double nY = thisRay.y * j * 0.5 * stretch;
                        Vec3 lookDirScaled = eyePos.add(new Vec3(nX, nY, nZ));

                        BlockPos thisBlockPos = new BlockPos((int)Math.floor(lookDirScaled.x),(int)Math.floor(lookDirScaled.y),(int)Math.floor(lookDirScaled.z));

                        BlockState thisBlockState = level.getBlockState(thisBlockPos);

                        if (thisBlockState.getExplosionResistance(null, BlockPos.ZERO, null)<=power && thisBlockState.getBlock()!=Blocks.AIR) {
                            blockKillQueue.add(thisBlockPos);
                        }

                        if (thisBlockState.getFluidState().is(FluidTags.WATER)) {
                            level.setBlock(thisBlockPos, Blocks.AIR.defaultBlockState(), 3);
                        }

                        if (thisBlockState.getFluidState().isEmpty()) {
                            power -= (float) thisBlockState.getExplosionResistance(null, BlockPos.ZERO, null)*4;
                        }

                        if (power<=0) {break;}
                    }
                    c++;
                }
            }
        }
    }
}
