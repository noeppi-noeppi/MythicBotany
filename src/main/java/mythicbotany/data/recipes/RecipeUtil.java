package mythicbotany.data.recipes;

import mythicbotany.EmptyRecipe;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.TrueCondition;

import java.util.function.Consumer;

public class RecipeUtil {
    
    public static Consumer<IFinishedRecipe> forMod(String modid, Consumer<IFinishedRecipe> consumer) {
        return recipe -> ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(modid))
                .addRecipe(recipe)
                .addCondition(TrueCondition.INSTANCE)
                .addRecipe(EmptyRecipe.empty(recipe.getID()))
                .build(consumer, recipe.getID());
    }
}
