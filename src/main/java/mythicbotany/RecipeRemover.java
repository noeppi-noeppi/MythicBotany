package mythicbotany;

import com.google.common.collect.ImmutableMap;
import mythicbotany.config.MythicConfig;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecipeRemover {

    public static final ResourceLocation GAIA_PYLON = new ResourceLocation("botania", "gaia_pylon");
    public static final ResourceLocation HARD_PYLON = new ResourceLocation(MythicBotany.getInstance().modid, "modified_gaia_pylon_with_alfsteel");

    private RecipeRemover() {

    }

    public static void removeRecipes(RecipeManager rm) {
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
        if (MythicConfig.replaceGaiaRecipe) {
            recipesToRemove.add(GAIA_PYLON);
        } else {
            recipesToRemove.add(HARD_PYLON);
        }
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, rm, "field_199522_d");
        if (recipes == null)
            return;
        @SuppressWarnings("UnstableApiUsage")
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipes = recipes.entrySet().stream().map(entry -> Pair.of(entry.getKey(), withRecipesRemoved(entry.getValue(), recipesToRemove))).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
        ObfuscationReflectionHelper.setPrivateValue(RecipeManager.class, rm, newRecipes, "field_199522_d");
    }

    private static <T> ImmutableMap<ResourceLocation, T> withRecipesRemoved(Map<ResourceLocation, T> map, Set<ResourceLocation> recipesToRemove) {
        //noinspection UnstableApiUsage
        return map.entrySet().stream().parallel().filter(entry -> !recipesToRemove.contains(entry.getKey())).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
