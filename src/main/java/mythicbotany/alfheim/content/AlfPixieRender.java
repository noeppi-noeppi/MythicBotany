package mythicbotany.alfheim.content;

import mythicbotany.MythicBotany;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.client.model.ModModelLayers;

import javax.annotation.Nonnull;

public class AlfPixieRender extends MobRenderer<AlfPixie, AlfPixieModel> {
    
    private static final ResourceLocation TEXTURE = MythicBotany.getInstance().resource("textures/entity/alf_pixie.png");

    public AlfPixieRender(EntityRendererProvider.Context context) {
        super(context, new AlfPixieModel(context.bakeLayer(ModModelLayers.PIXIE)), 0);
    }
    
    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull AlfPixie entity) {
        return TEXTURE;
    }
}
