package mythicbotany.alfheim.feature;

import com.google.common.collect.ImmutableList;
import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import mythicbotany.alfheim.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.api.block.IPetalApothecary;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

public class AbandonedApothecaryFeature extends Feature<NoneFeatureConfiguration> {

    private static final List<BlockState> STATES = ImmutableList.of(
            ModBlocks.defaultAltar.defaultBlockState(),
            ModBlocks.forestAltar.defaultBlockState(),
            ModBlocks.plainsAltar.defaultBlockState(),
            ModBlocks.mountainAltar.defaultBlockState(),
            ModBlocks.fungalAltar.defaultBlockState(),
            ModBlocks.swampAltar.defaultBlockState(),
            ModBlocks.desertAltar.defaultBlockState(),
            ModBlocks.taigaAltar.defaultBlockState(),
            ModBlocks.mesaAltar.defaultBlockState(),
            ModBlocks.mossyAltar.defaultBlockState()
    );

    private static final List<Item> PETALS = ImmutableList.of(
            ModItems.whitePetal, ModItems.orangePetal, ModItems.magentaPetal,
            ModItems.lightBluePetal, ModItems.yellowPetal, ModItems.limePetal,
            ModItems.pinkPetal, ModItems.grayPetal, ModItems.lightGrayPetal,
            ModItems.cyanPetal, ModItems.purplePetal, ModItems.bluePetal,
            ModItems.brownPetal, ModItems.greenPetal, ModItems.redPetal,
            ModItems.blackPetal
    );


    public AbandonedApothecaryFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (context.random().nextInt(3) == 0) {
            return AlfheimWorldGenUtil.generateTries(context, 1, this::tryGenerate);
        } else {
            return false;
        }
    }

    private boolean tryGenerate(FeaturePlaceContext<NoneFeatureConfiguration> context, HorizontalPos hor) {
        BlockPos pos = AlfheimWorldGenUtil.highestFreeBlock(context.level(), hor, AlfheimWorldGenUtil::passReplaceableAndLeaves);
        if (context.level().getBlockState(pos.below()).canOcclude() && context.level().getBlockState(pos.above()).isAir()) {
            BlockState state = STATES.get(context.random().nextInt(STATES.size()));
            if (context.random().nextInt(30) == 0) {
                return context.level().setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.LAVA), 2);
            } else if (context.random().nextInt(4) != 0) {
                if (!context.level().setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.WATER), 2)) {
                    return false;
                }
                try {
                    BlockEntity be = context.level().getBlockEntity(pos);
                    if (be instanceof TileAltar) {
                        be.blockState = state.setValue(BlockAltar.FLUID, IPetalApothecary.State.WATER);
                        int petals = context.random().nextInt(5);
                        for (int i = 0; i < petals; i++) {
                            ((TileAltar) be).getItemHandler().setItem(i, new ItemStack(PETALS.get(context.random().nextInt(PETALS.size()))));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return context.level().setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.EMPTY), 2);
            }
        } else {
            return false;
        }
    }
}
