package com.stbstudios.spikesnukes.mechanics.explosives.vfx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stbstudios.spikesnukes.SpikesNukesMod;
import com.stbstudios.spikesnukes.math.Math2;
import com.stbstudios.spikesnukes.sounds.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SpikesNukesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FlashBangScreenEffect {
    public static final ResourceLocation HUD_FLASH_TEX = new ResourceLocation(SpikesNukesMod.MOD_ID, "textures/hud/flashbang.png");

    private static final double FLASH_LIFETIME = 4.;

    private static long lastTime = System.currentTimeMillis();
    private static double flashTimer = 0;

    public static void flash() {
        if (Minecraft.getInstance().player != null) {
            Vec3 playerPos = Minecraft.getInstance().player.position();
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(ModSounds.NUKE_EXPLOSION.get(), 1.0F, 1.0F)
                );
            }
        }
        flashTimer = FLASH_LIFETIME;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        long currentTime = System.currentTimeMillis();
        float delta = (currentTime - lastTime) / 1000f;
        lastTime = currentTime;

        flashTimer = Math.max(0, flashTimer - delta);

        float deathProgress = (float) Math2.lerpWithEaseIn(0f, 1f,(flashTimer / FLASH_LIFETIME));

        Minecraft mc = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f,1f,1f,deathProgress);

        if (flashTimer > 0) {
            guiGraphics.blit(HUD_FLASH_TEX, 0, 0, 0, 0, 1920, 1080, screenWidth, screenHeight);
        }

        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.disableBlend();
    }
}
