package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.biome.BiomeLayer;
import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.BiomeLayerData;
import net.minecraft.core.Holder;

public class AlfheimBiomeLayers extends BiomeLayerData {

    private final AlfheimBiomes biomes = this.resolve(AlfheimBiomes.class);

    public final Holder<BiomeLayer> alfheim = this.layer()
            .fullRange()
            
            .biome(this.biomes.alfheimPlains)
            .temperature(0.89f, 0.91f)
            .humidity(0.89f, 0.91f)
            .continentalness(-0.02f, 0.05f)
            .erosion(-0.5f, -0.3f)
            .depth(0.01f, 0.03f)
            .weirdness(-0.6f, -0.3f)
            .add()

            .biome(this.biomes.alfheimHills)
            .temperature(0.89f, 0.91f)
            .humidity(0.89f, 0.91f)
            .continentalness(0.5f, 0.8f)
            .erosion(-0.3f, 0.2f)
            .depth(0.03f, 0.2f)
            .weirdness(-0.3f, 0.2f)
            .add()

            .biome(this.biomes.dreamwoodForest)
            .temperature(0.89f, 0.91f)
            .humidity(0.89f, 0.91f)
            .continentalness(0.02f, 0.06f)
            .erosion(-0.4f, -0.1f)
            .depth(0.02f, 0.04f)
            .weirdness(-0.6f, -0.3f)
            .add()

            .biome(this.biomes.goldenFields)
            .temperature(0.89f, 0.91f)
            .humidity(0.89f, 0.91f)
            .continentalness(-0.02f, 0.05f)
            .erosion(-0.5f, -0.3f)
            .depth(0.015f, 0.03f)
            .weirdness(-0.2f, 0.4f)
            .add()

            .biome(this.biomes.alfheimLakes)
            .temperature(0.89f, 0.91f)
            .humidity(0.89f, 0.91f)
            .continentalness(-0.04f, -0.01f)
            .erosion(-0.01f, 1.01f)
            .depth(-0.1f, -0.02f)
            .weirdness(-0.8f, -0.4f)
            .add()
            
            .build();
    
    public AlfheimBiomeLayers(Properties properties) {
        super(properties);
    }
}
