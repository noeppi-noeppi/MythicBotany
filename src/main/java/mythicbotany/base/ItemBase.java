package mythicbotany.base;

import mythicbotany.MythicBotany;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    public ItemBase(Properties properties) {
        super(properties.group(MythicBotany.TAB));
    }
}
