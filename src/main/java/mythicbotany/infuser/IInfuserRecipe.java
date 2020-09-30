package mythicbotany.infuser;

import mythicbotany.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nonnull;
import java.util.List;

public interface IInfuserRecipe extends IRecipe<IInventory> {

    int getManaUsage();

    int fromColor();

    int toColor();

    @Nonnull
    @Override
    default IRecipeType<?> getType() {
        return ModRecipes.INFUSER;
    }

    @Nonnull
    @Override
    default ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

    ItemStack result(List<ItemStack> inputs);
}
