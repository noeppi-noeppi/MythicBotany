package mythicbotany.mjoellnir;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

public class RenderEntityMjoellnir extends EntityRenderer<EntityMjoellnir> {

    protected RenderEntityMjoellnir(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(@Nonnull EntityMjoellnir entity, float yaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int light) {
        super.render(entity, yaw, partialTicks, matrixStack, buffer, light);
        matrixStack.push();
        matrixStack.translate(0, 0.3, 0);
        Vector3d motion = entity.getMotion();
        if (motion.z != 0) {
            float r = (float) (Math.atan2(motion.x, motion.z) + (Math.PI / 2));
            if (!Float.isNaN(r)) {
                matrixStack.rotate(Vector3f.YP.rotation(r));
            }
        }
        if (entity.isReturning()) {
            matrixStack.scale(0.7f, 0.7f, 0.7f);
        } else {
            double g = Math.sqrt((motion.x * motion.x) + (motion.z * motion.z));
            matrixStack.rotate(Vector3f.ZP.rotation((float) (Math.atan2(g, motion.y) + Math.PI)));
        }
        matrixStack.translate(-0.5, -0.5, -0.5);
        RenderMjoellnir.renderHammer(entity.getStack(), null, matrixStack, buffer, light);
        matrixStack.pop();
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull EntityMjoellnir entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}
