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
        event.getDispatcher().register(Commands.literal("setktyield")
                .then(Commands.argument("kt", DoubleArgumentType.doubleArg())
                        .executes(commandContext -> {

                            DemonHeartDropEvent.yieldKT = DoubleArgumentType.getDouble(commandContext,"kt");

                            return 1;
                        })
                )
        );
    }
}
