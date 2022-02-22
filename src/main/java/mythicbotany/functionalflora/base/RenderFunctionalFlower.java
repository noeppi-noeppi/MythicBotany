package mythicbotany.functionalflora.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraftforge.client.model.data.IModelData;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RenderFunctionalFlower<T extends FunctionalFlowerBase> implements BlockEntityRenderer<T> {
    
    @Override
    public void render(@Nonnull T blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity.isFloating() && !BotaniaConfig.client().staticFloaters()) {
            IModelData data = blockEntity.getModelData();
            matrixStack.pushPose();
            double worldTime = (float) ClientTickHandler.ticksInGame + partialTicks;
            if (blockEntity.getLevel() != null) {
                worldTime += (new Random(blockEntity.getBlockPos().hashCode())).nextInt(1000);
            }

            matrixStack.translate(0.5D, 0.0D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-((float) worldTime * 0.5F)));
            matrixStack.translate(-0.5D, (float) Math.sin(worldTime * 0.05000000074505806D) * 0.1F, 0.5D);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(4.0F * (float) Math.sin(worldTime * 0.03999999910593033D)));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
            BlockState state = blockEntity.getBlockState();
            BakedModel ibakedmodel = brd.getBlockModel(state);
            brd.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, ibakedmodel, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay, data);
            matrixStack.popPose();
        }
        if (Minecraft.getInstance().cameraEntity instanceof LivingEntity view) {
            if (ItemMonocle.hasMonocle(view)) {
                HitResult ray = Minecraft.getInstance().hitResult;
                if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) ray).getBlockPos();
                    if (blockEntity.getBlockPos().equals(pos) || hasBindingAttempt(view, blockEntity.getBlockPos())) {
                        RadiusDescriptor descriptor = blockEntity.getRadius();
                        if (descriptor != null) {
                            matrixStack.pushPose();
                            matrixStack.translate(-blockEntity.getBlockPos().getX(), -blockEntity.getBlockPos().getY(), -blockEntity.getBlockPos().getZ());
                            if (descriptor.isCircle()) {
                                renderCircle(matrixStack, buffer, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
                            } else {
                                RenderTileSpecialFlower.renderRectangle(matrixStack, buffer, descriptor.getAABB(), true, null, (byte) 32);
                            }
                            matrixStack.popPose();
                        }
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

    public static void renderCircle(PoseStack poseStack, MultiBufferSource buffer, BlockPos center, double radius) {
        poseStack.pushPose();
        double x = (double) center.getX() + 0.5D;
        double y = center.getY();
        double z = (double) center.getZ() + 0.5D;
        poseStack.translate(x, y, z);
        int color = Mth.hsvToRgb((float) (ClientTickHandler.ticksInGame % 200) / 200.0F, 0.6F, 1.0F);
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        int alpha = 32;
        float f = 0.0625F;
        int totalAngles = 360;
        int drawAngles = 360;
        int step = totalAngles / drawAngles;
        radius -= f;
        VertexConsumer vertexBuffer = buffer.getBuffer(RenderHelper.CIRCLE);
        Matrix4f mat = poseStack.last().pose();
        Runnable centerFunc = () -> vertexBuffer.vertex(mat, 0.0F, f, 0.0F).color(r, g, b, alpha).endVertex();
        List<Runnable> vertexFuncs = new ArrayList<>();

        for (int i = 0; i < totalAngles + 1; i += step) {
            double rad = (double) (totalAngles - i) * 3.141592653589793D / 180.0D;
            float xp = (float) (Math.cos(rad) * radius);
            float zp = (float) (Math.sin(rad) * radius);
            vertexFuncs.add(() -> vertexBuffer.vertex(mat, xp, f, zp).color(r, g, b, alpha).endVertex());
        }

        RenderHelper.triangleFan(centerFunc, vertexFuncs);
        radius += f;
        float f1 = f + f / 4.0F;
        int alpha2 = 64;
        centerFunc = () -> vertexBuffer.vertex(mat, 0.0F, f1, 0.0F).color(r, g, b, alpha2).endVertex();
        vertexFuncs.clear();

        for (int i = 0; i < totalAngles + 1; i += step) {
            double rad = (double) (totalAngles - i) * 3.141592653589793D / 180.0D;
            float xp = (float) (Math.cos(rad) * radius);
            float zp = (float) (Math.sin(rad) * radius);
            vertexFuncs.add(() -> vertexBuffer.vertex(mat, xp, f1, zp).color(r, g, b, alpha2).endVertex());
        }

        RenderHelper.triangleFan(centerFunc, vertexFuncs);
        poseStack.popPose();
    }
}
