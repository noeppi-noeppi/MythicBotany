package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

import javax.annotation.Nonnull;

public class RenderReturnPortal extends TileEntityRenderer<TileReturnPortal> {

    public RenderReturnPortal(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(@Nonnull TileReturnPortal tile, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light, int overlay) {
        float alpha = (float) Math.min(1, (Math.sin(((double) ((float) ClientTickHandler.ticksInGame + partialTicks) / 8d) + 1) / 7d) + 0.7);
        matrixStack.push();
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
        matrixStack.translate(0, 0, -0.5);
        matrixStack.scale(1/3f, 1/3f, 1/3f);
        matrixStack.translate(0, 0, -0.5);
        this.renderPortal(matrixStack, buffer, MiscellaneousIcons.INSTANCE.alfPortalTex.getSprite(), 0, 0, 3, 3, alpha, overlay);
        matrixStack.translate(3, 0, 0.5);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
        this.renderPortal(matrixStack, buffer, MiscellaneousIcons.INSTANCE.alfPortalTex.getSprite(), 0, 0, 3, 3, alpha, overlay);
        matrixStack.pop();
    }
    
    @SuppressWarnings("SameParameterValue")
    private void renderPortal(MatrixStack matrixStack, IRenderTypeBuffer buffer, TextureAtlasSprite sprite, int x, int y, int width, int height, float alpha, int overlay) {
        IVertexBuilder vertex = buffer.getBuffer(Atlases.getTranslucentCullBlockType());
        Matrix4f model = matrixStack.getLast().getMatrix();
        Matrix3f normal = matrixStack.getLast().getNormal();
        vertex.pos(model, (float) x, (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).tex(sprite.getMinU(), sprite.getMaxV()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.pos(model, (float) (x + width), (float) (y + height), 0.0F).color(1.0F, 1.0F, 1.0F, alpha).tex(sprite.getMaxU(), sprite.getMaxV()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.pos(model, (float) (x + width), (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).tex(sprite.getMaxU(), sprite.getMinV()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
        vertex.pos(model, (float) x, (float) y, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).tex(sprite.getMinU(), sprite.getMinV()).overlay(overlay).lightmap(15728880).normal(normal, 1.0F, 0.0F, 0.0F).endVertex();
    }
}
