package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import java.util.function.Predicate;

@RegisterClass(priority = 1) // This should to go before the biomes
public class AlfheimWorldGen {

    public static final Feature<NoneFeatureConfiguration> motifFlowers = new MotifFlowerFeature();
    public static final Feature<NoneFeatureConfiguration> manaCrystals = new ManaCrystalFeature();
    public static final Feature<NoneFeatureConfiguration> abandonedApothecaries = new AbandonedApothecaryFeature();
    public static final Feature<NoneFeatureConfiguration> wheatFields = new WheatFeature();
    
    public static final StructureFeature<NoneFeatureConfiguration> andwariCave = new AndwariCave();
    
    public static BlockPos highestFreeBlock(WorldGenLevel level, BlockPos pos, Predicate<BlockState> passthrough) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), level.getMaxBuildHeight() - 1, pos.getZ());
        while (mpos.getY() > 0 && level.isEmptyBlock(mpos) && passthrough.test(level.getBlockState(mpos)))
            mpos.move(0, -1, 0);
        return mpos.immutable().above();
    }

    public static BlockPos lowestFreeBlock(WorldGenLevel level, BlockPos pos, Predicate<BlockState> passthrough) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), 0, pos.getZ());
        while (mpos.getY() < level.getMaxBuildHeight() - 1 && (!level.isEmptyBlock(mpos) || !passthrough.test(level.getBlockState(mpos))))
            mpos.move(0, 1, 0);
        return mpos.immutable();
    }

    public static boolean passReplaceableAndLeaves(BlockState state) {
        return (state.getMaterial().isReplaceable() && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA) || state.getMaterial() == Material.LEAVES;
    }
    
    public static boolean passReplaceableAndDreamwood(BlockState state) {
        return passReplaceableAndLeaves(state) || state.getBlock() == ModBlocks.dreamwoodLeaves || state.getBlock() == vazkii.botania.common.block.ModBlocks.dreamwood;
    }
    
    public static boolean passReplaceableNoCrops(BlockState state) {
        return ((state.getMaterial().isReplaceable() && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA)) && !(state.getBlock() instanceof CropBlock);
    }
}
