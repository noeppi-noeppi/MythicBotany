package mythicbotany.mjoellnir;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.noeppi_noeppi.libx.annotation.Model;
import mythicbotany.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderMjoellnir extends TileEntityRenderer<TileMjoellnir> {

    @Model("block/mjoellnir")
    public static IBakedModel model = null;
    
    public RenderMjoellnir(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull TileMjoellnir tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        renderHammer(tile.getStack(), tile.getBlockState(), matrixStack, buffer, light);
    }
    
    public static void renderHammer(@Nullable ItemStack stack, @Nullable BlockState state, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        matrixStack.push();
        if (stack != null && stack.getItem() == ModBlocks.mjoellnir.asItem()) {
            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(135));
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        } else {
            // Should not happen, just render it without the glint
            MatrixStack.Entry matrix = matrixStack.getLast();
            IVertexBuilder vertex = buffer.getBuffer(RenderType.getCutout());
            //noinspection deprecation
            Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
                .renderModelBrightnessColor(matrix, vertex, state == null ? ModBlocks.mjoellnir.getDefaultState() : state,
                        model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();
    }
}
