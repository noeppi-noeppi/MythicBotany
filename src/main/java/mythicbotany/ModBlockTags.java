package mythicbotany;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public class ModBlockTags {

    public static final Tag.Named<Block> ALFHEIM_LOGS = BlockTags.bind(MythicBotany.getInstance().resource("alfheim_logs").toString());
    public static final Tag.Named<Block> ALFHEIM_LEAVES = BlockTags.bind(MythicBotany.getInstance().resource("alfheim_leaves").toString());
    public static final Tag.Named<Block> BASE_STONE_ALFHEIM = BlockTags.bind(MythicBotany.getInstance().resource("base_stone_alfheim").toString());
    public static final Tag.Named<Block> ALFHEIM_ORES = BlockTags.bind(MythicBotany.getInstance().resource("alfheim_ores").toString());
}
