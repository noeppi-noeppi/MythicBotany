package mythicbotany.pylon;

import net.minecraft.item.ItemStack;

public interface PylonRepairable {

    int getRepairManaPerTick(ItemStack stack);

    default int getRepairAmountPerTick() {
        return 1;
    }

    default boolean canRepair(ItemStack stack) {
        return stack.getDamage() > 0;
    }

    default ItemStack repairOneTick(ItemStack stack) {
        if (stack.getDamage() > 0) {
            stack.setDamage(Math.max(0, stack.getDamage() - getRepairAmountPerTick()));
        }
        return stack;
    }
}
