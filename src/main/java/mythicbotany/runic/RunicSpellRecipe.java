package mythicbotany.runic;

import com.google.common.collect.ImmutableList;
import mythicbotany.ModItems;
import mythicbotany.infuser.InfuserRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.stream.Collectors;

public class RunicSpellRecipe extends InfuserRecipe {

    public RunicSpellRecipe(int manaCost, int fromColor, int toColor) {
        super(manaCost, fromColor, toColor);
    }

    @Override
    public ItemStack result(List<ItemStack> inputs) {
        if (inputs.size() != 4)
            return ItemStack.EMPTY;
        if (inputs.stream().noneMatch(stack -> stack.getItem() == Items.PAPER && stack.getCount() == 1))
            return ItemStack.EMPTY;
        List<Item> runes = inputs.stream().filter(stack -> RuneSpell.RUNE_IDS.contains(stack.getItem()) && stack.getCount() == 1).map(ItemStack::getItem).collect(Collectors.toList());
        if (runes.size() != 3)
            return ItemStack.EMPTY;
        return ItemRunicSpell.createRunicSpell(runes.get(0), runes.get(1), runes.get(2));
    }

    @Override
    public List<List<ItemStack>> displayIngredients() {
        List<ItemStack> runeStacks = RuneSpell.RUNE_IDS.stream().map(ItemStack::new).collect(Collectors.toList());
        return ImmutableList.of(
                ImmutableList.of(new ItemStack(Items.PAPER)),
                runeStacks,
                runeStacks,
                runeStacks
        );
    }

    @Override
    public List<ItemStack> displayResult() {
        return ImmutableList.of(new ItemStack(ModItems.runicSpell));
    }
}
