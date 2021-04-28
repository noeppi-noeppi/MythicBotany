package mythicbotany.misc;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

public class Andwari {

    public static ItemStack randomAndwariItem(Random random) {
        int num = random.nextInt(40);
        if (num < 1) {
            return new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
        } else if (num < 4) {
            return new ItemStack(Items.GOLDEN_APPLE, 1 + random.nextInt(4));
        } else if (num < 13) {
            ItemStack stack;
            if (num < 5) {
                stack = new ItemStack(Items.GOLDEN_SWORD);
            } else if (num < 6) {
                stack = new ItemStack(Items.GOLDEN_AXE);
            } else if (num < 7) {
                stack = new ItemStack(Items.GOLDEN_PICKAXE);
            } else if (num < 8) {
                stack = new ItemStack(Items.GOLDEN_SHOVEL);
            } else if (num < 9) {
                stack = new ItemStack(Items.GOLDEN_HOE);
            } else if (num < 10) {
                stack = new ItemStack(Items.GOLDEN_HELMET);
            } else if (num < 11) {
                stack = new ItemStack(Items.GOLDEN_CHESTPLATE);
            } else if (num < 12) {
                stack = new ItemStack(Items.GOLDEN_LEGGINGS);
            } else {
                stack = new ItemStack(Items.GOLDEN_BOOTS);
            }
            if (random.nextBoolean()) {
                int amount = random.nextInt(3);
                for (int i = 0; i < amount; i++) {
                    stack = EnchantmentHelper.addRandomEnchantment(random, stack, 2 + random.nextInt(18), false);
                }
            }
            return stack;
        } else if (num < 14) {
            return new ItemStack(Items.GOLDEN_HORSE_ARMOR);
        } else if (num < 18) {
            return new ItemStack(Items.GOLDEN_CARROT, 1 + random.nextInt(6));
        } else if (num < 30) {
            return new ItemStack(Items.GOLD_INGOT, 1 + random.nextInt(8));
        } else {
            return new ItemStack(Items.GOLD_NUGGET, 1 + random.nextInt(18));
        }
    }
}
