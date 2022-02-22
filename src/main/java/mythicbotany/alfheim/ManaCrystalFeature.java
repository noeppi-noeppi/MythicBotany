package mythicbotany.alfheim;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import java.util.Random;

public class ManaCrystalFeature extends Feature<NoneFeatureConfiguration> {

    public ManaCrystalFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        boolean success = false;
        for (int i = 0; i < 1; i++) {
            if (tryGenerate(level, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)))) {
                success = true;
            }
        }
        return success;
    }

    private boolean tryGenerate(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        if (rand.nextInt(2) == 0) {
            BlockPos pos = AlfheimWorldGen.highestFreeBlock(level, hor, AlfheimWorldGen::passReplaceableAndLeaves);
            if (pos.getY() >= 84 && level.getBlockState(pos.below()).canOcclude()
                    && level.getBlockState(pos.below().north()).canOcclude()
                    && level.getBlockState(pos.below().south()).canOcclude()
                    && level.getBlockState(pos.below().east()).canOcclude()
                    && level.getBlockState(pos.below().west()).canOcclude()) {
                if (!level.setBlock(pos, ModBlocks.dilutedPool.defaultBlockState(), 2)) {
                    return false;
                }
                try {
                    BlockEntity te = level.getBlockEntity(pos);
                    if (te instanceof TilePool) {
                        te.setPosition(pos);
                        te.blockState = ModBlocks.dilutedPool.defaultBlockState();
                        CompoundTag nbt = new CompoundTag();
                        nbt.putInt("manaCap", 10000);
                        nbt.putInt("mana", 0);
                        ((TilePool) te).readPacketNBT(nbt);
                        ((TilePool) te).receiveMana(10 + rand.nextInt(490));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BlockState bifrost = ModBlocks.bifrostPerm.defaultBlockState();
                int mainHeight = 5 + rand.nextInt(5);
                for (int i = 2; i < mainHeight; i++) {
                    level.setBlock(pos.above(i), bifrost, 2);
                }
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos base = pos.relative(dir);
                        int height = 2 + rand.nextInt(mainHeight - 4);
                        for (int i = 0; i < height; i++) {
                            level.setBlock(base.above(i), bifrost, 2);
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
