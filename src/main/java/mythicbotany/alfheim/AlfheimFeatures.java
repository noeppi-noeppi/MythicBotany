package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import mythicbotany.ModBlockTags;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import vazkii.botania.common.block.ModFluffBlocks;

import net.minecraft.data.worldgen.Features;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;

public class AlfheimFeatures {

    public static final RuleTest ALFHEIM_STONE_SIMPLE = new BlockMatchTest(vazkii.botania.common.block.ModBlocks.livingrock);
    public static final RuleTest ALFHEIM_STONE = new TagMatchTest(ModBlockTags.BASE_STONE_ALFHEIM);

    public static final StructurePieceType ANDWARI_CAVE_PIECE = AndwariCave.Piece::new;

    public static final ConfiguredSurfaceBuilder<?> GRASS_SURFACE = SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderBaseConfiguration(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.COARSE_DIRT.defaultBlockState()));
    public static final ConfiguredSurfaceBuilder<?> GOLD_SURFACE = SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderBaseConfiguration(vazkii.botania.common.block.ModBlocks.goldenGrass.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.COARSE_DIRT.defaultBlockState()));

    public static final ConfiguredFeature<?, ?> METAMORPHIC_FOREST_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneForest.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_MOUNTAIN_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneMountain.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_FUNGAL_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneFungal.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_SWAMP_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneSwamp.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_DESERT_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneDesert.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_TAIGA_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneTaiga.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_MESA_STONE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneMesa.defaultBlockState(), 27)).range(256).squared();
    public static final ConfiguredFeature<TreeConfiguration, ?> TREES_DREAMWOOD = Feature.TREE.configured((
            new TreeConfiguration.TreeConfigurationBuilder(
                    new SimpleStateProvider(vazkii.botania.common.block.ModBlocks.dreamwood.defaultBlockState()),
                    new SimpleStateProvider(ModBlocks.dreamwoodLeaves.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true)),
                    new RandomFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(0)),
                    new ShatteredTrunkPlacer(7, 4, 0),
                    new TwoLayersFeatureSize(1, 0, 1))
    ).ignoreVines().build());
    public static final ConfiguredFeature<?, ?> TREES_DREAMWOOD_LOOSE = Feature.SIMPLE_RANDOM_SELECTOR
            .configured(new SimpleRandomFeatureConfiguration(ImmutableList.of(() -> TREES_DREAMWOOD)))
            .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
            .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(10)));
    public static final ConfiguredFeature<?, ?> TREES_DREAMWOOD_DENSE = Feature.SIMPLE_RANDOM_SELECTOR
            .configured(new SimpleRandomFeatureConfiguration(ImmutableList.of(() -> TREES_DREAMWOOD)))
            .decorated(Features.Decorators.HEIGHTMAP_SQUARE)
            .decorated(FeatureDecorator.COUNT_EXTRA.configured(new FrequencyWithExtraChanceDecoratorConfiguration(2, 0.1f, 1)));
    public static final ConfiguredFeature<?, ?> MOTIF_FLOWERS = AlfheimWorldGen.motifFlowers.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> ALFHEIM_GRASS = Feature.RANDOM_PATCH.configured(Features.Configs.JUNGLE_GRASS_CONFIG).decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).count(15);
    public static final ConfiguredFeature<?, ?> MANA_CRYSTALS = AlfheimWorldGen.manaCrystals.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> ABANDONED_APOTHECARIES = AlfheimWorldGen.abandonedApothecaries.configured(NoneFeatureConfiguration.INSTANCE);
    public static final ConfiguredFeature<?, ?> ELEMENTIUM_ORE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE, ModBlocks.elementiumOre.defaultBlockState(), 9)).range(64).squared().count(5);
    public static final ConfiguredFeature<?, ?> DRAGONSTONE_ORE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE, ModBlocks.dragonstoneOre.defaultBlockState(), 4)).range(16).squared();
    public static final ConfiguredFeature<?, ?> GOLD_ORE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE, ModBlocks.goldOre.defaultBlockState(), 9)).range(32).squared().count(2);
    public static final ConfiguredFeature<?, ?> MORE_GOLD_ORE = Feature.ORE.configured(new OreConfiguration(ALFHEIM_STONE, ModBlocks.goldOre.defaultBlockState(), 9)).decorated(FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(32, 32, 80))).squared().count(20);
    public static final ConfiguredFeature<?, ?> WHEAT_FIELDS = AlfheimWorldGen.wheatFields.configured(NoneFeatureConfiguration.INSTANCE);

    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> ANDWARI_CAVE = AlfheimWorldGen.andwariCave.configured(NoneFeatureConfiguration.INSTANCE);
    
    public static void register() {
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave_piece"), ANDWARI_CAVE_PIECE);
        
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MythicBotany.getInstance().modid, "grass_surface"), GRASS_SURFACE);
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MythicBotany.getInstance().modid, "gold_surface"), GOLD_SURFACE);
        
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_forest_stone"), METAMORPHIC_FOREST_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_mountain_stone"), METAMORPHIC_MOUNTAIN_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_fungal_stone"), METAMORPHIC_FUNGAL_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_swamp_stone"), METAMORPHIC_SWAMP_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_desert_stone"), METAMORPHIC_DESERT_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_taiga_stone"), METAMORPHIC_TAIGA_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_mesa_stone"), METAMORPHIC_MESA_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood"), TREES_DREAMWOOD);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood_loose"), TREES_DREAMWOOD_LOOSE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood_dense"), TREES_DREAMWOOD_DENSE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "motif_flowers"), MOTIF_FLOWERS);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_grass"), ALFHEIM_GRASS);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "mana_crystals"), MANA_CRYSTALS);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "abandoned_apothecaries"), ABANDONED_APOTHECARIES);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "elementium_ore"), ELEMENTIUM_ORE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "dragonstone_ore"), DRAGONSTONE_ORE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "gold_ore"), GOLD_ORE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "more_gold_ore"), MORE_GOLD_ORE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "wheat_fields"), WHEAT_FIELDS);
        
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave"), ANDWARI_CAVE);
    }
}
