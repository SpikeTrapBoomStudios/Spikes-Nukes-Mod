package com.stbstudios.spikesnukes.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.events.DemonHeartDropEvent;
import com.stbstudios.spikesnukes.particles.MushroomCloudParticle;
import com.stbstudios.spikesnukes.particles.SpawnMushCloud;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChangeExplosionCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("setraycount")
                .then(Commands.argument("count", IntegerArgumentType.integer(50000,1000000))
                        .executes(commandContext -> {
                            int rayCount = IntegerArgumentType.getInteger(commandContext,"count");

                            DemonHeartDropEvent.rayCount = rayCount;

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setradius")
                .then(Commands.argument("r", IntegerArgumentType.integer(10,1000))
                        .executes(commandContext -> {
                            int r = IntegerArgumentType.getInteger(commandContext,"r");

                            DemonHeartDropEvent.radius = r;

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setstretch")
                .then(Commands.argument("stretch", FloatArgumentType.floatArg())
                        .executes(commandContext -> {
                            float
                                    stretch = FloatArgumentType.getFloat(commandContext,"stretch");

                            DemonHeartDropEvent.stretch = stretch;

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("pm")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            SpawnMushCloud.amount = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("rt")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            MushroomCloudParticle.resetTick = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
    }
}
