package mythicbotany.mjoellnir;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class RenderEntityMjoellnir extends EntityRenderer<Mjoellnir> {

    protected RenderEntityMjoellnir(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@Nonnull Mjoellnir entity, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.pushPose();
        matrixStack.translate(0, 0.3, 0);
        Vec3 motion = entity.getDeltaMovement();
        if (motion.z != 0) {
            float r = (float) (Math.atan2(motion.x, motion.z) + (Math.PI / 2));
            if (!Float.isNaN(r)) {
                matrixStack.mulPose(Vector3f.YP.rotation(r));
            }
        }
        if (entity.isReturning()) {
            matrixStack.scale(0.7f, 0.7f, 0.7f);
        } else {
            double g = Math.sqrt((motion.x * motion.x) + (motion.z * motion.z));
            matrixStack.mulPose(Vector3f.ZP.rotation((float) (Math.atan2(g, motion.y) + Math.PI)));
        }
        matrixStack.translate(-0.5, -0.5, -0.5);
        RenderMjoellnir.renderHammer(entity.getStack(), null, matrixStack, buffer, packedLight);
        matrixStack.popPose();
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull Mjoellnir entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
