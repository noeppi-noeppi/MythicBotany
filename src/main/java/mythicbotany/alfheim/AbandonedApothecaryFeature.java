package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

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
    public boolean place(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoneFeatureConfiguration config) {
        if (rand.nextInt(6) == 0) {
            return tryGenerate(level, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)));
        } else {
            return false;
        }
    }

    private boolean tryGenerate(@Nonnull WorldGenLevel level, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(level, hor, AlfheimWorldGen::passReplaceableAndLeaves);
        //noinspection deprecation
        if (level.getBlockState(pos.below()).canOcclude() && level.getBlockState(pos.above()).isAir()) {
            BlockState state = STATES.get(rand.nextInt(STATES.size()));
            if (rand.nextInt(30) == 0) {
                return level.setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.LAVA), 2);
            } else if (rand.nextInt(4) != 0) {
                if (!level.setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.WATER), 2)) {
                    return false;
                }
                try {
                    BlockEntity te = level.getBlockEntity(pos);
                    if (te instanceof TileAltar) {
                        te.setPosition(pos);
                        te.blockState = state.setValue(BlockAltar.FLUID, IPetalApothecary.State.WATER);
                        int petals = rand.nextInt(5);
                        for (int i = 0; i < petals; i++) {
                            ((TileAltar) te).getItemHandler().setItem(i, new ItemStack(PETALS.get(rand.nextInt(PETALS.size()))));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return level.setBlock(pos, state.setValue(BlockAltar.FLUID, IPetalApothecary.State.EMPTY), 2);
            }
        } else {
            return false;
        }
    }
}
