package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.DimensionData;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Holder;
import net.minecraft.world.level.dimension.LevelStem;

public class AlfheimDimension extends DimensionData {

    private final AlfheimNoise noise = this.resolve(AlfheimNoise.class);
    private final AlfheimDimensionTypes dimensionTypes = this.resolve(AlfheimDimensionTypes.class);
    private final AlfheimBiomeLayers layers = this.resolve(AlfheimBiomeLayers.class);
    private final AlfheimSurface surface = this.resolve(AlfheimSurface.class);
    
    public final Holder<LevelStem> dimension = this.dimension(Alfheim.DIMENSION, this.dimensionTypes.alfheim)
            .layeredBiome(48, 120, this.layers.alfheim)
            .noiseGenerator(this.noise.alfheim)
            .surfaceOverride(this.surface.alfheimSurface)
            .build();
    
    public AlfheimDimension(Properties properties) {
        super(properties);
    }
}
