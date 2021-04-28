package mythicbotany.data.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ElvenTradeProvider extends RecipeProvider {
	public ElvenTradeProvider(DataGenerator gen) {
		super(gen);
	}

	@Nonnull
    @Override
	public String getName() {
		return "MysticBotany elven trade recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		consumer.accept(trade(ItemTags.LEAVES, ModBlocks.dreamwoodLeaves));
	}

	private FinishedRecipe trade(IItemProvider input, IItemProvider output) {
		return trade(input, new ItemStack(output));
	}

	private FinishedRecipe trade(ITag<Item> input, IItemProvider output) {
		return trade(input, new ItemStack(output));
	}

	private FinishedRecipe trade(Ingredient input, IItemProvider output) {
		return trade(input, new ItemStack(output));
	}
	
	private FinishedRecipe trade(IItemProvider input, ItemStack output) {
		return trade(Ingredient.fromItems(input), output);
	}
	
	private FinishedRecipe trade(ITag<Item> input, ItemStack output) {
		return trade(Ingredient.fromTag(input), output);
	}
	
	private FinishedRecipe trade(Ingredient input, ItemStack output) {
		return trade(ImmutableList.of(input), output);
	}
	
	private FinishedRecipe trade(List<Ingredient> inputs, ItemStack output) {
		ResourceLocation rl = Objects.requireNonNull(output.getItem().getRegistryName());
		return new FinishedRecipe(new ResourceLocation(rl.getNamespace(), "elven_trade/" + rl.getPath()), inputs, ImmutableList.of(output));
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
		public void serialize(@Nonnull JsonObject json) {
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

		@Nonnull
        @Override
		public ResourceLocation getID() {
			return id;
		}

		@Nonnull
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
