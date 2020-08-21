package mythicbotany.pylon;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PylonRepairables {

    public static final int PRIORITY_ITEM_WITH_INTERFACE = 10;
    public static final int PRIORITY_MENDING = -10;
    public static final int PRIORITY_DEFAULT = 0;

    private static final List<Pair<PylonRepairable, Integer>> repairables = new LinkedList<>();

    public static void register(PylonRepairable repairable) {
        register(repairable, PRIORITY_DEFAULT);
    }

    public static void register(PylonRepairable repairable, int priority) {
        repairables.add(Pair.of(repairable, -priority));
        repairables.sort(Comparator.comparing(Pair::getRight));
    }

    @Nullable
    public static PylonRepairable getRepairInfo(ItemStack stack) {
        if (stack.isEmpty())
            return null;
        for (Pair<PylonRepairable, Integer> entry : repairables) {
            if (entry.getLeft().canRepairPylon(stack))
                return entry.getLeft();
        }
        return null;
    }

    public static class ItemPylonRepairable implements PylonRepairable {

        @Override
        public int getRepairManaPerTick(ItemStack stack) {
            return stack.getItem() instanceof PylonRepairable ? ((PylonRepairable) stack.getItem()).getRepairManaPerTick(stack) : 0;
        }

        @Override
        public boolean canRepairPylon(ItemStack stack) {
            return stack.getItem() instanceof PylonRepairable && ((PylonRepairable) stack.getItem()).canRepairPylon(stack);
        }

        @Override
        public ItemStack repairOneTick(ItemStack stack) {
            return stack.getItem() instanceof PylonRepairable ? ((PylonRepairable) stack.getItem()).repairOneTick(stack) : stack;
        }
    }

    public static class MendingPylonRepairable implements PylonRepairable {

        public static final int MANA_PER_DURABILITY = 25;
        public static final int DURABILITY_PER_TICK = 5;

        @Override
        public int getRepairManaPerTick(ItemStack stack) {
            return EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0 ? MANA_PER_DURABILITY * DURABILITY_PER_TICK : 0;
        }

        @Override
        public boolean canRepairPylon(ItemStack stack) {
            return EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0 && stack.getDamage() > 0;
        }

        @Override
        public ItemStack repairOneTick(ItemStack stack) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) > 0 && stack.getDamage() > 0) {
                stack.setDamage(stack.getDamage() - DURABILITY_PER_TICK);
            }
            return stack;
        }
    }
}
