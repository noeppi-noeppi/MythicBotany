package mythicbotany.pylon;

import net.minecraft.world.item.ItemStack;

public interface PylonRepairable {

    int getRepairManaPerTick(ItemStack stack);

    default boolean canRepairPylon(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    default ItemStack repairOneTick(ItemStack stack) {
        if (stack.getDamageValue() > 0) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
        }
        return stack;
    }
}
