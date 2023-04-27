package mythicbotany.misc;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.WitherAconite;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.Item.Properties;

public class ItemFadedNetherStar extends Item {

    public ItemFadedNetherStar() {
        super(new Properties().stacksTo(1).durability(WitherAconite.DEFAULT_MANA_PER_STAR));
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return MythicConfig.flowers.witherAconiteMana;
    }
}
