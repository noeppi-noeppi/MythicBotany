package mythicbotany.alfheim.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import javax.annotation.Nonnull;

public class ManaCrystalFeature extends Feature<NoneFeatureConfiguration> {

    public ManaCrystalFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (context.level().getBlockState(context.origin().below()).canOcclude()
                && context.level().getBlockState(context.origin().below().north()).canOcclude()
                && context.level().getBlockState(context.origin().below().south()).canOcclude()
                && context.level().getBlockState(context.origin().below().east()).canOcclude()
                && context.level().getBlockState(context.origin().below().west()).canOcclude()) {
            if (!context.level().setBlock(context.origin(), BotaniaBlocks.dilutedPool.defaultBlockState(), 2)) {
                return false;
            }
            try {
                BlockEntity te = context.level().getBlockEntity(context.origin());
                if (te instanceof ManaPoolBlockEntity pool) {
                    te.blockState = BotaniaBlocks.dilutedPool.defaultBlockState();
                    CompoundTag nbt = new CompoundTag();
                    nbt.putInt("manaCap", 10000);
                    nbt.putInt("mana", 0);
                    pool.readPacketNBT(nbt);
                    pool.receiveMana(10 + context.random().nextInt(490));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BlockState bifrost = BotaniaBlocks.bifrostPerm.defaultBlockState();
            int mainHeight = 5 + context.random().nextInt(5);
            for (int i = 2; i < mainHeight; i++) {
                context.level().setBlock(context.origin().above(i), bifrost, 2);
            }
            for (Direction dir : Direction.values()) {
                if (dir.getAxis() != Direction.Axis.Y) {
                    BlockPos base = context.origin().relative(dir);
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
    }
}
