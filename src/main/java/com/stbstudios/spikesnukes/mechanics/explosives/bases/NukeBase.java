package com.stbstudios.spikesnukes.mechanics.explosives.bases;

import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.blocks.ModBlocks;
import com.stbstudios.spikesnukes.mechanics.radioactivity.RadioactiveWasteManager;
import com.stbstudios.spikesnukes.networking.BasicSmokeExplosionPacket;
import com.stbstudios.spikesnukes.networking.NetworkHandler;
import com.stbstudios.spikesnukes.networking.SmokeParticlePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class NukeBase {
    private static final float poleCluster = .9f;

    protected Level level;
    protected Vec3 explosionPos;
    protected float radius;
    protected float power;
    protected float yieldKT;

    private static final int MAX_RAYS_PER_TICK = 5000;
    private final HashMap<Long, HashSet<Long>> blockKillQueue = new HashMap<>();
    private final HashMap<Integer, HashSet<Long>> waterKillQueue = new HashMap<>();
    private final HashMap<Long, HashSet<Long>> radioactiveCapsQueue = new HashMap<>();
    private final Queue<Vec3> rayQueue = new ArrayDeque<>();

    private boolean doBlockKill = false;

    public NukeBase(Level level, Vec3 explosionPos, float yieldKT) {
        this.level = level;
        this.yieldKT = yieldKT;
        this.explosionPos = explosionPos;
        this.power = (int) NukeBase.getExplosionPowerFromYield(this.yieldKT);
    }

    public static float getExplosionRadiusFromYield(float yieldKT) {
        return (float) (30 * Math.sqrt(6 * yieldKT));
    }
    public static float getExplosionPowerFromYield(float yieldKT) {
        return (float) (150 * Math.sqrt(1.75 * yieldKT));
    }

    public void detonate() {
        this.radius = getExplosionRadiusFromYield(this.yieldKT);
        castRaySphere((int) (25.0 * this.radius * this.radius));
        spawnPfx();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void spawnPfx() {
        if (level instanceof ServerLevel) {
            if (yieldKT >= 1.0) {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new SmokeParticlePacket(explosionPos.x, explosionPos.y, explosionPos.z));
            } else {
                NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new BasicSmokeExplosionPacket(explosionPos.x, explosionPos.y, explosionPos.z));
            }
        }
    }

    public void castRaySphere(int rayCount) {
        final double offset = 2.0 / rayCount;
        final double increment = Math.PI * (3.0 - Math.sqrt(5)); //golden angle (rad)

        //Fibonacci Lattice Algorithm
        for (int i = 0; i < rayCount; i++) {
            double y = ((i * offset) - 1) + (offset / 2);
            y  = Math.signum(y) * Math.pow(Math.abs(y), poleCluster); //Artificial pole clustering because for some god-damn reason the distribution is inconsistent in the top/bottom
            double r = Math.sqrt(1 - y * y);

            double phi = i * increment;

            double x = Math.cos(phi) * r;
            double z = Math.sin(phi) * r;

            Vec3 direction = new Vec3(x, y, z).normalize();
            rayQueue.add(direction);
        }
    }

    public void shootRay (Vec3 rayDir, int x, int y, int z) {
        float maxDist = radius;
        Vec3 origin = explosionPos;

        int stepX = rayDir.x > 0 ? 1 : -1;
        int stepY = rayDir.y > 0 ? 1 : -1;
        int stepZ = rayDir.z > 0 ? 1 : -1;

        float tDeltaX = (float) Math.abs(1.0 / rayDir.x);
        float tDeltaY = (float) Math.abs(1.0 / rayDir.y);
        float tDeltaZ = (float) Math.abs(1.0 / rayDir.z);

        float tMaxX = (float) (((stepX > 0 ? (x + 1) - origin.x : origin.x - x)) * tDeltaX);
        float tMaxY = (float) (((stepY > 0 ? (y + 1) - origin.y : origin.y - y)) * tDeltaY);
        float tMaxZ = (float) (((stepZ > 0 ? (z + 1) - origin.z : origin.z - z)) * tDeltaZ);

        float t = 0;
        float rayPower = power;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, y, z);
        while (t < maxDist && rayPower > 0) {
            mutableBlockPos.set(x, y, z);
            long thisChunk = new ChunkPos(mutableBlockPos).toLong();
            BlockState state = level.getBlockState(mutableBlockPos);

            boolean doRemoveBlock = false;
            boolean doRemoveWater = false;
            float powerSubtraction = 0;
            //TODO: Create a water block queue that removes top->bottom
            Block block = state.getBlock();
            float explosionResistance = block.getExplosionResistance(state, level, mutableBlockPos, null);
            if (block != Blocks.AIR && state.getFluidState().isEmpty()) {
                powerSubtraction += explosionResistance;
            }
            if (explosionResistance <= rayPower && block != Blocks.AIR && state.getFluidState().isEmpty()) {
                doRemoveBlock = true;
            } else if (block == Blocks.AIR) {
                powerSubtraction += .1f;
            } else if (state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA)) {
                doRemoveWater = true;
                powerSubtraction += 1;
            }
            float oldT = t;

            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    x += stepX;
                    t = tMaxX;
                    tMaxX += tDeltaX;
                } else {
                    z += stepZ;
                    t = tMaxZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if (tMaxY < tMaxZ) {
                    y += stepY;
                    t = tMaxY;
                    tMaxY += tDeltaY;
                } else {
                    z += stepZ;
                    t = tMaxZ;
                    tMaxZ += tDeltaZ;
                }
            }

            rayPower -= powerSubtraction * (t - oldT);

            if (t>=maxDist || rayPower<=0) {
                mutableBlockPos.set(x, y, z);
                thisChunk = new ChunkPos(mutableBlockPos).toLong();
                state = level.getBlockState(mutableBlockPos);
                if (state.getBlock() != Blocks.AIR && state.getFluidState().isEmpty()) {
                    radioactiveCapsQueue.computeIfAbsent(thisChunk, v -> new HashSet<>()).add(mutableBlockPos.immutable().asLong());
                    return;
                }
            } else if (doRemoveBlock) {
                blockKillQueue.computeIfAbsent(thisChunk, v -> new HashSet<>()).add(mutableBlockPos.immutable().asLong());
            } else if (doRemoveWater) {
                waterKillQueue.computeIfAbsent(mutableBlockPos.getY(), v -> new HashSet<>()).add(mutableBlockPos.immutable().asLong());
            }
        }
    }

    private Long getNearestChunk(HashMap<Long, HashSet<Long>> chunkPosHashSetHashMap) {
        Long nearestChunk = null;
        double minDistSq = Double.MAX_VALUE;
        for (Long chunkLong : chunkPosHashSetHashMap.keySet()) {
            ChunkPos chunk = new ChunkPos(chunkLong);
            double centerX = (chunk.x << 4) + 8;
            double centerZ = (chunk.z << 4) + 8;
            double dx = centerX - explosionPos.x;
            double dz = centerZ - explosionPos.z;
            double distSq = dx * dx + dz * dz;
            if (distSq < minDistSq) {
                minDistSq = distSq;
                nearestChunk = chunkLong;
            }
        }
        return nearestChunk;
    }

    public void processBlockKillQueue() {
        Long nearestChunk = getNearestChunk(blockKillQueue);

        if (nearestChunk != null) {
            HashSet<Long> blockPositions = blockKillQueue.remove(nearestChunk);
            if (blockPositions != null) {
                for (Long posLong : blockPositions) {
                    BlockPos pos = BlockPos.of(posLong);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }
    public void processRadioactiveWasteQueue() {
        Long nearestChunk = getNearestChunk(radioactiveCapsQueue);

        if (nearestChunk != null) {
            HashSet<Long> blockPositions = radioactiveCapsQueue.remove(nearestChunk);
            if (blockPositions != null) {
                for (Long posLong : blockPositions) {
                    BlockPos pos = BlockPos.of(posLong);
                    level.setBlock(pos, ModBlocks.RADIOACTIVE_WASTE.get().defaultBlockState(), 2);
                    RadioactiveWasteManager.radioactiveWasteQueue.add(new RadioactiveWasteManager.WasteKey(level.dimension(), pos.asLong(), 8));
                }
            }
        }
    }
    public void processWaterKillQueue() {
        int highestYLevel = -69696969;
        for (int yLevel : waterKillQueue.keySet()) {
            if (yLevel > highestYLevel) {
                highestYLevel = yLevel;
            }
        }
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        if (highestYLevel != -69696969) {
            HashSet<Long> waterPositions = waterKillQueue.remove(highestYLevel);
            if (waterPositions != null) {
                for (Long posLong : waterPositions) {
                    BlockPos pos = BlockPos.of(posLong);
                    for (var x=pos.getX()-3;x<=pos.getX()+3;x++) {
                        for (var z=pos.getZ()-3;z<=pos.getZ()+3;z++) {
                            mutableBlockPos.set(x, highestYLevel, z);
                            FluidState fluidState = level.getBlockState(mutableBlockPos).getFluidState();
                            if (fluidState.is(FluidTags.WATER) || fluidState.is(FluidTags.LAVA)) {
                                level.setBlock(mutableBlockPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent tick) {
        if (tick.phase != TickEvent.Phase.START) {
            return;
        }
        if (doBlockKill) {
            if (!waterKillQueue.isEmpty()) {
                processWaterKillQueue();
            } else if (!blockKillQueue.isEmpty()) {
                processBlockKillQueue();
            } else if (!radioactiveCapsQueue.isEmpty()) {
                processRadioactiveWasteQueue();
            } else {
                System.out.println("Nuke Logic Complete");
                doBlockKill = false;
                MinecraftForge.EVENT_BUS.unregister(this);
            }
            return;
        }

        int processedRays = 0;
        while (processedRays < MAX_RAYS_PER_TICK && !rayQueue.isEmpty()) {
            Vec3 rayDir = rayQueue.poll();
            if (rayDir != null) {
                shootRay(rayDir, (int) Math.floor(explosionPos.x), (int) Math.floor(explosionPos.y), (int) Math.floor(explosionPos.z));
            }
            processedRays++;
        }

        if (rayQueue.isEmpty()) {
            System.out.println("All rays completed.");
            doBlockKill = true;
        }
    }
}
