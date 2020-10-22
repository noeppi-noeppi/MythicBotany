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
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ElvenTradeProvider extends RecipeProvider {
	public ElvenTradeProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "MysticBotany elven trade recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		//Ingredient livingwood = Ingredient.fromTag(ModTags.Items.LIVINGWOOD);
		//consumer.accept(new FinishedRecipe(id("dreamwood"), new ItemStack(ModBlocks.dreamwood), livingwood));
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(MythicBotany.getInstance().modid, "elven_trade/" + path);
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final List<Ingredient> inputs;
		private final List<ItemStack> outputs;

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this(id, Arrays.asList(inputs), Collections.singletonList(output));
		}

		private FinishedRecipe(ResourceLocation id, List<Ingredient> inputs, List<ItemStack> outputs) {
			this.id = id;
			this.inputs = inputs;
			this.outputs = outputs;
		}

		@Override
		public void serialize(JsonObject json) {
			JsonArray in = new JsonArray();
			for (Ingredient ingr : inputs) {
				in.add(ingr.serialize());
			}

			JsonArray out = new JsonArray();
			for (ItemStack s : outputs) {
				out.add(ItemNBTHelper.serializeStack(s));
			}

			json.add("ingredients", in);
			json.add("output", out);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.ELVEN_TRADE_SERIALIZER;
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
