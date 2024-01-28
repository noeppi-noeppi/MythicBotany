package mythicbotany.alfheim.datagen;

import mythicbotany.register.tags.ModWorldGenTags;
import net.minecraft.core.Holder;
import net.minecraft.world.level.dimension.LevelStem;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.Id;
import org.moddingx.libx.datagen.provider.sandbox.DimensionProviderBase;

public class AlfheimDimension extends DimensionProviderBase {

    private final AlfheimNoise noise = this.context.findRegistryProvider(AlfheimNoise.class);
    private final AlfheimDimensionTypes dimensionTypes = this.context.findRegistryProvider(AlfheimDimensionTypes.class);
    private final AlfheimSurface surface = this.context.findRegistryProvider(AlfheimSurface.class);
    
    @Id("alfheim")
    public final Holder<LevelStem> dimension = this.dimension(this.dimensionTypes.alfheim)
            .layeredBiome(ModWorldGenTags.ALFHEIM_LAYERS)
            .noiseGenerator(this.noise.alfheim)
            .surfaceOverride(this.surface.alfheimSurface)
            .build();
    
    public AlfheimDimension(DatagenContext ctx) {
        super(ctx);
    }
}
