package mythicbotany.alfheim.datagen;

import net.minecraft.core.Holder;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.BiomeLayerProviderBase;
import org.moddingx.libx.sandbox.generator.BiomeLayer;

public class AlfheimBiomeLayers extends BiomeLayerProviderBase {

    private final AlfheimBiomes biomes = this.context.findRegistryProvider(AlfheimBiomes.class);

    public final Holder<BiomeLayer> alfheim = this.layer()
            .baseLayer()
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

    public AlfheimBiomeLayers(DatagenContext ctx) {
        super(ctx);
    }
}
