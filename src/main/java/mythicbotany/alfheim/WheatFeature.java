package mythicbotany.alfheim;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;
import java.util.Random;

public class WheatFeature extends Feature<NoneFeatureConfiguration> {

    public WheatFeature() {
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
        for (int i = 0; i < 5; i++) {
            int length = rand.nextInt(5);
            BlockPos.MutableBlockPos mpos = hor.mutable();
            for (int j = 0; j < length; j++) {
                tryPlace(level, mpos);
                mpos.move(Direction.from2DDataValue(rand.nextInt(4)));
            }
        }
        return true;
    }

    private void tryPlace(@Nonnull WorldGenLevel level, BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(level, hor, AlfheimWorldGen::passReplaceableNoCrops);
        if (level.getBlockState(pos.below()).canOcclude()) {
            level.setBlock(pos.below(), Blocks.FARMLAND.defaultBlockState(), 2);
            level.setBlock(pos, Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7), 2);
        }
    }
}
