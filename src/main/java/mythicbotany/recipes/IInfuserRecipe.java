package mythicbotany.recipes;

import mythicbotany.MythicBotany;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

public interface IInfuserRecipe extends IRecipe<IInventory> {

    ResourceLocation TYPE_ID = new ResourceLocation(MythicBotany.MODID, "infusion");

    int getManaUsage();

    int fromColor();

    int toColor();

    @Nonnull
    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.func_241873_b(TYPE_ID).get();
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
}
