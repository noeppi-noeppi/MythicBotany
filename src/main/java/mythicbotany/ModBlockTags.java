package mythicbotany;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class ModBlockTags {

    public static final ITag.INamedTag<Block> ALFHEIM_LOGS = BlockTags.makeWrapperTag(new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_logs").toString());
    public static final ITag.INamedTag<Block> ALFHEIM_LEAVES = BlockTags.makeWrapperTag(new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_leaves").toString());
    public static final ITag.INamedTag<Block> BASE_STONE_ALFHEIM = BlockTags.makeWrapperTag(new ResourceLocation(MythicBotany.getInstance().modid, "base_stone_alfheim").toString());
}
