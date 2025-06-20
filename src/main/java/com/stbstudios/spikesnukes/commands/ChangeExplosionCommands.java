package com.stbstudios.spikesnukes.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.blocks.FatManNukeBE;
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

                            FatManNukeBE.yieldKT = (float) DoubleArgumentType.getDouble(commandContext,"kt");

                            return 1;
                        })
                )
        );
    }
}
