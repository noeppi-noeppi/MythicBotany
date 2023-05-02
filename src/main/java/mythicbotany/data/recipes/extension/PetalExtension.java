package mythicbotany.data.recipes.extension;

import org.moddingx.libx.datagen.provider.recipe.RecipeExtension;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.recipes.PetalApothecaryProvider;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public interface PetalExtension extends RecipeExtension {

    default Ingredient petal(DyeColor color) {
        return Ingredient.of(switch (color) {
            case WHITE -> BotaniaTags.Items.PETALS_WHITE;
            case ORANGE -> BotaniaTags.Items.PETALS_ORANGE;
            case MAGENTA -> BotaniaTags.Items.PETALS_MAGENTA;
            case LIGHT_BLUE -> BotaniaTags.Items.PETALS_LIGHT_BLUE;
            case YELLOW -> BotaniaTags.Items.PETALS_YELLOW;
            case LIME -> BotaniaTags.Items.PETALS_LIME;
            case PINK -> BotaniaTags.Items.PETALS_PINK;
            case GRAY -> BotaniaTags.Items.PETALS_GRAY;
            case LIGHT_GRAY -> BotaniaTags.Items.PETALS_LIGHT_GRAY;
            case CYAN -> BotaniaTags.Items.PETALS_CYAN;
            case PURPLE -> BotaniaTags.Items.PETALS_PURPLE;
            case BLUE -> BotaniaTags.Items.PETALS_BLUE;
            case BROWN -> BotaniaTags.Items.PETALS_BROWN;
            case GREEN -> BotaniaTags.Items.PETALS_GREEN;
            case RED -> BotaniaTags.Items.PETALS_RED;
            case BLACK -> BotaniaTags.Items.PETALS_BLACK;
        });
    }
    
    default void petalApothecary(ItemLike output, ItemLike... inputs) {
        this.petalApothecary(new ItemStack(output), inputs);
    }

    default void petalApothecary(ItemLike output, Ingredient... inputs) {
        this.petalApothecary(new ItemStack(output), inputs);
    }
    
    default void petalApothecary(ItemStack output, ItemLike... inputs) {
        this.petalApothecary(output, Arrays.stream(inputs).map(Ingredient::of).toArray(Ingredient[]::new));
    }

    default void petalApothecary(ItemStack output, Ingredient... inputs) {
        this.consumer().accept(Wrapper.create(this.provider().loc(output.getItem(), "petal_apothecary"), output, inputs));
    }
    
    class Wrapper extends PetalApothecaryProvider {

        public Wrapper(DataGenerator gen) {
            super(gen);
        }
        
        private static FinishedRecipe create(ResourceLocation id, ItemStack output, Ingredient... inputs) {
            try {
                Constructor<FinishedRecipe> ctor = FinishedRecipe.class.getDeclaredConstructor(ResourceLocation.class, ItemStack.class, Ingredient.class, Ingredient[].class);
                ctor.setAccessible(true);
                return ctor.newInstance(id, output, Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT), inputs);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
