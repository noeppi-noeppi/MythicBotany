package mythicbotany.data.recipes;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.Property;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ManaInfusionProvider extends RecipeProvider {
	public ManaInfusionProvider(DataGenerator gen) {
		super(gen);
	}

	@Nonnull
    @Override
	public String getName() {
		return "MythicBotany mana pool recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(ModItems.gjallarHornEmpty.getRegistryName(), new ItemStack(ModItems.gjallarHornEmpty), ingr(vazkii.botania.common.item.ModItems.grassHorn), 20000));
	}

	private static ResourceLocation id(String s) {
		return new ResourceLocation(MythicBotany.getInstance().modid, "mana_infusion/" + s);
	}

	private static Ingredient ingr(IItemProvider i) {
		return Ingredient.fromItems(i);
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient input;
		private final ItemStack output;
		private final int mana;
		private final String group;
		@Nullable
		private final BlockState catalyst;

		public static FinishedRecipe conjuration(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			return new FinishedRecipe(id, output, input, mana, "", ModBlocks.conjurationCatalyst.getDefaultState());
		}

		public static FinishedRecipe alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			return alchemy(id, output, input, mana, "");
		}

		public static FinishedRecipe alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group) {
			return new FinishedRecipe(id, output, input, mana, group, ModBlocks.alchemyCatalyst.getDefaultState());
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			this(id, output, input, mana, "");
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group) {
			this(id, output, input, mana, group, null);
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group, @Nullable BlockState catalyst) {
			this.id = id;
			this.input = input;
			this.output = output;
			this.mana = mana;
			this.group = group;
			this.catalyst = catalyst;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("input", input.serialize());
			json.add("output", ItemNBTHelper.serializeStack(output));
			json.addProperty("mana", mana);
			if (!group.isEmpty()) {
				json.addProperty("group", group);
			}
			if (catalyst != null) {
				json.add("catalyst", StateIngredientHelper.serializeBlockState(catalyst));
			}
		}

		@SuppressWarnings("unchecked")
		private static <T extends Comparable<T>> String getName(Property<T> prop, Comparable<?> val) {
			return prop.getName((T) val);
		}

		@Nonnull
        @Override
		public ResourceLocation getID() {
			return id;
		}

		@Nonnull
        @Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.MANA_INFUSION_SERIALIZER;
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
