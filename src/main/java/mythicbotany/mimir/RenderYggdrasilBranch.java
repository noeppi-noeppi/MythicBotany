package mythicbotany.mimir;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.block.tesr.HorizontalRotatedTesr;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class RenderYggdrasilBranch extends HorizontalRotatedTesr<TileYggdrasilBranch> {

    private final LazyValue<ItemStack> twig = new LazyValue<>(() -> new ItemStack(ModItems.livingwoodTwig));
    
    public RenderYggdrasilBranch(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    protected void doRender(@Nonnull TileYggdrasilBranch tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        matrixStack.push();
        matrixStack.translate(0.5, 0.9, 0.5);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(160));
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        Minecraft.getInstance().getItemRenderer().renderItem(twig.getValue(), ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
        ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5, 0.1, 0.25);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
            matrixStack.pop();
        }
    }
}
