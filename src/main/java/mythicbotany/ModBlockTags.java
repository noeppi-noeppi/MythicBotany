package mythicbotany;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public class ModBlockTags {

    public static final Tag.Named<Block> ALFHEIM_LOGS = BlockTags.bind(new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_logs").toString());
    public static final Tag.Named<Block> ALFHEIM_LEAVES = BlockTags.bind(new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_leaves").toString());
    public static final Tag.Named<Block> BASE_STONE_ALFHEIM = BlockTags.bind(new ResourceLocation(MythicBotany.getInstance().modid, "base_stone_alfheim").toString());
    public static final Tag.Named<Block> ALFHEIM_ORES = BlockTags.bind(new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_ores").toString());
}
