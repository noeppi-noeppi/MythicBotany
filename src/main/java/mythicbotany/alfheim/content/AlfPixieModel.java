package mythicbotany.alfheim.content;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class AlfPixieModel extends EntityModel<AlfPixie> {

    private final ModelPart body;
    private final ModelPart leftWingT;
    private final ModelPart leftWingB;
    private final ModelPart rightWingT;
    private final ModelPart rightWingB;

    public AlfPixieModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.body = root.getChild("body");
        this.leftWingT = root.getChild("leftWingT");
        this.leftWingB = root.getChild("leftWingB");
        this.rightWingT = root.getChild("rightWingT");
        this.rightWingB = root.getChild("rightWingB");
    }

    public void renderToBuffer(@Nonnull PoseStack poseStack, @Nonnull VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.body.render(poseStack, buffer, light, overlay);
        this.leftWingT.render(poseStack, buffer, light, overlay);
        this.leftWingB.render(poseStack, buffer, light, overlay);
        this.rightWingT.render(poseStack, buffer, light, overlay);
        this.rightWingB.render(poseStack, buffer, light, overlay);
    }

    @Override
    public void setupAnim(@Nonnull AlfPixie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.rightWingT.yRot = -(Mth.cos(ageInTicks * 1.7F) * 3.1415927F * 0.5F);
        this.leftWingT.yRot = Mth.cos(ageInTicks * 1.7F) * 3.1415927F * 0.5F;
        this.rightWingB.yRot = -(Mth.cos(ageInTicks * 1.7F) * 3.1415927F * 0.25F);
        this.leftWingB.yRot = Mth.cos(ageInTicks * 1.7F) * 3.1415927F * 0.25F;
    }
}
