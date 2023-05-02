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
import vazkii.botania.client.render.block_entity.FloatingFlowerBlockEntityRenderer;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderFunctionalFlower<T extends FunctionalFlowerBase> implements BlockEntityRenderer<T> {
    
    @Override
    public void render(@Nonnull T blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        if (blockEntity.isFloating() && !BotaniaConfig.client().staticFloaters()) {
            FloatingFlowerBlockEntityRenderer.renderFloatingIsland(blockEntity, partialTicks, poseStack, buffer, overlay);
        }
        if (Minecraft.getInstance().cameraEntity instanceof LivingEntity view) {
            if (ManaseerMonocleItem.hasMonocle(view)) {
                HitResult ray = Minecraft.getInstance().hitResult;
                if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) ray).getBlockPos();
                    if (blockEntity.getBlockPos().equals(pos) || hasBindingAttempt(view, blockEntity.getBlockPos())) {
                        poseStack.pushPose();
                        if (hasBindingAttempt(view, blockEntity.getBlockPos())) {
                            poseStack.translate(0, 0.005, 0);
                        }
                        SpecialFlowerBlockEntityRenderer.renderRadius(blockEntity, poseStack, buffer, blockEntity.getRadius());
                        poseStack.translate(0, 0.002, 0);
                        SpecialFlowerBlockEntityRenderer.renderRadius(blockEntity, poseStack, buffer, blockEntity.getSecondaryRadius());
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    public static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
        ItemStack stackHeld = PlayerHelper.getFirstHeldItem(view, BotaniaItems.twigWand);
        if (!stackHeld.isEmpty() && WandOfTheForestItem.getBindMode(stackHeld)) {
            Optional<BlockPos> coords = WandOfTheForestItem.getBindingAttempt(stackHeld);
            return coords.isPresent() && coords.get().equals(tilePos);
        } else {
            return false;
        }
    }
}
