package mythicbotany.misc;

import mythicbotany.ModItems;
import mythicbotany.config.MythicConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SolidifiedMana extends Item implements IManaItem, IManaTooltipDisplay {

    public SolidifiedMana(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (getMana(stack) > 0) {
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.solidified_mana").mergeStyle(TextFormatting.GRAY));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.invalid_solidified_mana").mergeStyle(TextFormatting.RED));
        }
    }

    @Override
    public int getMana(ItemStack stack) {
        return stack.hasTag() ? stack.getOrCreateTag().getInt("Mana") : 0;
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return stack.hasTag() ? stack.getOrCreateTag().getInt("Mana") : 0;
    }

    @Override
    public void addMana(ItemStack stack, int mana) {
        if (mana < 0) {
            stack.getOrCreateTag().putInt("Mana", getMana(stack) + mana);
        }
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (getMana(stack) <= 0) {
            entity.remove();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
        return false;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return false;
    }

    @Override
    public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
        return true;
    }

    @Override
    public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
        return false;
    }

    @Override
    public boolean isNoExport(ItemStack stack) {
        return false;
    }

    @Override
    public float getManaFractionForDisplay(ItemStack stack) {
        return getMana(stack) > 0 ? 1 : 0;
    }
    
    public static void dropMana(World world, BlockPos pos, int mana) {
        dropMana(world, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, mana);
    }
    
    public static void dropMana(World world, double x, double y, double z, int mana) {
        if (MythicConfig.solidified_mana) {
            int manaLeft = mana;
            while (manaLeft > 0) {
                int manaForStack = Math.min(manaLeft, 200000);
                manaLeft -= manaForStack;
                ItemStack stack = new ItemStack(ModItems.solidifiedMana);
                stack.getOrCreateTag().putInt("Mana", manaForStack);
                ItemEntity ie = new ItemEntity(world, x, y, z, stack);
                world.addEntity(ie);
            }
        }
    }
}
