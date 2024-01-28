package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import vazkii.botania.client.core.handler.ClientTickHandler;

import javax.annotation.Nonnull;

public class RenderReturnPortal implements BlockEntityRenderer<TileReturnPortal> {

    @Override
    public void render(@Nonnull TileReturnPortal blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        float alpha = (float) Math.min(1, (Math.sin(((double) ((float) ClientTickHandler.ticksInGame + partialTicks) / 8d) + 1) / 7d) + 0.7);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("botania", "block/alfheim_portal_swirl"));
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.translate(0, 0, -0.5);
        poseStack.scale(1/3f, 1/3f, 1/3f);
        poseStack.translate(0, 0, -0.5);
        this.renderPortal(poseStack, buffer, sprite, 0, 0, 3, 3, alpha, combinedOverlay);
        poseStack.translate(3, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        this.renderPortal(poseStack, buffer, sprite, 0, 0, 3, 3, alpha, combinedOverlay);
        poseStack.popPose();
    }
    
    @SuppressWarnings("SameParameterValue")
    private void renderPortal(PoseStack poseStack, MultiBufferSource buffer, TextureAtlasSprite sprite, int x, int y, int width, int height, float alpha, int overlay) {
        VertexConsumer vertex = buffer.getBuffer(Sheets.translucentItemSheet());
        Matrix4f model = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        vertex.vertex(model, (float) x, (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU0(), sprite.getV1()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) (x + width), (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU1(), sprite.getV1()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) (x + width), (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU1(), sprite.getV0()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.vertex(model, (float) x, (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(sprite.getU0(), sprite.getV0()).overlayCoords(overlay).uv2(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
    }
}
