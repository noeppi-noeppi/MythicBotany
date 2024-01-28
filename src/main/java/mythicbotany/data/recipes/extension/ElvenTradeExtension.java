package mythicbotany.data.recipes.extension;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.moddingx.libx.datagen.provider.recipe.RecipeExtension;
import vazkii.botania.data.recipes.ElvenTradeProvider;

import java.util.Arrays;

public interface ElvenTradeExtension extends RecipeExtension {

    default void elvenTrade(ItemLike output, ItemLike... inputs) {
        this.elvenTrade(new ItemStack(output), inputs);
    }

    default void elvenTrade(ItemLike output, Ingredient... inputs) {
        this.elvenTrade(new ItemStack(output), inputs);
    }
    
    default void elvenTrade(ItemStack output, ItemLike... inputs) {
        this.elvenTrade(output, Arrays.stream(inputs).map(Ingredient::of).toArray(Ingredient[]::new));
    }

    default void elvenTrade(ItemStack output, Ingredient... inputs) {
        this.consumer().accept(Wrapper.create(this.provider().loc(output.getItem(), "elven_trade"), output, inputs));
    }
    
    class Wrapper extends ElvenTradeProvider {
        
        public Wrapper(PackOutput packOutput) {
            super(packOutput);
        }

        private static FinishedElvenRecipe create(ResourceLocation id, ItemStack output, Ingredient... inputs) {
            return new FinishedElvenRecipe(id, output, inputs);
        }
    }
}
