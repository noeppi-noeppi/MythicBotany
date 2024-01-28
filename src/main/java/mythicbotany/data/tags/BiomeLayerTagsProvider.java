package mythicbotany.data.tags;

import mythicbotany.alfheim.datagen.AlfheimBiomeLayers;
import mythicbotany.register.tags.ModWorldGenTags;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.tags.TagProviderBase;
import org.moddingx.libx.sandbox.SandBox;
import org.moddingx.libx.sandbox.generator.BiomeLayer;

public class BiomeLayerTagsProvider extends TagProviderBase<BiomeLayer> {
    
    private final AlfheimBiomeLayers biomeLayers;
    
    public BiomeLayerTagsProvider(DatagenContext ctx) {
        super(ctx, SandBox.BIOME_LAYER);
        this.biomeLayers = ctx.findRegistryProvider(AlfheimBiomeLayers.class);
    }

    @Override
    protected void setup() {
        this.tag(ModWorldGenTags.ALFHEIM_LAYERS).add(this.biomeLayers.alfheim.value());
    }
}
