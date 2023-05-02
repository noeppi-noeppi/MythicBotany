package mythicbotany.register.tags;

import mythicbotany.MythicBotany;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    
    public static final TagKey<Item> RITUAL_RUNES = ItemTags.create(MythicBotany.getInstance().resource("ritual_runes"));
    public static final TagKey<Item> ALFHEIM_ORES = ItemTags.create(MythicBotany.getInstance().resource("alfheim_ores"));
}
