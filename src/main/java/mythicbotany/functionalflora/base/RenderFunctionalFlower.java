package mythicbotany.functionalflora.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import vazkii.botania.client.render.tile.RenderTileFloatingFlower;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderFunctionalFlower<T extends FunctionalFlowerBase> implements BlockEntityRenderer<T> {
    
    @Override
    public void render(@Nonnull T blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (blockEntity.isFloating() && !BotaniaConfig.client().staticFloaters()) {
            RenderTileFloatingFlower.renderFloatingIsland(blockEntity, partialTicks, poseStack, buffer, light, overlay);
        }
        if (Minecraft.getInstance().cameraEntity instanceof LivingEntity view) {
            if (ItemMonocle.hasMonocle(view)) {
                HitResult ray = Minecraft.getInstance().hitResult;
                if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) ray).getBlockPos();
                    if (blockEntity.getBlockPos().equals(pos) || hasBindingAttempt(view, blockEntity.getBlockPos())) {
                        poseStack.pushPose();
                        if (hasBindingAttempt(view, blockEntity.getBlockPos())) {
                            poseStack.translate(0, 0.005, 0);
                        }
                        RenderTileSpecialFlower.renderRadius(blockEntity, poseStack, buffer, blockEntity.getRadius());
                        poseStack.translate(0, 0.002, 0);
                        RenderTileSpecialFlower.renderRadius(blockEntity, poseStack, buffer, blockEntity.getSecondaryRadius());
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    public static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
        ItemStack stackHeld = PlayerHelper.getFirstHeldItem(view, ModItems.twigWand);
        if (!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
            Optional<BlockPos> coords = ItemTwigWand.getBindingAttempt(stackHeld);
            return coords.isPresent() && coords.get().equals(tilePos);
        } else {
            return false;
        }
    }
}
