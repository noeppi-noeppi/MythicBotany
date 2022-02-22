package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class MotifFlowerFeature extends Feature<NoneFeatureConfiguration> {

    private static final List<BlockState> FLOWERS = ImmutableList.of(
            ModBlocks.motifDaybloom.defaultBlockState(),
            ModBlocks.motifNightshade.defaultBlockState()
    );

    public MotifFlowerFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        boolean success = false;
        for (int i = 0; i < 3; i++) {
            if (tryGenerate(level, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)))) {
                success = true;
            }
        }
        return success;
    }

    private boolean tryGenerate(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(level, hor, AlfheimWorldGen::passReplaceableAndLeaves);
        BlockState state = FLOWERS.get(rand.nextInt(FLOWERS.size()));
        if (state.canSurvive(level, pos)) {
            return level.setBlock(pos, state, 2);
        } else {
            return false;
        }
    }
}
