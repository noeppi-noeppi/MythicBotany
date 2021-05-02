package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import mythicbotany.ModBlockTags;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.MineshaftPieces;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import vazkii.botania.common.block.ModFluffBlocks;

import java.util.Objects;

public class AlfheimFeatures {

    public static final RuleTest ALFHEIM_STONE_SIMPLE = new BlockMatchRuleTest(vazkii.botania.common.block.ModBlocks.livingrock);
    public static final RuleTest ALFHEIM_STONE = new TagMatchRuleTest(ModBlockTags.BASE_STONE_ALFHEIM);

    public static final IStructurePieceType ANDWARI_CAVE_PIECE = AndwariCave.Piece::new;

    public static final ConfiguredSurfaceBuilder<?> GRASS_SURFACE = SurfaceBuilder.DEFAULT.func_242929_a(new SurfaceBuilderConfig(Blocks.GRASS_BLOCK.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.COARSE_DIRT.getDefaultState()));
    public static final ConfiguredSurfaceBuilder<?> GOLD_SURFACE = SurfaceBuilder.DEFAULT.func_242929_a(new SurfaceBuilderConfig(vazkii.botania.common.block.ModBlocks.goldenGrass.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.COARSE_DIRT.getDefaultState()));

    public static final ConfiguredFeature<?, ?> METAMORPHIC_FOREST_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneForest.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_MOUNTAIN_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneMountain.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_FUNGAL_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneFungal.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_SWAMP_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneSwamp.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_DESERT_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneDesert.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_TAIGA_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneTaiga.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<?, ?> METAMORPHIC_MESA_STONE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE_SIMPLE, ModFluffBlocks.biomeStoneMesa.getDefaultState(), 27)).range(256).square();
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREES_DREAMWOOD = Feature.TREE.withConfiguration((
            new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(vazkii.botania.common.block.ModBlocks.dreamwood.getDefaultState()),
                    new SimpleBlockStateProvider(ModBlocks.dreamwoodLeaves.getDefaultState().with(BlockStateProperties.PERSISTENT, true)),
                    new RandomFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0)),
                    new ShatteredTrunkPlacer(7, 4, 0),
                    new TwoLayerFeature(1, 0, 1))
    ).setIgnoreVines().build());
    public static final ConfiguredFeature<?, ?> TREES_DREAMWOOD_LOOSE = Feature.SIMPLE_RANDOM_SELECTOR
            .withConfiguration(new SingleRandomFeature(ImmutableList.of(() -> TREES_DREAMWOOD)))
            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
            .withPlacement(Placement.CHANCE.configure(new ChanceConfig(10)));
    public static final ConfiguredFeature<?, ?> TREES_DREAMWOOD_DENSE = Feature.SIMPLE_RANDOM_SELECTOR
            .withConfiguration(new SingleRandomFeature(ImmutableList.of(() -> TREES_DREAMWOOD)))
            .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
            .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.1f, 1)));
    public static final ConfiguredFeature<?, ?> MOTIF_FLOWERS = AlfheimWorldGen.motifFlowers.withConfiguration(NoFeatureConfig.field_236559_b_);
    public static final ConfiguredFeature<?, ?> ALFHEIM_GRASS = Feature.RANDOM_PATCH.withConfiguration(Features.Configs.JUNGLE_VEGETATION_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(15);
    public static final ConfiguredFeature<?, ?> MANA_CRYSTALS = AlfheimWorldGen.manaCrystals.withConfiguration(NoFeatureConfig.field_236559_b_);
    public static final ConfiguredFeature<?, ?> ABANDONED_APOTHECARIES = AlfheimWorldGen.abandonedApothecaries.withConfiguration(NoFeatureConfig.field_236559_b_);
    public static final ConfiguredFeature<?, ?> ELEMENTIUM_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE, ModBlocks.elementiumOre.getDefaultState(), 9)).range(64).square().func_242731_b(5);
    public static final ConfiguredFeature<?, ?> DRAGONSTONE_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE, ModBlocks.dragonstoneOre.getDefaultState(), 4)).range(16).square();
    public static final ConfiguredFeature<?, ?> GOLD_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE, ModBlocks.goldOre.getDefaultState(), 9)).range(32).square().func_242731_b(2);
    public static final ConfiguredFeature<?, ?> MORE_GOLD_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(ALFHEIM_STONE, ModBlocks.goldOre.getDefaultState(), 9)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(32, 32, 80))).square().func_242731_b(20);
    public static final ConfiguredFeature<?, ?> WHEAT_FIELDS = AlfheimWorldGen.wheatFields.withConfiguration(NoFeatureConfig.field_236559_b_);

    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> ANDWARI_CAVE = AlfheimWorldGen.andwariCave.withConfiguration(NoFeatureConfig.field_236559_b_);
    
    public static void register() {
        Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave_piece"), ANDWARI_CAVE_PIECE);
        
        Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MythicBotany.getInstance().modid, "grass_surface"), GRASS_SURFACE);
        Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(MythicBotany.getInstance().modid, "gold_surface"), GOLD_SURFACE);
        
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_forest_stone"), METAMORPHIC_FOREST_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_mountain_stone"), METAMORPHIC_MOUNTAIN_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_fungal_stone"), METAMORPHIC_FUNGAL_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_swamp_stone"), METAMORPHIC_SWAMP_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_desert_stone"), METAMORPHIC_DESERT_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_taiga_stone"), METAMORPHIC_TAIGA_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "metamorphic_mesa_stone"), METAMORPHIC_MESA_STONE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood"), TREES_DREAMWOOD);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood_loose"), TREES_DREAMWOOD_LOOSE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "trees_dreamwood_dense"), TREES_DREAMWOOD_DENSE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "motif_flowers"), MOTIF_FLOWERS);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_grass"), ALFHEIM_GRASS);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "mana_crystals"), MANA_CRYSTALS);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "abandoned_apothecaries"), ABANDONED_APOTHECARIES);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "elementium_ore"), ELEMENTIUM_ORE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "dragonstone_ore"), DRAGONSTONE_ORE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "gold_ore"), GOLD_ORE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "more_gold_ore"), MORE_GOLD_ORE);
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "wheat_fields"), WHEAT_FIELDS);
        
        Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave"), ANDWARI_CAVE);
    }
}
