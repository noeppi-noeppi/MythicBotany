package mythicbotany.mimir;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.render.block.RotatedBlockRenderer;
import org.moddingx.libx.util.lazy.LazyValue;
import vazkii.botania.common.item.BotaniaItems;

import javax.annotation.Nonnull;

public class RenderYggdrasilBranch extends RotatedBlockRenderer<TileYggdrasilBranch> {

    private final LazyValue<ItemStack> twig = new LazyValue<>(() -> new ItemStack(BotaniaItems.livingwoodTwig));

    @Override
    protected void doRender(@Nonnull TileYggdrasilBranch tile, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.9, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(160));
        poseStack.scale(0.8f, 0.8f, 0.8f);
        Minecraft.getInstance().getItemRenderer().renderStatic(this.twig.get(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, tile.getLevel(), 0);
        poseStack.popPose();
        ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.1, 0.25);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, tile.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
