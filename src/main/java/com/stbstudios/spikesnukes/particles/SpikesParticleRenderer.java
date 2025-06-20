package com.stbstudios.spikesnukes.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SpikesParticleRenderer {

    private static final float[][] CORNERS = {
            { -1f, -1f },
            { -1f,  1f },
            {  1f,  1f },
            {  1f, -1f }
    };
    private static final float[][] UVS = {
            { 0f, 0f },
            { 0f, 1f },
            { 1f, 1f },
            { 1f, 0f }
    };

    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        // camera position for subtraction
        float camX = (float) camera.getPosition().x;
        float camY = (float) camera.getPosition().y;
        float camZ = (float) camera.getPosition().z;
        // camera basis for billboarding
        Vector3f right = new Vector3f(camera.getLeftVector()).negate();
        Vector3f up    = camera.getUpVector();

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource =
                mc.renderBuffers().bufferSource();
        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityTranslucent(net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS)
        );

        for (SimpleParticle particle : SpikesParticleEngine.PARTICLES) {
            float x    = (float) particle.x;
            float y    = (float) particle.y;
            float z    = (float) particle.z;
            float halfSize = particle.size * 0.5f;

            // translate into camera-relative space
            poseStack.pushPose();
            poseStack.translate(x - camX, y - camY, z - camZ);

            Matrix4f modelMat = poseStack.last().pose();
            Matrix3f normalMat = poseStack.last().normal();
            // camera-facing normal for lighting
            Vector3f faceNormal = new Vector3f(right).cross(up).normalize();

            for (int i = 0; i < 4; i++) {
                float dx = CORNERS[i][0] * halfSize;
                float dy = CORNERS[i][1] * halfSize;

                float vx = right.x() * dx + up.x() * dy;
                float vy = right.y() * dx + up.y() * dy;
                float vz = right.z() * dx + up.z() * dy;

                consumer
                        .vertex(modelMat, vx, vy, vz)
                        .color(255, 128, 0, 255)
                        .uv(UVS[i][0], UVS[i][1])
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(0x00F000F0)
                        .normal(normalMat,
                                faceNormal.x(),
                                faceNormal.y(),
                                faceNormal.z())
                        .endVertex();
            }

            poseStack.popPose();
        }

        bufferSource.endBatch();
    }
}
