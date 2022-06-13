package mythicbotany.alfheim.placement;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.ModBiomeTags;
import mythicbotany.ModBlocks;
import mythicbotany.alfheim.featuregen.RandomFoliagePlacer;
import mythicbotany.alfheim.featuregen.ShatteredTrunkPlacer;
import mythicbotany.register.HackyHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import vazkii.botania.common.block.ModFluffBlocks;

@RegisterClass(prefix = "wg", priority = -2)
public class AlfheimFeatures {
    
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicForestStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneForest.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicMountainStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneMountain.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicFungalStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneFungal.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicSwampStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneSwamp.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicDesertStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneDesert.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicTaigaStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneTaiga.defaultBlockState(), 27)));
    public static final Holder<ConfiguredFeature<?, ?>> metamorphicMesaStone = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.livingrock, ModFluffBlocks.biomeStoneMesa.defaultBlockState(), 27)));
    
    public static final Holder<ConfiguredFeature<?, ?>> dreamwoodTrees = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.TREE, 
            new TreeConfiguration.TreeConfigurationBuilder(
                    SimpleStateProvider.simple(vazkii.botania.common.block.ModBlocks.dreamwood),
                    new ShatteredTrunkPlacer(7, 4, 0),
                    SimpleStateProvider.simple(ModBlocks.dreamwoodLeaves.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true)),
                    new RandomFoliagePlacer(UniformInt.of(2, 2), UniformInt.of(0, 0)),
                    new TwoLayersFeatureSize(1, 0, 1)
    ).ignoreVines().build()));
    
    public static final Holder<ConfiguredFeature<?, ?>> motifFlowers = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(AlfheimWorldGen.motifFlowers, NoneFeatureConfiguration.INSTANCE));
    public static final Holder<ConfiguredFeature<?, ?>> alfheimGrass = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.RANDOM_PATCH, VegetationFeatures.PATCH_GRASS_JUNGLE.value().config()));
    public static final Holder<ConfiguredFeature<?, ?>> manaCrystals = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(AlfheimWorldGen.manaCrystals, NoneFeatureConfiguration.INSTANCE));
    public static final Holder<ConfiguredFeature<?, ?>> abandonedApothecaries = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(AlfheimWorldGen.abandonedApothecaries, NoneFeatureConfiguration.INSTANCE));
    public static final Holder<ConfiguredFeature<?, ?>> elementiumOre = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.elementiumOre.defaultBlockState(), 9)));
    public static final Holder<ConfiguredFeature<?, ?>> dragonstoneOre = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.dragonstoneOre.defaultBlockState(), 4)));
    public static final Holder<ConfiguredFeature<?, ?>> goldOre = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(AlfheimWorldGen.alfheimStone, ModBlocks.goldOre.defaultBlockState(), 9)));
    public static final Holder<ConfiguredFeature<?, ?>> wheatFields = new HackyHolder<>(Registry.CONFIGURED_FEATURE_REGISTRY, new ConfiguredFeature<>(AlfheimWorldGen.wheatFields, NoneFeatureConfiguration.INSTANCE));

    public static final Holder<ConfiguredStructureFeature<?, ?>> andwariCave = new HackyHolder<>(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, AlfheimWorldGen.andwariCave.configured(AlfheimWorldGen.dummyJigsaw, ModBiomeTags.ANDWARI_CAVE));

    public static final Holder<ConfiguredWorldCarver<?>> cave = new HackyHolder<>(Registry.CONFIGURED_CARVER_REGISTRY, AlfheimCarvers.cave.configured(Carvers.CAVE.value().config()));
    public static final Holder<ConfiguredWorldCarver<?>> canyon = new HackyHolder<>(Registry.CONFIGURED_CARVER_REGISTRY, AlfheimCarvers.canyon.configured(Carvers.CANYON.value().config()));
}
