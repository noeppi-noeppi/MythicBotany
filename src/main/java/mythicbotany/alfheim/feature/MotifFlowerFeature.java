package mythicbotany.alfheim.feature;

import com.google.common.collect.ImmutableList;
import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import mythicbotany.alfheim.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.List;

public class MotifFlowerFeature extends Feature<NoneFeatureConfiguration> {

    private static final List<BlockState> FLOWERS = ImmutableList.of(
            ModBlocks.motifDaybloom.defaultBlockState(),
            ModBlocks.motifNightshade.defaultBlockState()
    );

    public MotifFlowerFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return AlfheimWorldGenUtil.generateTries(context, 3, this::tryGenerate);
    }

    private boolean tryGenerate(FeaturePlaceContext<NoneFeatureConfiguration> context, HorizontalPos hor) {
        BlockPos pos = AlfheimWorldGenUtil.highestFreeBlock(context.level(), hor, AlfheimWorldGenUtil::passReplaceableAndLeaves);
        BlockState state = FLOWERS.get(context.random().nextInt(FLOWERS.size()));
        if (state.canSurvive(context.level(), pos)) {
            return context.level().setBlock(pos, state, 2);
        } else {
            return false;
        }
    }
}
