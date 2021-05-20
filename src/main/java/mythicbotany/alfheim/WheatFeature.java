package mythicbotany.alfheim;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class WheatFeature extends Feature<NoFeatureConfig> {

    public WheatFeature() {
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
        for (int i = 0; i < 5; i++) {
            int length = rand.nextInt(5);
            BlockPos.Mutable mpos = hor.toMutable();
            for (int j = 0; j < length; j++) {
                tryPlace(world, mpos);
                mpos.move(Direction.byHorizontalIndex(rand.nextInt(4)));
            }
        }
        return true;
    }

    private void tryPlace(@Nonnull ISeedReader world, BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(world, hor, AlfheimWorldGen::passReplaceableNoCrops);
        if (world.getBlockState(pos.down()).isSolid()) {
            world.setBlockState(pos.down(), Blocks.FARMLAND.getDefaultState(), 2);
            world.setBlockState(pos, Blocks.WHEAT.getDefaultState().with(BlockStateProperties.AGE_0_7, 7), 2);
        }
    }
}
