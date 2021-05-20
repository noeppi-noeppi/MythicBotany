package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import vazkii.botania.api.item.IPetalApothecary;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class AbandonedApothecaryFeature extends Feature<NoFeatureConfig> {

    private static final List<BlockState> STATES = ImmutableList.of(
            ModBlocks.defaultAltar.getDefaultState(),
            ModBlocks.forestAltar.getDefaultState(),
            ModBlocks.plainsAltar.getDefaultState(),
            ModBlocks.mountainAltar.getDefaultState(),
            ModBlocks.fungalAltar.getDefaultState(),
            ModBlocks.swampAltar.getDefaultState(),
            ModBlocks.desertAltar.getDefaultState(),
            ModBlocks.taigaAltar.getDefaultState(),
            ModBlocks.mesaAltar.getDefaultState(),
            ModBlocks.mossyAltar.getDefaultState()
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
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull NoFeatureConfig config) {
        if (rand.nextInt(6) == 0) {
            return tryGenerate(world, generator, rand, new BlockPos(pos.getX() + rand.nextInt(16), 0, pos.getZ() + rand.nextInt(16)));
        } else {
            return false;
        }
    }

    private boolean tryGenerate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos hor) {
        BlockPos pos = AlfheimWorldGen.highestFreeBlock(world, hor, AlfheimWorldGen::passReplaceableAndLeaves);
        //noinspection deprecation
        if (world.getBlockState(pos.down()).isSolid() && world.getBlockState(pos.up()).isAir()) {
            BlockState state = STATES.get(rand.nextInt(STATES.size()));
            if (rand.nextInt(30) == 0) {
                return world.setBlockState(pos, state.with(BlockAltar.FLUID, IPetalApothecary.State.LAVA), 2);
            } else if (rand.nextInt(4) != 0) {
                if (!world.setBlockState(pos, state.with(BlockAltar.FLUID, IPetalApothecary.State.WATER), 2)) {
                    return false;
                }
                try {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TileAltar) {
                        te.setPos(pos);
                        te.cachedBlockState = state.with(BlockAltar.FLUID, IPetalApothecary.State.WATER);
                        int petals = rand.nextInt(5);
                        for (int i = 0; i < petals; i++) {
                            ((TileAltar) te).getItemHandler().setInventorySlotContents(i, new ItemStack(PETALS.get(rand.nextInt(PETALS.size()))));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            } else {
                return world.setBlockState(pos, state.with(BlockAltar.FLUID, IPetalApothecary.State.EMPTY), 2);
            }
        } else {
            return false;
        }
    }
}
