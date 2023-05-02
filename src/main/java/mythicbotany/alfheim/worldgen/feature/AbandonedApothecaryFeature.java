package mythicbotany.alfheim.worldgen.feature;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import vazkii.botania.api.block.PetalApothecary;
import vazkii.botania.common.block.PetalApothecaryBlock;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

import javax.annotation.Nonnull;

public class AbandonedApothecaryFeature extends Feature<AbandonedApothecaryConfiguration> {
    
    public AbandonedApothecaryFeature() {
        super(AbandonedApothecaryConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<AbandonedApothecaryConfiguration> context) {
        if (context.level().getBlockState(context.origin().below()).canOcclude() && context.level().getBlockState(context.origin().above()).isAir()) {
            BlockState state = context.config().states().get(context.random().nextInt(context.config().states().size()));
            if (context.random().nextInt(30) == 0) {
                return context.level().setBlock(context.origin(), state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.LAVA), 2);
            } else if (context.random().nextInt(4) != 0) {
                if (!context.level().setBlock(context.origin(), state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.WATER), 2)) {
                    return false;
                }
                try {
                    BlockEntity be = context.level().getBlockEntity(context.origin());
                    if (be instanceof PetalApothecaryBlockEntity apothecary) {
                        be.blockState = state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.WATER);
                        int petals = context.random().nextInt(5);
                        for (int i = 0; i < petals; i++) {
                            apothecary.getItemHandler().setItem(i, new ItemStack(context.config().petals().get(context.random().nextInt(context.config().petals().size()))));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return context.level().setBlock(context.origin(), state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.EMPTY), 2);
            }
        } else {
            return false;
        }
    }
}
