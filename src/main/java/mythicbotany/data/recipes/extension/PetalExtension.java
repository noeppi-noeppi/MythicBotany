package mythicbotany.data.recipes.extension;

import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeExtension;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.data.recipes.ElvenTradeProvider;
import vazkii.botania.data.recipes.PetalProvider;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public interface PetalExtension extends RecipeExtension {

    default Ingredient petal(DyeColor color) {
        return Ingredient.of(switch (color) {
            case WHITE -> ModTags.Items.PETALS_WHITE;
            case ORANGE -> ModTags.Items.PETALS_ORANGE;
            case MAGENTA -> ModTags.Items.PETALS_MAGENTA;
            case LIGHT_BLUE -> ModTags.Items.PETALS_LIGHT_BLUE;
            case YELLOW -> ModTags.Items.PETALS_YELLOW;
            case LIME -> ModTags.Items.PETALS_LIME;
            case PINK -> ModTags.Items.PETALS_PINK;
            case GRAY -> ModTags.Items.PETALS_GRAY;
            case LIGHT_GRAY -> ModTags.Items.PETALS_LIGHT_GRAY;
            case CYAN -> ModTags.Items.PETALS_CYAN;
            case PURPLE -> ModTags.Items.PETALS_PURPLE;
            case BLUE -> ModTags.Items.PETALS_BLUE;
            case BROWN -> ModTags.Items.PETALS_BROWN;
            case GREEN -> ModTags.Items.PETALS_GREEN;
            case RED -> ModTags.Items.PETALS_RED;
            case BLACK -> ModTags.Items.PETALS_BLACK;
        });
    }
    
    default void petalApothecary(ItemLike output, ItemLike... inputs) {
        petalApothecary(new ItemStack(output), inputs);
    }

    default void petalApothecary(ItemLike output, Ingredient... inputs) {
        petalApothecary(new ItemStack(output), inputs);
    }
    
    default void petalApothecary(ItemStack output, ItemLike... inputs) {
        petalApothecary(output, Arrays.stream(inputs).map(Ingredient::of).toArray(Ingredient[]::new));
    }

    default void petalApothecary(ItemStack output, Ingredient... inputs) {
        this.consumer().accept(Wrapper.create(this.provider().loc(output.getItem(), "by_elven_trade"), output, inputs));
    }
    
    class Wrapper extends PetalProvider {

        public Wrapper(DataGenerator gen) {
            super(gen);
        }
        
        private static FinishedRecipe create(ResourceLocation id, ItemStack output, Ingredient... inputs) {
            try {
                Constructor<FinishedRecipe> ctor = FinishedRecipe.class.getDeclaredConstructor(ResourceLocation.class, ItemStack.class, Ingredient[].class);
                ctor.setAccessible(true);
                return ctor.newInstance(id, output, inputs);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
