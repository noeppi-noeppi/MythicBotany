package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.biome.BiomeLayer;
import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.BiomeLayerData;
import net.minecraft.core.Holder;

public class AlfheimBiomeLayers extends BiomeLayerData {

    private final AlfheimBiomes biomes = this.resolve(AlfheimBiomes.class);

    public final Holder<BiomeLayer> alfheim = this.layer()
            .fullRange()
            
            .biome(this.biomes.alfheimLakes)
            .continentalness(-1, 0)
            .erosion(-1, 1)
            .weirdness(-1, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .biome(this.biomes.alfheimLakes)
            .continentalness(0, 0.1f)
            .erosion(-1, 0)
            .weirdness(-1, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()

            .biome(this.biomes.alfheimPlains)
            .continentalness(0.1f, 0.15f)
            .erosion(-1, 0)
            .weirdness(-1, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .biome(this.biomes.alfheimPlains)
            .continentalness(0, 0.15f)
            .erosion(0, 1)
            .weirdness(-1, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .biome(this.biomes.dreamwoodForest)
            .continentalness(0.15f, 0.4f)
            .erosion(-1, 1)
            .weirdness(-1, 0)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .biome(this.biomes.goldenFields)
            .continentalness(0.15f, 0.4f)
            .erosion(-1, 1)
            .weirdness(0, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .biome(this.biomes.alfheimHills)
            .continentalness(0.4f, 1)
            .erosion(-1, 1)
            .weirdness(-1, 1)
            .depth(-1, 1)
            .temperature(-1, 1)
            .humidity(-1, 1)
            .add()
            
            .build();
    
    public AlfheimBiomeLayers(Properties properties) {
        super(properties);
    }
}
