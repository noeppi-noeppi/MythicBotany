package mythicbotany.register.tags;

import mythicbotany.MythicBotany;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

    public static final TagKey<Block> ALFHEIM_LOGS = BlockTags.create(MythicBotany.getInstance().resource("alfheim_logs"));
    public static final TagKey<Block> ALFHEIM_LEAVES = BlockTags.create(MythicBotany.getInstance().resource("alfheim_leaves"));
    public static final TagKey<Block> BASE_STONE_ALFHEIM = BlockTags.create(MythicBotany.getInstance().resource("base_stone_alfheim"));
    public static final TagKey<Block> ALFHEIM_ORES = BlockTags.create(MythicBotany.getInstance().resource("alfheim_ores"));
}
