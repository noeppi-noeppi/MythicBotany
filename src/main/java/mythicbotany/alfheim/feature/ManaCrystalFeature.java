package mythicbotany.alfheim.feature;

import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import mythicbotany.alfheim.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;

public class ManaCrystalFeature extends Feature<NoneFeatureConfiguration> {

    public ManaCrystalFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return AlfheimWorldGenUtil.generateTries(context, 1, this::tryGenerate);
    }

    private boolean tryGenerate(FeaturePlaceContext<NoneFeatureConfiguration> context, HorizontalPos hor) {
        if (context.random().nextInt(2) == 0) {
            BlockPos pos = AlfheimWorldGenUtil.highestFreeBlock(context.level(), hor, AlfheimWorldGenUtil::passReplaceableAndDreamWood);
            if (pos.getY() >= 84 && context.level().getBlockState(pos.below()).canOcclude()
                    && context.level().getBlockState(pos.below().north()).canOcclude()
                    && context.level().getBlockState(pos.below().south()).canOcclude()
                    && context.level().getBlockState(pos.below().east()).canOcclude()
                    && context.level().getBlockState(pos.below().west()).canOcclude()) {
                if (!context.level().setBlock(pos, ModBlocks.dilutedPool.defaultBlockState(), 2)) {
                    return false;
                }
                try {
                    BlockEntity te = context.level().getBlockEntity(pos);
                    if (te instanceof TilePool) {
                        te.blockState = ModBlocks.dilutedPool.defaultBlockState();
                        CompoundTag nbt = new CompoundTag();
                        nbt.putInt("manaCap", 10000);
                        nbt.putInt("mana", 0);
                        ((TilePool) te).readPacketNBT(nbt);
                        ((TilePool) te).receiveMana(10 + context.random().nextInt(490));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BlockState bifrost = ModBlocks.bifrostPerm.defaultBlockState();
                int mainHeight = 5 + context.random().nextInt(5);
                for (int i = 2; i < mainHeight; i++) {
                    context.level().setBlock(pos.above(i), bifrost, 2);
                }
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos base = pos.relative(dir);
                        int height = 2 + context.random().nextInt(mainHeight - 4);
                        for (int i = 0; i < height; i++) {
                            context.level().setBlock(base.above(i), bifrost, 2);
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
