package mythicbotany.alfheim.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class AlfPixieModel extends EntityModel<AlfPixie> {

    public ModelRenderer body;
    public ModelRenderer leftWingT;
    public ModelRenderer leftWingB;
    public ModelRenderer rightWingT;
    public ModelRenderer rightWingB;

    public AlfPixieModel() {
        super(RenderType::getEntityCutoutNoCull);

        textureWidth = 32;
        textureHeight = 32;

        body = new ModelRenderer(this, 0, 0);
        body.setRotationPoint(0.0F, 16.0F, 0.0F);
        body.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);

        leftWingT = new ModelRenderer(this, 0, 4);
        leftWingT.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(leftWingT, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
        leftWingB = new ModelRenderer(this, 0, 11);
        leftWingB.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(leftWingB, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        rightWingT = new ModelRenderer(this, 0, 4);
        rightWingT.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(rightWingT, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
        rightWingB = new ModelRenderer(this, 0, 11);
        rightWingB.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(rightWingB, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
        body.render(matrixStack, buffer, light, overlay);
        leftWingT.render(matrixStack, buffer, light, overlay);
        leftWingB.render(matrixStack, buffer, light, overlay);
        rightWingT.render(matrixStack, buffer, light, overlay);
        rightWingB.render(matrixStack, buffer, light, overlay);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(@Nonnull AlfPixie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        rightWingT.rotateAngleY = -(MathHelper.cos(ageInTicks * 1.7F) * (float) Math.PI * 0.5F);
        leftWingT.rotateAngleY = MathHelper.cos(ageInTicks * 1.7F) * (float) Math.PI * 0.5F;
        rightWingB.rotateAngleY = -(MathHelper.cos(ageInTicks * 1.7F) * (float) Math.PI * 0.25F);
        leftWingB.rotateAngleY = MathHelper.cos(ageInTicks * 1.7F) * (float) Math.PI * 0.25F;
    }
}
