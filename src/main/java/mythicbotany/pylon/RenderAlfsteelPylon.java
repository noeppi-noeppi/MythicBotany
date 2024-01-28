package mythicbotany.pylon;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import mythicbotany.MythicBotany;
import mythicbotany.register.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.moddingx.libx.render.ClientTickHandler;
import org.moddingx.libx.util.lazy.LazyValue;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.model.BotaniaModelLayers;
import vazkii.botania.client.model.NaturaPylonModel;
import vazkii.botania.client.model.PylonModel;
import vazkii.botania.mixin.client.RenderTypeAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class RenderAlfsteelPylon implements BlockEntityRenderer<TileAlfsteelPylon> {

    public static final ResourceLocation TEXTURE = MythicBotany.getInstance().resource("textures/model/pylon_alfsteel.png");
    public static final RenderType TARGET = createRenderType(false);
    public static final RenderType DIRECT_TARGET = createRenderType(true);
    
    private final NaturaPylonModel model;
    private ItemDisplayContext forceTransform = ItemDisplayContext.NONE;

    public RenderAlfsteelPylon(BlockEntityRendererProvider.Context ctx) {
        this.model = new NaturaPylonModel(ctx.bakeLayer(BotaniaModelLayers.PYLON_NATURA));
    }
    
    public void render(@Nonnull TileAlfsteelPylon blockEntity, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        this.doRender(blockEntity, partialTicks, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    public void doRender(@Nullable TileAlfsteelPylon pylon, float pticks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        boolean direct = pylon == null && (this.forceTransform == ItemDisplayContext.GUI || this.forceTransform.firstPerson());
        RenderType glow = direct ? DIRECT_TARGET : TARGET;

        poseStack.pushPose();
        float worldTime = (float) ClientTickHandler.ticksInGame() + pticks;
        worldTime += pylon == null ? 0.0F : (float) (new Random(pylon.getBlockPos().hashCode())).nextInt(360);
        poseStack.translate(0.0D, pylon == null ? 1.35D : 1.5D, 0.0D);
        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, -0.5D);
        if (pylon != null) {
            poseStack.mulPose(Axis.YP.rotationDegrees(worldTime * 1.5F));
        }

        RenderType layer = RenderType.entityTranslucent(TEXTURE);
        VertexConsumer vertex = buffer.getBuffer(layer);
        ((PylonModel) this.model).renderRing(poseStack, vertex, light, overlay);
        if (pylon != null) {
            poseStack.translate(0.0D, Math.sin((double) worldTime / 20.0D) / 20.0D - 0.025D, 0.0D);
        }

        poseStack.popPose();
        poseStack.pushPose();
        if (pylon != null) {
            poseStack.translate(0.0D, Math.sin((double) worldTime / 20.0D) / 17.5D, 0.0D);
        }

        poseStack.translate(0.5D, 0.0D, -0.5D);
        if (pylon != null) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-worldTime));
        }

        vertex = buffer.getBuffer(glow);
        ((PylonModel) this.model).renderCrystal(poseStack, vertex, light, overlay);
        poseStack.popPose();
        poseStack.popPose();
    }

    public static class ItemRenderer extends BlockEntityWithoutLevelRenderer {

        private static final LazyValue<TileAlfsteelPylon> DUMMY = new LazyValue<>(() -> new TileAlfsteelPylon(ModBlocks.alfsteelPylon.getBlockEntityType(), BlockPos.ZERO, ModBlocks.alfsteelPylon.defaultBlockState()));

        public ItemRenderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        
        @Override
        public void renderByItem(ItemStack stack, @Nonnull ItemDisplayContext context, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
            if (Block.byItem(stack.getItem()) instanceof BlockAlfsteelPylon) {
                BlockEntityRenderer<TileAlfsteelPylon> renderer = this.blockEntityRenderDispatcher.getRenderer(DUMMY.get());
                //noinspection ConstantConditions
                ((RenderAlfsteelPylon) renderer).forceTransform = context;
                ((RenderAlfsteelPylon) renderer).doRender(null, 0f, poseStack, buffer, light, overlay);
            }
        }
    }
    
    private static RenderType createRenderType(boolean direct) {
        RenderType.CompositeState.CompositeStateBuilder state = RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(CoreShaders::pylon))
                .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY);
        if (!direct) state = state.setOutputState(RenderStateShard.ITEM_ENTITY_TARGET);
        return RenderTypeAccessor.create(
                "mythicbotany_pylon" + (direct ? "_direct" : ""), DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS, 128, false, false,
                state.createCompositeState(false)
        );
    }
}
