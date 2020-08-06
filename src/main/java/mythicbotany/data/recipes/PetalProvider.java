/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.MythicBotany;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PetalProvider extends RecipeProvider {
	public PetalProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "MythicBotany petal apothecary recipes";
	}

	@Override
	@SuppressWarnings("unused")
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		Ingredient white = tagIngr("petals/white");
		Ingredient orange = tagIngr("petals/orange");
		Ingredient magenta = tagIngr("petals/magenta");
		Ingredient lightBlue = tagIngr("petals/light_blue");
		Ingredient yellow = tagIngr("petals/yellow");
		Ingredient lime = tagIngr("petals/lime");
		Ingredient pink = tagIngr("petals/pink");
		Ingredient gray = tagIngr("petals/gray");
		Ingredient lightGray = tagIngr("petals/light_gray");
		Ingredient cyan = tagIngr("petals/cyan");
		Ingredient purple = tagIngr("petals/purple");
		Ingredient blue = tagIngr("petals/blue");
		Ingredient brown = tagIngr("petals/brown");
		Ingredient green = tagIngr("petals/green");
		Ingredient red = tagIngr("petals/red");
		Ingredient black = tagIngr("petals/black");
		Ingredient runeWater = tagIngr("runes/water");
		Ingredient runeFire = tagIngr("runes/fire");
		Ingredient runeEarth = tagIngr("runes/earth");
		Ingredient runeAir = tagIngr("runes/air");
		Ingredient runeSpring = tagIngr("runes/spring");
		Ingredient runeSummer = tagIngr("runes/summer");
		Ingredient runeAutumn = tagIngr("runes/autumn");
		Ingredient runeWinter = tagIngr("runes/winter");
		Ingredient runeMana = tagIngr("runes/mana");
		Ingredient runeLust = tagIngr("runes/lust");
		Ingredient runeGluttony = tagIngr("runes/gluttony");
		Ingredient runeGreed = tagIngr("runes/greed");
		Ingredient runeSloth = tagIngr("runes/sloth");
		Ingredient runeWrath = tagIngr("runes/wrath");
		Ingredient runeEnvy = tagIngr("runes/envy");
		Ingredient runePride = tagIngr("runes/pride");

		Ingredient redstoneRoot = Ingredient.fromItems(ModItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.fromItems(ModItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.fromItems(ModItems.lifeEssence);

		//consumer.accept(make(ModSubtiles.pureDaisy, white, white, white, white));
	}

	private static Ingredient tagIngr(String tag) {
		return Ingredient.fromTag(ItemTags.makeWrapperTag(new ResourceLocation(MythicBotany.MODID, tag).toString()));
	}

	private static FinishedRecipe make(IItemProvider item, Ingredient... ingredients) {
		return new FinishedRecipe(idFor(Registry.ITEM.getKey(item.asItem())), new ItemStack(item), ingredients);
	}

	private static ResourceLocation idFor(ResourceLocation name) {
		return new ResourceLocation(name.getNamespace(), "petal_apothecary/" + name.getPath());
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final ItemStack output;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this.id = id;
			this.output = output;
			this.inputs = inputs;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.serialize());
			}
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.PETAL_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
