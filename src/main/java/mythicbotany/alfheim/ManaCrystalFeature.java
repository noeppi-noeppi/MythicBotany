package mythicbotany.alfheim;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;
import java.util.Random;

public class ManaCrystalFeature extends Feature<NoFeatureConfig> {

    public ManaCrystalFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        boolean success = false;
        for (int i = 0; i < 1; i++) {
            if (tryGenerate(world, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)))) {
                success = true;
            }
        }
        return success;
    }

    private boolean tryGenerate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        if (rand.nextInt(2) == 0) {
            BlockPos pos = AlfheimWorldGen.highestFreeBlock(world, hor, AlfheimWorldGen::passReplaceableAndLeaves);
            if (pos.getY() >= 84 && world.getBlockState(pos.down()).isSolid()
                    && world.getBlockState(pos.down().north()).isSolid()
                    && world.getBlockState(pos.down().south()).isSolid()
                    && world.getBlockState(pos.down().east()).isSolid()
                    && world.getBlockState(pos.down().west()).isSolid()) {
                if (!world.setBlockState(pos, ModBlocks.dilutedPool.getDefaultState(), 2)) {
                    return false;
                }
                try {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TilePool) {
                        te.setPos(pos);
                        te.cachedBlockState = ModBlocks.dilutedPool.getDefaultState();
                        CompoundNBT nbt = new CompoundNBT();
                        nbt.putInt("manaCap", 10000);
                        nbt.putInt("mana", 0);
                        ((TilePool) te).readPacketNBT(nbt);
                        ((TilePool) te).receiveMana(10 + rand.nextInt(490));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BlockState bifrost = ModBlocks.bifrostPerm.getDefaultState();
                int mainHeight = 5 + rand.nextInt(5);
                for (int i = 2; i < mainHeight; i++) {
                    world.setBlockState(pos.up(i), bifrost, 2);
                }
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() != Direction.Axis.Y) {
                        BlockPos base = pos.offset(dir);
                        int height = 2 + rand.nextInt(mainHeight - 4);
                        for (int i = 0; i < height; i++) {
                            world.setBlockState(base.up(i), bifrost, 2);
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
