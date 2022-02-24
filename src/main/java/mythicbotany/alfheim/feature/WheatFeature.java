package mythicbotany.alfheim.feature;

import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import mythicbotany.alfheim.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nonnull;

public class WheatFeature extends Feature<NoneFeatureConfiguration> {

    public WheatFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return AlfheimWorldGenUtil.generateTries(context, 3, this::tryGenerate);
    }

    private boolean tryGenerate(FeaturePlaceContext<NoneFeatureConfiguration> context, HorizontalPos hor) {
        for (int i = 0; i < 5; i++) {
            int length = context.random().nextInt(5);
            for (int j = 0; j < length; j++) {
                this.tryPlace(context.level(), hor);
                hor = hor.offset(Direction.from2DDataValue(context.random().nextInt(4)));
            }
        }
        return true;
    }

    private void tryPlace(@Nonnull WorldGenLevel level, HorizontalPos hor) {
        BlockPos pos = AlfheimWorldGenUtil.highestFreeBlock(level, hor, AlfheimWorldGenUtil::passReplaceableNoCrops);
        if (level.getBlockState(pos.below()).canOcclude() && level.getBlockState(pos.below()).getFluidState().isEmpty()) {
            level.setBlock(pos.below(), Blocks.FARMLAND.defaultBlockState(), 2);
            level.setBlock(pos, Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 7), 2);
        }
    }
}
