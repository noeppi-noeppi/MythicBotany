package mythicbotany.functionalflora.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.IModelData;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RenderFunctionalFlower<T extends FunctionalFlowerBase> extends TileEntityRenderer<T> {

    public RenderFunctionalFlower(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull T te, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (te.isFloating() && !(Boolean) ConfigHandler.CLIENT.staticFloaters.get()) {
            IModelData data = te.getModelData();
            matrixStack.push();
            double worldTime = (float) ClientTickHandler.ticksInGame + partialTicks;
            if (te.getWorld() != null) {
                worldTime += (new Random(te.getPos().hashCode())).nextInt(1000);
            }

            matrixStack.translate(0.5D, 0.0D, 0.5D);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-((float) worldTime * 0.5F)));
            matrixStack.translate(-0.5D, (float) Math.sin(worldTime * 0.05000000074505806D) * 0.1F, 0.5D);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(4.0F * (float) Math.sin(worldTime * 0.03999999910593033D)));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
            BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
            BlockState state = te.getBlockState();
            IBakedModel ibakedmodel = brd.getModelForState(state);
            brd.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderTypeLookup.func_239220_a_(state, false)), state, ibakedmodel, 1.0F, 1.0F, 1.0F, combinedLightIn, combinedOverlayIn, data);
            matrixStack.pop();
        }/* else {
            IModelData data = te.getModelData();
            matrixStack.push();
            BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
            BlockState state = te.getBlockState();
            @SuppressWarnings("ConstantConditions")
            Vector3d offset = state.getOffset(te.getWorld(), te.getPos());
            matrixStack.translate(offset.x, offset.y, offset.z);
            IBakedModel ibakedmodel = brd.getModelForState(state);
            brd.getBlockModelRenderer().renderModel(matrixStack.getLast(), buffer.getBuffer(RenderTypeLookup.func_239220_a_(state, false)), state, ibakedmodel, 1.0F, 1.0F, 1.0F, combinedLightIn, combinedOverlayIn, data);
            matrixStack.pop();
        }*/
        if (Minecraft.getInstance().renderViewEntity instanceof LivingEntity) {
            LivingEntity view = (LivingEntity) Minecraft.getInstance().renderViewEntity;
            if (ItemMonocle.hasMonocle(view)) {
                RayTraceResult ray = Minecraft.getInstance().objectMouseOver;
                if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
                    BlockPos pos = ((BlockRayTraceResult) ray).getPos();
                    if (te.getPos().equals(pos) || hasBindingAttempt(view, te.getPos())) {
                        RadiusDescriptor descriptor = te.getRadius();
                        if (descriptor != null) {
                            matrixStack.push();
                            matrixStack.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
                            if (descriptor.isCircle()) {
                                renderCircle(matrixStack, buffer, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
                            } else {
                                RenderTileSpecialFlower.renderRectangle(matrixStack, buffer, descriptor.getAABB(), true, null, (byte) 32);
                            }
                            matrixStack.pop();
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

    public static void renderCircle(MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockPos center, double radius) {
        matrixStack.push();
        double x = (double) center.getX() + 0.5D;
        double y = center.getY();
        double z = (double) center.getZ() + 0.5D;
        matrixStack.translate(x, y, z);
        int color = MathHelper.hsvToRGB((float) (ClientTickHandler.ticksInGame % 200) / 200.0F, 0.6F, 1.0F);
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        int alpha = 32;
        float f = 0.0625F;
        int totalAngles = 360;
        int drawAngles = 360;
        int step = totalAngles / drawAngles;
        radius -= f;
        IVertexBuilder vertexBuffer = buffer.getBuffer(RenderHelper.CIRCLE);
        Matrix4f mat = matrixStack.getLast().getMatrix();
        Runnable centerFunc = () -> vertexBuffer.pos(mat, 0.0F, f, 0.0F).color(r, g, b, alpha).endVertex();
        List<Runnable> vertexFuncs = new ArrayList<>();

        for (int i = 0; i < totalAngles + 1; i += step) {
            double rad = (double) (totalAngles - i) * 3.141592653589793D / 180.0D;
            float xp = (float) (Math.cos(rad) * radius);
            float zp = (float) (Math.sin(rad) * radius);
            vertexFuncs.add(() -> vertexBuffer.pos(mat, xp, f, zp).color(r, g, b, alpha).endVertex());
        }

        RenderHelper.triangleFan(centerFunc, vertexFuncs);
        radius += f;
        float f1 = f + f / 4.0F;
        int alpha2 = 64;
        centerFunc = () -> vertexBuffer.pos(mat, 0.0F, f1, 0.0F).color(r, g, b, alpha2).endVertex();
        vertexFuncs.clear();

        for (int i = 0; i < totalAngles + 1; i += step) {
            double rad = (double) (totalAngles - i) * 3.141592653589793D / 180.0D;
            float xp = (float) (Math.cos(rad) * radius);
            float zp = (float) (Math.sin(rad) * radius);
            vertexFuncs.add(() -> vertexBuffer.pos(mat, xp, f1, zp).color(r, g, b, alpha2).endVertex());
        }

        RenderHelper.triangleFan(centerFunc, vertexFuncs);
        matrixStack.pop();
    }
}
