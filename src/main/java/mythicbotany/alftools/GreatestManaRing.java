package mythicbotany.alftools;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;

public class GreatestManaRing extends ItemManaRing {

    private static final int MAX_MANA = 4000000;

    public GreatestManaRing(Properties props) {
        super(props);
    }

    public int getMaxMana(ItemStack stack) {
        return MAX_MANA;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }
}
