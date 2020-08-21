package mythicbotany.runic;

import mythicbotany.ModItems;
import mythicbotany.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemRunicSpell extends ItemBase {

    public ItemRunicSpell(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flags) {
        Affinities aff = RuneSpell.getAffinities(getRune(stack, 1), getRune(stack, 2), getRune(stack, 3));
        if (aff == null) {
            list.add(new TranslationTextComponent("tooltip.mythicbotany.affinity.invalid").func_240699_a_(TextFormatting.LIGHT_PURPLE));
        } else {
            list.add(new StringTextComponent(aff.translatedString()).func_240699_a_(TextFormatting.LIGHT_PURPLE));
            list.add(new TranslationTextComponent("tooltip.mythicbotany.affinity.manacost", Integer.toString(aff.manaCost)).func_240699_a_(TextFormatting.LIGHT_PURPLE));
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        int rune1 = getRune(stack, 1);
        int rune2= getRune(stack, 2);
        int rune3 = getRune(stack, 3);

        Affinities aff = RuneSpell.getAffinities(rune1, rune2, rune3);
        if (aff == null || !ManaItemHandler.instance().requestManaExactForTool(stack, player, aff.manaCost, false))
            return ActionResult.resultFail(stack);
        ManaItemHandler.instance().requestManaExactForTool(stack, player, aff.manaCost, true);
        player.getCooldownTracker().setCooldown(this, 100);
        return RuneSpell.perform(world, player, stack, rune1, rune2, rune3, aff) ? ActionResult.resultSuccess(stack) : ActionResult.resultFail(stack);
    }

    private int getRune(ItemStack stack, int num) {
        if (num <= 0 || num > 3)
            throw new IllegalArgumentException("Attempted to get rune " + num + " from runic spell. Only 1 to 3 ist allowed.");
        return ItemNBTHelper.getInt(stack, "rune" + num, -1);
    }

    public static ItemStack createRunicSpell(Item rune1, Item rune2, Item rune3) {
        if (RuneSpell.getAffinities(rune1, rune2, rune3) == null) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = new ItemStack(ModItems.runicSpell);
            ItemNBTHelper.setInt(stack, "rune1", RuneSpell.getIdFromRune(rune1));
            ItemNBTHelper.setInt(stack, "rune2", RuneSpell.getIdFromRune(rune2));
            ItemNBTHelper.setInt(stack, "rune3", RuneSpell.getIdFromRune(rune3));
            return stack;
        }
    }
}
