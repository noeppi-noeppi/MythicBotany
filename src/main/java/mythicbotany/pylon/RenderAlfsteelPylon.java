package mythicbotany.pylon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylonNatura;
import vazkii.botania.mixin.AccessorRenderState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RenderAlfsteelPylon extends TileEntityRenderer<TileAlfsteelPylon> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/model/pylon_alfsteel.png");
    public static final RenderType PYLON_GLOW = initFieldPylonGlow(false);
    public static final RenderType PYLON_GLOW_ITEM = initFieldPylonGlow(true);

    private static RenderType initFieldPylonGlow(boolean forItem) {
        RenderType.State.Builder glState = RenderType.State.getBuilder().texture(new RenderState.TextureState(TEXTURE, false, false)).transparency(AccessorRenderState.getTranslucentTransparency()).diffuseLighting(new RenderState.DiffuseLightingState(true)).alpha(new RenderState.AlphaState(0.0F)).cull(new RenderState.CullState(false)).lightmap(new RenderState.LightmapState(true));
        if (forItem)
            glState = glState.target(AccessorRenderState.getItemEntityTarget());
        RenderType layer = RenderType.makeType(MythicBotany.getInstance().modid  + ":alfsteel_pylon_glow" + (forItem ? "_item" : ""), DefaultVertexFormats.ENTITY, 7, 128, glState.build(false));
        return ShaderHelper.useShaders() ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.PYLON_GLOW, null, layer) : layer;
    }

    private final ModelPylonNatura model = new ModelPylonNatura();
    private ItemCameraTransforms.TransformType forceTransform = ItemCameraTransforms.TransformType.NONE;

    public RenderAlfsteelPylon(TileEntityRendererDispatcher manager) {
        super(manager);
    }

    public void render(@Nonnull TileAlfsteelPylon pylon, float partialTicks, @Nonnull MatrixStack ms, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        doRender(pylon, partialTicks, ms, buffer, light, overlay);
    }

    public void doRender(@Nullable TileAlfsteelPylon pylon, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
        boolean direct = pylon == null && (forceTransform == ItemCameraTransforms.TransformType.GUI || forceTransform.func_241716_a_());
        RenderType glow = direct ? PYLON_GLOW_ITEM : PYLON_GLOW;

        ms.push();
        float worldTime = (float) ClientTickHandler.ticksInGame + pticks;
        worldTime += pylon == null ? 0.0F : (float) (new Random(pylon.getPos().hashCode())).nextInt(360);
        ms.translate(0.0D, pylon == null ? 1.35D : 1.5D, 0.0D);
        ms.scale(1.0F, -1.0F, -1.0F);
        ms.push();
        ms.translate(0.5D, 0.0D, -0.5D);
        if (pylon != null) {
            ms.rotate(Vector3f.YP.rotationDegrees(worldTime * 1.5F));
        }

        RenderType layer = RenderType.getEntityTranslucent(TEXTURE);
        IVertexBuilder buffer = buffers.getBuffer(layer);
        ((IPylonModel) model).renderRing(ms, buffer, light, overlay);
        if (pylon != null) {
            ms.translate(0.0D, Math.sin((double) worldTime / 20.0D) / 20.0D - 0.025D, 0.0D);
        }

        ms.pop();
        ms.push();
        if (pylon != null) {
            ms.translate(0.0D, Math.sin((double) worldTime / 20.0D) / 17.5D, 0.0D);
        }

        ms.translate(0.5D, 0.0D, -0.5D);
        if (pylon != null) {
            ms.rotate(Vector3f.YP.rotationDegrees(-worldTime));
        }

        buffer = buffers.getBuffer(glow);
        ((IPylonModel) model).renderCrystal(ms, buffer, light, overlay);
        ms.pop();
        ms.pop();
    }

    public static class TEISR extends ItemStackTileEntityRenderer {

        private static final LazyValue<TileAlfsteelPylon> DUMMY = new LazyValue<>(() -> new TileAlfsteelPylon(ModBlocks.alfsteelPylon.getTileType()));

        public TEISR() {

        }

        @Override
        public void func_239207_a_(ItemStack stack, @Nonnull ItemCameraTransforms.TransformType type, @Nonnull MatrixStack ms, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
            if (Block.getBlockFromItem(stack.getItem()) instanceof BlockAlfsteelPylon) {
                TileEntityRenderer<TileAlfsteelPylon> r = TileEntityRendererDispatcher.instance.getRenderer(DUMMY.getValue());
                //noinspection ConstantConditions
                ((RenderAlfsteelPylon) r).forceTransform = type;
                ((RenderAlfsteelPylon) r).doRender(null, 0f, ms, buffer, light, overlay);
            }
        }
    }
}
