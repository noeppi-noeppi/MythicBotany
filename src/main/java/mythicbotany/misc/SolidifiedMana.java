package mythicbotany.misc;

import mythicbotany.ModItems;
import mythicbotany.config.MythicConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import vazkii.botania.api.mana.IManaItem;

public class SolidifiedMana extends Item implements IManaItem {

    public SolidifiedMana(Properties properties) {
        super(properties);
    }

    // TODO will be removed, just shut the compiler up for now.


    @Override
    public int getMana(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getMaxMana(ItemStack itemStack) {
        return 0;
    }

    @Override
    public void addMana(ItemStack itemStack, int i) {

    }

    @Override
    public boolean canReceiveManaFromPool(ItemStack itemStack, BlockEntity blockEntity) {
        return false;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack itemStack, ItemStack itemStack1) {
        return false;
    }

    @Override
    public boolean canExportManaToPool(ItemStack itemStack, BlockEntity blockEntity) {
        return false;
    }

    @Override
    public boolean canExportManaToItem(ItemStack itemStack, ItemStack itemStack1) {
        return false;
    }

    @Override
    public boolean isNoExport(ItemStack itemStack) {
        return false;
    }

    public static void dropMana(Level level, BlockPos pos, int mana) {
        dropMana(level, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, mana);
    }
    
    public static void dropMana(Level level, double x, double y, double z, int mana) {
        if (MythicConfig.solidified_mana) {
            int manaLeft = mana;
            while (manaLeft > 0) {
                int manaForStack = Math.min(manaLeft, 200000);
                manaLeft -= manaForStack;
                ItemStack stack = new ItemStack(ModItems.solidifiedMana);
                stack.getOrCreateTag().putInt("Mana", manaForStack);
                ItemEntity ie = new ItemEntity(level, x, y, z, stack);
                level.addFreshEntity(ie);
            }
        }
    }
}
