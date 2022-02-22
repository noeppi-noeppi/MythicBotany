package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousModels;

import javax.annotation.Nonnull;

public class RenderReturnPortal implements BlockEntityRenderer<TileReturnPortal> {

    @Override
    public void render(@Nonnull TileReturnPortal blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        float alpha = (float) Math.min(1, (Math.sin(((double) ((float) ClientTickHandler.ticksInGame + partialTicks) / 8d) + 1) / 7d) + 0.7);
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        matrixStack.translate(0, 0, -0.5);
        matrixStack.scale(1/3f, 1/3f, 1/3f);
        matrixStack.translate(0, 0, -0.5);
        this.renderPortal(matrixStack, buffer, MiscellaneousModels.INSTANCE.alfPortalTex.sprite(), 0, 0, 3, 3, alpha, combinedOverlay);
        matrixStack.translate(3, 0, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        this.renderPortal(matrixStack, buffer, MiscellaneousModels.INSTANCE.alfPortalTex.sprite(), 0, 0, 3, 3, alpha, combinedOverlay);
        matrixStack.popPose();
    }
    
    @SuppressWarnings("SameParameterValue")
    private void renderPortal(PoseStack poseStack, MultiBufferSource buffer, TextureAtlasSprite sprite, int x, int y, int width, int height, float alpha, int overlay) {
        VertexConsumer vertex = buffer.getBuffer(Sheets.translucentCullBlockSheet());
        Matrix4f model = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        vertex.vertex(model, (float) x, (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) (x + width), (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) (x + width), (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) x, (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
    }
}
