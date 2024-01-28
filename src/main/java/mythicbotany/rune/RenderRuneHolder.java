package mythicbotany.rune;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mythicbotany.register.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.moddingx.libx.render.ClientTickHandler;

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
            vec.cross(new Vector3f(0, 1, 0));
            vec.normalize();
            AxisAngle4f rotation = new AxisAngle4f((float) (floatProgress * Math.PI), vec);
            matrixStack.mulPose(new Quaternionf(rotation));
        }
        //noinspection deprecation
        double rot = (BuiltInRegistries.ITEM.getId(stack.getItem()) % 2 == 0 ? 0.04 : -0.04) * (ClientTickHandler.ticksInGame() + partialTicks + (7 * current.getX() + 31 * current.getZ() + (current.getZ() == 0 ? 87 : Math.floorMod(current.getX(), current.getZ()))));
        matrixStack.mulPose(Axis.YP.rotationDegrees(31 * current.getX() + 7 * current.getZ()));
        matrixStack.mulPose(Axis.XP.rotationDegrees((float) (10 * Math.sin(rot))));
        matrixStack.mulPose(Axis.ZP.rotationDegrees((float) (10 * Math.cos(rot))));
        matrixStack.mulPose(Axis.XP.rotationDegrees(90));
        float scale = stack.getItem() == ModItems.fimbultyrTablet ? 0.9f : 0.7f;
        matrixStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer, blockEntity.getLevel(), 0);
        matrixStack.popPose();
    }
}
