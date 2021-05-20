package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class MotifFlowerFeature extends Feature<NoFeatureConfig> {

    private static final List<BlockState> FLOWERS = ImmutableList.of(
            ModBlocks.motifDaybloom.getDefaultState(),
            ModBlocks.motifNightshade.getDefaultState()
    );

    public MotifFlowerFeature() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        boolean success = false;
        for (int i = 0; i < 3; i++) {
            if (tryGenerate(world, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)))) {
                success = true;
            }
        }
        return success;
    }

    private boolean tryGenerate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(world, hor, AlfheimWorldGen::passReplaceableAndLeaves);
        BlockState state = FLOWERS.get(rand.nextInt(FLOWERS.size()));
        if (state.isValidPosition(world, pos)) {
            return world.setBlockState(pos, state, 2);
        } else {
            return false;
        }
    }
}
