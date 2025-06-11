package com.stbstudios.spikesnukes.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.events.DemonHeartDropEvent;
import com.stbstudios.spikesnukes.explosives.nukes.NukeBase;
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
        event.getDispatcher().register(Commands.literal("setradius")
                .then(Commands.argument("r", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            DemonHeartDropEvent.radius = IntegerArgumentType.getInteger(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setpower")
                .then(Commands.argument("r", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            DemonHeartDropEvent.power = IntegerArgumentType.getInteger(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setgengap")
                .then(Commands.argument("r", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            NukeBase.generationRadiusGap = IntegerArgumentType.getInteger(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setangleoffset")
                .then(Commands.argument("r", DoubleArgumentType.doubleArg())
                        .executes(commandContext -> {

                            NukeBase.angleOffsetConst = DoubleArgumentType.getDouble(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setstretch")
                .then(Commands.argument("r", DoubleArgumentType.doubleArg())
                        .executes(commandContext -> {

                            NukeBase.stretch = (float) DoubleArgumentType.getDouble(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setpolecluster")
                .then(Commands.argument("r", DoubleArgumentType.doubleArg())
                        .executes(commandContext -> {

                            NukeBase.poleCluster = (float) DoubleArgumentType.getDouble(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setpolesafety")
                .then(Commands.argument("r", DoubleArgumentType.doubleArg())
                        .executes(commandContext -> {

                            NukeBase.poleSafetyConst = DoubleArgumentType.getDouble(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("setraycount")
                .then(Commands.argument("r", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            NukeBase.rayCountOverride = IntegerArgumentType.getInteger(commandContext,"r");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("pma1")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            SpawnMushCloud.amountA1 = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("pma2")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            SpawnMushCloud.amountA2 = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("pmb1")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            SpawnMushCloud.amountB1 = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("pmb2")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            SpawnMushCloud.amountB2 = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
        event.getDispatcher().register(Commands.literal("rt")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(commandContext -> {

                            MushroomCloudParticle.VARIANT1_RESET_TICK = IntegerArgumentType.getInteger(commandContext,"amount");

                            return 1;
                        })
                )
        );
    }
}
