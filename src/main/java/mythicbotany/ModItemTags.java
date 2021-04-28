package mythicbotany;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModItemTags {
    
    public static final ITag.INamedTag<Item> RITUAL_RUNES = ItemTags.makeWrapperTag(new ResourceLocation(MythicBotany.getInstance().modid, "ritua_runes").toString());
}
