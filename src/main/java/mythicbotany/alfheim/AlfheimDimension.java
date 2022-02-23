package mythicbotany.alfheim;

import mythicbotany.alfheim.biome.AlfheimBiomes;
import mythicbotany.alfheim.placement.AlfheimFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import vazkii.botania.common.block.ModBlocks;

public class AlfheimDimension {

    public static void setup() {
        Alfheim.addBiome(AlfheimBiomes.alfheimPlains, new BiomeConfiguration().depth(0.025f).continentalness(0.05f));
        Alfheim.addBiome(AlfheimBiomes.alfheimHills, new BiomeConfiguration().depth(0.5f).continentalness(0.2f));
        Alfheim.addBiome(AlfheimBiomes.dreamwoodForest, new BiomeConfiguration().depth(0.1f).continentalness(0.05f));
        Alfheim.addBiome(AlfheimBiomes.goldenFields, new BiomeConfiguration().depth(0.025f).continentalness(0.07f).temperature(0.8f).humidity(0.4f).surface(ModBlocks.goldenGrass, Blocks.DIRT));
        Alfheim.addBiome(AlfheimBiomes.alfheimLakes, new BiomeConfiguration().depth(-0.5f).continentalness(0.1f).temperature(0.8f).humidity(0.7f).surface(Blocks.SAND, Blocks.GRAVEL));
        
        Alfheim.addStructure(AlfheimFeatures.andwariCave, new StructureFeatureConfiguration(28, 8, "andwari_cave".hashCode()), AlfheimBiomes.goldenFields);
    }
}
