package mythicbotany.alfheim.placement;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.ModBlocks;
import mythicbotany.alfheim.featuregen.RandomFoliagePlacer;
import mythicbotany.alfheim.featuregen.ShatteredTrunkPlacer;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import vazkii.botania.common.block.ModFluffBlocks;

@RegisterClass(prefix = "wg", priority = -2)
public class AlfheimFeatures {
    
    public static final ConfiguredFeature<?, ?> metamorphicForestStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneForest.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicMountainStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneMountain.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicFungalStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneFungal.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicSwampStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneSwamp.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicDesertStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneDesert.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicTaigaStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneTaiga.defaultBlockState(), 27));
    public static final ConfiguredFeature<?, ?> metamorphicMesaStone = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneMesa.defaultBlockState(), 27));
    
    public static final ConfiguredFeature<TreeConfiguration, ?> dreamwoodTrees = Feature.TREE.configured((
            new TreeConfiguration.TreeConfigurationBuilder(
                    SimpleStateProvider.simple(vazkii.botania.common.block.ModBlocks.dreamwood),
                    new ShatteredTrunkPlacer(7, 4, 0),
                    SimpleStateProvider.simple(ModBlocks.dreamwoodLeaves.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true)),
                    new RandomFoliagePlacer(UniformInt.of(2, 2), UniformInt.of(0, 0)),
                    new TwoLayersFeatureSize(1, 0, 1))
    ).ignoreVines().build());
    
    public static final ConfiguredFeature<?, ?> motifFlowers = AlfheimWorldGen.motifFlowers.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> alfheimGrass = Feature.RANDOM_PATCH.configured(VegetationFeatures.PATCH_GRASS_JUNGLE.config);
    public static final ConfiguredFeature<?, ?> manaCrystals = AlfheimWorldGen.manaCrystals.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> abandonedApothecaries = AlfheimWorldGen.abandonedApothecaries.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> elementiumOre = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.elementiumOre.defaultBlockState(), 9));
    public static final ConfiguredFeature<?, ?> dragonstoneOre = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.dragonstoneOre.defaultBlockState(), 4));
    public static final ConfiguredFeature<?, ?> goldOre = Feature.ORE.configured(new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.goldOre.defaultBlockState(), 9));
    public static final ConfiguredFeature<?, ?> wheatFields = AlfheimWorldGen.wheatFields.configured(NoneFeatureConfiguration.INSTANCE);

    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> andwariCave = AlfheimWorldGen.andwariCave.configured(AlfheimWorldGen.dummyJigsaw);

    public static final ConfiguredWorldCarver<CaveCarverConfiguration> cave = AlfheimCarvers.cave.configured(Carvers.CAVE.config());
    public static final ConfiguredWorldCarver<CanyonCarverConfiguration> canyon = AlfheimCarvers.canyon.configured(Carvers.CANYON.config());
}
