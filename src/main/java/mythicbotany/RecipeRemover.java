package mythicbotany;

import com.google.common.collect.ImmutableMap;
import mythicbotany.config.MythicConfig;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecipeRemover {

    public static final ResourceLocation GAIA_PYLON = new ResourceLocation("botania", "gaia_pylon");
    public static final ResourceLocation HARD_PYLON = MythicBotany.getInstance().resource("gaia_pylon");

    private RecipeRemover() {

    }

    public static void removeRecipes(RecipeManager rm) {
        Set<ResourceLocation> recipesToRemove = new HashSet<>();
        if (MythicConfig.replaceGaiaRecipe) {
            recipesToRemove.add(GAIA_PYLON);
        } else {
            recipesToRemove.add(HARD_PYLON);
        }
        
        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, rm, "f_44007_");
        if (recipes != null) {
            Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> newRecipes = recipes.entrySet().stream().map(entry -> Pair.of(entry.getKey(), withRecipesRemoved(entry.getValue(), recipesToRemove))).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
            ObfuscationReflectionHelper.setPrivateValue(RecipeManager.class, rm, newRecipes, "f_44007_");
        }

        Map<ResourceLocation, Recipe<?>> byIdMap = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, rm, "f_199900_");
        if (byIdMap != null) {
            Map<ResourceLocation, Recipe<?>> newByIdMap = withRecipesRemoved(byIdMap, recipesToRemove);
            ObfuscationReflectionHelper.setPrivateValue(RecipeManager.class, rm, newByIdMap, "f_199900_");
        }
    }

    private static <T> ImmutableMap<ResourceLocation, T> withRecipesRemoved(Map<ResourceLocation, T> map, Set<ResourceLocation> recipesToRemove) {
        return map.entrySet().stream().parallel().filter(entry -> !recipesToRemove.contains(entry.getKey())).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
