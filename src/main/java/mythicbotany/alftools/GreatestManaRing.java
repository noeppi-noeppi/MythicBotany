package mythicbotany.alftools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;

import net.minecraft.world.item.Item.Properties;

public class GreatestManaRing extends ItemManaRing {

    private static final int MAX_MANA = 4000000;

    public GreatestManaRing(Properties props) {
        super(props);
    }

    public int getMaxMana(ItemStack stack) {
        return MAX_MANA * stack.getCount();
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }
}
