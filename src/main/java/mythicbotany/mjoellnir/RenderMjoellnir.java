package mythicbotany.mjoellnir;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.libx.annotation.model.Model;
import mythicbotany.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderMjoellnir implements BlockEntityRenderer<TileMjoellnir> {

    @Model("block/mjoellnir")
    public static BakedModel model = null;

    @Override
    public void render(@Nonnull TileMjoellnir blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        renderHammer(blockEntity.getStack(), blockEntity.getBlockState(), matrixStack, buffer, combinedLight);
    }
    
    public static void renderHammer(@Nullable ItemStack stack, @Nullable BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        if (stack != null && stack.getItem() == ModBlocks.mjoellnir.asItem()) {
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(135));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
        } else {
            // Should not happen, just render it without the glint
            PoseStack.Pose matrix = poseStack.last();
            VertexConsumer vertex = buffer.getBuffer(RenderType.cutout());
            //noinspection deprecation
            Minecraft.getInstance().getBlockRenderer().getModelRenderer()
                .renderModel(matrix, vertex, state == null ? ModBlocks.mjoellnir.defaultBlockState() : state,
                        model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
    }
}
