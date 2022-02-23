package mythicbotany.mimir;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.block.RotatedBlockRenderer;
import io.github.noeppi_noeppi.libx.util.LazyValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class RenderYggdrasilBranch extends RotatedBlockRenderer<TileYggdrasilBranch> {

    private final LazyValue<ItemStack> twig = new LazyValue<>(() -> new ItemStack(ModItems.livingwoodTwig));

    @Override
    protected void doRender(@Nonnull TileYggdrasilBranch tile, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.9, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(160));
        poseStack.scale(0.8f, 0.8f, 0.8f);
        Minecraft.getInstance().getItemRenderer().renderStatic(this.twig.get(), ItemTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
        poseStack.popPose();
        ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.1, 0.25);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
            poseStack.popPose();
        }
    }
}
