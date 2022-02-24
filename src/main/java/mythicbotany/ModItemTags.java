package mythicbotany;

import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

public class ModItemTags {
    
    public static final Tag.Named<Item> RITUAL_RUNES = ItemTags.bind(MythicBotany.getInstance().resource("ritual_runes").toString());
    public static final Tag.Named<Item> ALFHEIM_ORES = ItemTags.bind(MythicBotany.getInstance().resource("alfheim_ores").toString());
}
