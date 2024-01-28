package mythicbotany.alfheim.worldgen;

import mythicbotany.register.ModBlocks;
import mythicbotany.register.tags.ModBlockTags;
import mythicbotany.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import vazkii.botania.common.block.BotaniaBlocks;

public class AlfheimWorldGen {

    public static final RuleTest livingrock = new BlockMatchTest(BotaniaBlocks.livingrock);
    public static final RuleTest alfheimStone = new TagMatchTest(ModBlockTags.BASE_STONE_ALFHEIM);
    
    public static BlockPos highestFreeBlock(LevelAccessor level, HorizontalPos hor) {
        return hor.trace(level.getMaxBuildHeight(), level.getMinBuildHeight(), p -> !level.isEmptyBlock(p) && !passthrough(level.getBlockState(p)));
    }
    
    public static boolean passthrough(BlockState state) {
        return (state.canBeReplaced() && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA && !(state.getBlock() instanceof LiquidBlock)) || state.is(BlockTags.LEAVES) || state.getBlock() == ModBlocks.dreamwoodLeaves || state.getBlock() == BotaniaBlocks.dreamwood;
    }
}
