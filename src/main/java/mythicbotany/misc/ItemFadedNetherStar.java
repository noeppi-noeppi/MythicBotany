package mythicbotany.misc;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.WitherAconite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFadedNetherStar extends Item {

    public ItemFadedNetherStar() {
        super(new Properties().maxStackSize(1).maxDamage(WitherAconite.DEFAULT_MANA_PER_STAR));
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return MythicConfig.flowers.witherAconiteMana;
    }
}
