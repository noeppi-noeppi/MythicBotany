package mythicbotany.alfheim.worldgen;

import mythicbotany.register.ModBlocks;
import mythicbotany.register.tags.ModBlockTags;
import mythicbotany.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Material;
import vazkii.botania.common.block.BotaniaBlocks;

public class AlfheimWorldGen {

    public static final RuleTest livingrock = new BlockMatchTest(BotaniaBlocks.livingrock);
    public static final RuleTest alfheimStone = new TagMatchTest(ModBlockTags.BASE_STONE_ALFHEIM);
    
    public static BlockPos highestFreeBlock(LevelAccessor level, HorizontalPos hor) {
        return hor.trace(level.getMaxBuildHeight(), level.getMinBuildHeight(), p -> !level.isEmptyBlock(p) && !passthrough(level.getBlockState(p)));
    }
    
    public static boolean passthrough(BlockState state) {
        return (state.getMaterial().isReplaceable() && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA) || state.getMaterial() == Material.LEAVES || state.getBlock() == ModBlocks.dreamwoodLeaves || state.getBlock() == BotaniaBlocks.dreamwood;
    }
}
