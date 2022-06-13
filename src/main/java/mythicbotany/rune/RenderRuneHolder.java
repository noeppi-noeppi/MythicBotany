package mythicbotany.rune;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.noeppi_noeppi.libx.render.ClientTickHandler;
import mythicbotany.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class RenderRuneHolder implements BlockEntityRenderer<TileRuneHolder> {

    @Override
    public final void render(@Nonnull TileRuneHolder blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockPos current = blockEntity.getBlockPos();
        ItemStack stack = blockEntity.getInventory().getStackInSlot(0);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.25, 0.5);
        if (blockEntity.getTarget() != null && blockEntity.getFloatProgress() > 0 && !current.equals(blockEntity.getTarget())) {
            BlockPos target = blockEntity.getTarget();
            double floatProgress = blockEntity.getFloatProgress();
            matrixStack.translate((target.getX() - current.getX()) * floatProgress, Math.sin(floatProgress * Math.PI), (target.getZ() - current.getZ()) * floatProgress);
            Vector3f vec = new Vector3f(target.getX() - current.getX(), 0, target.getZ() - current.getZ());
            vec.cross(Vector3f.YN);
            vec.normalize();
            matrixStack.mulPose(vec.rotation((float) (floatProgress * Math.PI)));
        }
        //noinspection deprecation
        double rot = (Registry.ITEM.getId(stack.getItem()) % 2 == 0 ? 0.04 : -0.04) * (ClientTickHandler.ticksInGame + partialTicks + (7 * current.getX() + 31 * current.getZ() + (current.getZ() == 0 ? 87 : Math.floorMod(current.getX(), current.getZ()))));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(31 * current.getX() + 7 * current.getZ()));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((float) (10 * Math.sin(rot))));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) (10 * Math.cos(rot))));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        float scale = stack.getItem() == ModItems.fimbultyrTablet ? 0.9f : 0.7f;
        matrixStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, 0);
        matrixStack.popPose();
    }
}
