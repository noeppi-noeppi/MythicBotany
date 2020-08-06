package mythicbotany;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Set;

public class RecipeRemover {

    public static final Set<ResourceLocation> RECIPES_TO_REMOVE = ImmutableSet.of(
            new ResourceLocation("botania", "gaia_pylon")
    );

    private RecipeRemover() {

    }

    public static void removeRecipes(RecipeManager rm) {
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, rm, "field_199522_d");
        if (recipes == null)
            return;
        @SuppressWarnings("UnstableApiUsage")
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipes = recipes.entrySet().stream().map(entry -> Pair.of(entry.getKey(), withRecipesRemove(entry.getValue()))).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
        ObfuscationReflectionHelper.setPrivateValue(RecipeManager.class, rm, newRecipes, "field_199522_d");
    }

    private static <T> ImmutableMap<ResourceLocation, T> withRecipesRemove(Map<ResourceLocation, T> map) {
        //noinspection UnstableApiUsage
        return map.entrySet().stream().parallel().filter(entry -> {System.out.println(entry.getKey());return !RECIPES_TO_REMOVE.contains(entry.getKey());}).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
