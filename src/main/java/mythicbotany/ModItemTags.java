package mythicbotany;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class ModItemTags {
    
    public static final TagKey<Item> RITUAL_RUNES = ItemTags.create(MythicBotany.getInstance().resource("ritual_runes"));
    public static final TagKey<Item> ALFHEIM_ORES = ItemTags.create(MythicBotany.getInstance().resource("alfheim_ores"));
}
