package mythicbotany.rune;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mythicbotany.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

public class RenderRuneHolder extends TileEntityRenderer<TileRuneHolder> {

    public RenderRuneHolder(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public final void render(@Nonnull TileRuneHolder tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        BlockPos current = tile.getPos();
        ItemStack stack = tile.getInventory().getStackInSlot(0);
        matrixStack.push();
        matrixStack.translate(0.5, 0.25, 0.5);
        if (tile.getTarget() != null && tile.getFloatProgress() > 0 && !current.equals(tile.getTarget())) {
            BlockPos target = tile.getTarget();
            double floatProgress = tile.getFloatProgress();
            matrixStack.translate((target.getX() - current.getX()) * floatProgress, Math.sin(floatProgress * Math.PI), (target.getZ() - current.getZ()) * floatProgress);
            Vector3f vec = new Vector3f(target.getX() - current.getX(), 0, target.getZ() - current.getZ());
            vec.cross(Vector3f.YN);
            vec.normalize();
            matrixStack.rotate(vec.rotation((float) (floatProgress * Math.PI)));
        }
        //noinspection deprecation
        double rot = (Registry.ITEM.getId(stack.getItem()) % 2 == 0 ? 0.04 : -0.04) * (ClientTickHandler.ticksInGame + partialTicks + (7 * current.getX() + 31 * current.getZ() + (current.getZ() == 0 ? 87 : Math.floorMod(current.getX(), current.getZ()))));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(31 * current.getX() + 7 * current.getZ()));
        matrixStack.rotate(Vector3f.XP.rotationDegrees((float) (10 * Math.sin(rot))));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (10 * Math.cos(rot))));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
        float scale = stack.getItem() == ModItems.fimbultyrTablet ? 0.9f : 0.7f;
        matrixStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
    }
}
