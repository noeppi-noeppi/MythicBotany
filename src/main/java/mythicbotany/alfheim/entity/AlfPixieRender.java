package mythicbotany.alfheim.entity;

import mythicbotany.MythicBotany;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AlfPixieRender extends MobRenderer<AlfPixie, AlfPixieModel> {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/entity/alf_pixie.png");

    public AlfPixieRender(EntityRendererManager renderManager) {
        super(renderManager, new AlfPixieModel(), 0);
    }


    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull AlfPixie entity) {
        return TEXTURE;
    }
}
