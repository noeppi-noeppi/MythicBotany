package mythicbotany.infuser;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class InfuserRecipe {

    private static final List<InfuserRecipe> recipes = new LinkedList<>();

    public static void add(InfuserRecipe recipe) {
        recipes.add(recipe);
    }

    public static void add(ItemStack output, int manaCost, int fromColor, int toColor, ItemStack... items) {
        recipes.add(new SimpleInfuseRecipe(output, manaCost, fromColor, toColor, items));
    }

    public static List<InfuserRecipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    @Nullable
    public static Pair<InfuserRecipe, ItemStack> getOutput(List<ItemStack> inputs) {
        for (InfuserRecipe recipe: recipes) {
            ItemStack stack = recipe.result(inputs);
            if (!stack.isEmpty())
                return Pair.of(recipe, stack);
        }
        return null;
    }

    public final int manaCost;
    public final int fromColor;
    public final int toColor;

    protected InfuserRecipe(int manaCost, int fromColor, int toColor) {
        this.manaCost = manaCost;
        this.fromColor = fromColor;
        this.toColor = toColor;
    }

    public abstract ItemStack result(List<ItemStack> inputs);

    public abstract List<List<ItemStack>> displayIngredients();
    public abstract List<ItemStack> displayResult();

    private static class SimpleInfuseRecipe extends InfuserRecipe {

        private final ItemStack output;
        private final List<ItemStack> items;

        protected SimpleInfuseRecipe(ItemStack output, int manaCost, int fromColor, int toColor, ItemStack[] items) {
            super(manaCost, fromColor, toColor);
            this.output = output.copy();
            this.items = Arrays.stream(items).map(ItemStack::copy).collect(Collectors.toList());
            Set<Item> differentItems = Arrays.stream(items).map(ItemStack::getItem).collect(Collectors.toSet());
            if (differentItems.size() != this.items.size()) {
                throw new IllegalArgumentException("Only one ingredient of one item type can be given to SimpleInfuseRecipe");
            }
        }

        @Override
        public ItemStack result(List<ItemStack> inputs) {
            if (inputs.size() != items.size())
                return ItemStack.EMPTY;
            outer: for (ItemStack item : items) {
                for (ItemStack stack : inputs) {
                    if (item.getItem() == stack.getItem() && item.getCount() == stack.getCount())
                        continue outer;
                }
                return ItemStack.EMPTY;
            }
            return output.copy();
        }

        @Override
        public List<List<ItemStack>> displayIngredients() {
            //noinspection UnstableApiUsage
            return items.stream().map(stack -> ImmutableList.of(stack.copy())).collect(ImmutableList.toImmutableList());
        }

        @Override
        public List<ItemStack> displayResult() {
            return ImmutableList.of(output.copy());
        }
    }
}
