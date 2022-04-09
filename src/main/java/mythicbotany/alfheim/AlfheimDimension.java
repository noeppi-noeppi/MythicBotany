package mythicbotany.alfheim;

import mythicbotany.alfheim.biome.AlfheimBiomes;
import mythicbotany.alfheim.placement.AlfheimFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import vazkii.botania.common.block.ModBlocks;

public class AlfheimDimension {

    public static void setup() {
        Alfheim.addBiome(AlfheimBiomes.alfheimPlains, new BiomeConfiguration(BiomeConfiguration.Template.FLAT));
        Alfheim.addBiome(AlfheimBiomes.alfheimHills, new BiomeConfiguration(BiomeConfiguration.Template.HILLS));
        Alfheim.addBiome(AlfheimBiomes.dreamwoodForest, new BiomeConfiguration(BiomeConfiguration.Template.MODERATE));
        Alfheim.addBiome(AlfheimBiomes.goldenFields, new BiomeConfiguration(BiomeConfiguration.Template.FLAT).depth(0.015f, 0.03f).weirdness(-0.2f, 0.4f).surface(ModBlocks.goldenGrass, Blocks.DIRT));
        Alfheim.addBiome(AlfheimBiomes.alfheimLakes, new BiomeConfiguration(BiomeConfiguration.Template.LAKE).surface(SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(68), 0), BiomeConfiguration.normalAlfheimSurface(Blocks.GRASS_BLOCK, Blocks.DIRT)),
                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(68), 0)), BiomeConfiguration.normalAlfheimSurface(Blocks.SAND, Blocks.GRAVEL))
        )));
        
        Alfheim.addStructure(AlfheimFeatures.andwariCave, 1, 28, 8, Math.abs("andwari_cave".hashCode()));
    }
}
