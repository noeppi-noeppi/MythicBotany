package mythicbotany.data.recipes.extension;

import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeExtension;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vazkii.botania.data.recipes.RuneProvider;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public interface RuneExtension extends RecipeExtension {

    default void runeAltar(ItemLike output, int mana, ItemLike... inputs) {
        this.runeAltar(new ItemStack(output), mana, inputs);
    }

    default void runeAltar(ItemLike output, int mana, Ingredient... inputs) {
        this.runeAltar(new ItemStack(output), mana, inputs);
    }

    default void runeAltar(ItemStack output, int mana, ItemLike... inputs) {
        this.runeAltar(output, mana, Arrays.stream(inputs).map(Ingredient::of).toArray(Ingredient[]::new));
    }

    default void runeAltar(ItemStack output, int mana, Ingredient... inputs) {
        this.consumer().accept(Wrapper.create(this.provider().loc(output.getItem(), "by_elven_trade"), output, mana, inputs));
    }

    class Wrapper extends RuneProvider {

        public Wrapper(DataGenerator gen) {
            super(gen);
        }

        private static FinishedRecipe create(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
            try {
                Constructor<FinishedRecipe> ctor = FinishedRecipe.class.getDeclaredConstructor(ResourceLocation.class, ItemStack.class, int.class, Ingredient[].class);
                ctor.setAccessible(true);
                return ctor.newInstance(id, output, mana, inputs);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
