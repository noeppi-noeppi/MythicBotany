package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import mythicbotany.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import vazkii.botania.common.lib.ModTags;

import java.util.function.Predicate;

@RegisterClass(priority = 1) // This should to go before the biomes
public class AlfheimWorldGen {

    public static final Feature<NoFeatureConfig> motifFlowers = new MotifFlowerFeature();
    public static final Feature<NoFeatureConfig> manaCrystals = new ManaCrystalFeature();
    public static final Feature<NoFeatureConfig> abandonedApothecaries = new AbandonedApothecaryFeature();
    public static final Feature<NoFeatureConfig> wheatFields = new WheatFeature();
    
    public static final Structure<NoFeatureConfig> andwariCave = new AndwariCave();
    
    public static BlockPos highestFreeBlock(ISeedReader world, BlockPos pos, Predicate<BlockState> passthrough) {
        BlockPos.Mutable mpos = new BlockPos.Mutable(pos.getX(), world.getHeight() - 1, pos.getZ());
        while (mpos.getY() > 0 && world.isAirBlock(mpos) && passthrough.test(world.getBlockState(mpos)))
            mpos.move(0, -1, 0);
        return mpos.toImmutable().up();
    }

    public static BlockPos lowestFreeBlock(ISeedReader world, BlockPos pos, Predicate<BlockState> passthrough) {
        BlockPos.Mutable mpos = new BlockPos.Mutable(pos.getX(), 0, pos.getZ());
        while (mpos.getY() < world.getHeight() - 1 && (!world.isAirBlock(mpos) || !passthrough.test(world.getBlockState(mpos))))
            mpos.move(0, 1, 0);
        return mpos.toImmutable();
    }

    public static boolean passReplaceableAndLeaves(BlockState state) {
        return (state.getMaterial().isReplaceable() && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA) || state.getMaterial() == Material.LEAVES;
    }
    
    public static boolean passReplaceableAndDreamwood(BlockState state) {
        return passReplaceableAndLeaves(state) || state.getBlock() == ModBlocks.dreamwoodLeaves || state.getBlock() == vazkii.botania.common.block.ModBlocks.dreamwood;
    }
    
    public static boolean passReplaceableNoCrops(BlockState state) {
        return ((state.getMaterial().isReplaceable() && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA)) && !(state.getBlock() instanceof CropsBlock);
    }
}
