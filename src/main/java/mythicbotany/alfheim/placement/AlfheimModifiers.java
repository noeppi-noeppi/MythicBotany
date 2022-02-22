package mythicbotany.alfheim.placement;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class AlfheimModifiers {

    public static final List<PlacementModifier> metamorphicStone = List.of(
            InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())
    );
    
    public static final List<PlacementModifier> trees = List.of(
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
            onGroundFor(Blocks.OAK_SAPLING),
            BiomeFilter.biome()
    );
    
    public static final List<PlacementModifier> looseTrees = ImmutableList.<PlacementModifier>builder()
            .add(RarityFilter.onAverageOnceEvery(10)).addAll(trees).build();
    
    public static final List<PlacementModifier> denseTrees = ImmutableList.<PlacementModifier>builder()
            .add(PlacementUtils.countExtra(2, 0.1f, 1)).addAll(trees).build();
    
    public static final List<PlacementModifier> vegetation = List.of(
            CountPlacement.of(15),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
            BiomeFilter.biome()
    );
    
    public static List<PlacementModifier> ore(int count, VerticalAnchor min, VerticalAnchor max) {
        ImmutableList.Builder<PlacementModifier> list = ImmutableList.builder();
        if (count > 1) list.add(CountPlacement.of(5));
        list.add(InSquarePlacement.spread());
        list.add(HeightRangePlacement.triangle(min, max));
        return list.build();
    }
    
    @SuppressWarnings("SameParameterValue")
    private static PlacementModifier onGroundFor(Block block) {
        return onGroundFor(block.defaultBlockState());
    }

    private static PlacementModifier onGroundFor(BlockState state) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.wouldSurvive(state, BlockPos.ZERO),
                BlockPredicate.matchesFluid(Fluids.EMPTY, BlockPos.ZERO)
        ));
    }
}
