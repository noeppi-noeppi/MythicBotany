/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data.recipes;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SmeltingProvider extends RecipeProvider {

	public SmeltingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	@Nonnull
	public String getName() {
		return "MythicBotany smelting recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		/*CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModFluffBlocks.biomeCobblestoneForest), ModFluffBlocks.biomeStoneForest, 0.1f, 200)
				.addCriterion("has_item", hasItem(ModFluffBlocks.biomeCobblestoneForest))
				.build(consumer, "botania:smelting/metamorphic_forest_stone");*/
	}
}
