package mythicbotany.infuser;

import mythicbotany.ModRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;
import java.util.List;

public interface IInfuserRecipe extends Recipe<Container> {

    int getManaUsage();

    int fromColor();

    int toColor();

    @Nonnull
    @Override
    default RecipeType<?> getType() {
        return ModRecipes.INFUSER;
    }

    @Nonnull
    @Override
    default ItemStack assemble(@Nonnull Container inv) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    ItemStack result(List<ItemStack> inputs);
}
