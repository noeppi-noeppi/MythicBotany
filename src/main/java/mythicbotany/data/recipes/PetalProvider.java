package mythicbotany.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PetalProvider extends RecipeProvider {
	public PetalProvider(DataGenerator gen) {
		super(gen);
	}

	@Nonnull
	@Override
	public String getName() {
		return "MythicBotany petal apothecary recipes";
	}

	@Override
	@SuppressWarnings("unused")
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		Ingredient white = tagIngrBotania("petals/white");
		Ingredient orange = tagIngrBotania("petals/orange");
		Ingredient magenta = tagIngrBotania("petals/magenta");
		Ingredient lightBlue = tagIngrBotania("petals/light_blue");
		Ingredient yellow = tagIngrBotania("petals/yellow");
		Ingredient lime = tagIngrBotania("petals/lime");
		Ingredient pink = tagIngrBotania("petals/pink");
		Ingredient gray = tagIngrBotania("petals/gray");
		Ingredient lightGray = tagIngrBotania("petals/light_gray");
		Ingredient cyan = tagIngrBotania("petals/cyan");
		Ingredient purple = tagIngrBotania("petals/purple");
		Ingredient blue = tagIngrBotania("petals/blue");
		Ingredient brown = tagIngrBotania("petals/brown");
		Ingredient green = tagIngrBotania("petals/green");
		Ingredient red = tagIngrBotania("petals/red");
		Ingredient black = tagIngrBotania("petals/black");
		Ingredient runeWater = tagIngrBotania("runes/water");
		Ingredient runeFire = tagIngrBotania("runes/fire");
		Ingredient runeEarth = tagIngrBotania("runes/earth");
		Ingredient runeAir = tagIngrBotania("runes/air");
		Ingredient runeSpring = tagIngrBotania("runes/spring");
		Ingredient runeSummer = tagIngrBotania("runes/summer");
		Ingredient runeAutumn = tagIngrBotania("runes/autumn");
		Ingredient runeWinter = tagIngrBotania("runes/winter");
		Ingredient runeMana = tagIngrBotania("runes/mana");
		Ingredient runeLust = tagIngrBotania("runes/lust");
		Ingredient runeGluttony = tagIngrBotania("runes/gluttony");
		Ingredient runeGreed = tagIngrBotania("runes/greed");
		Ingredient runeSloth = tagIngrBotania("runes/sloth");
		Ingredient runeWrath = tagIngrBotania("runes/wrath");
		Ingredient runeEnvy = tagIngrBotania("runes/envy");
		Ingredient runePride = tagIngrBotania("runes/pride");
		Ingredient runeAsgard = Ingredient.fromItems(mythicbotany.ModItems.asgardRune);
		Ingredient runeVanaheim = Ingredient.fromItems(mythicbotany.ModItems.vanaheimRune);
		Ingredient runeAlfheim = Ingredient.fromItems(mythicbotany.ModItems.alfheimRune);
		Ingredient runeMidgard = Ingredient.fromItems(mythicbotany.ModItems.midgardRune);
		Ingredient runeJoetunheim = Ingredient.fromItems(mythicbotany.ModItems.joetunheimRune);
		Ingredient runeMuspelheim = Ingredient.fromItems(mythicbotany.ModItems.muspelheimRune);
		Ingredient runeNiflheim = Ingredient.fromItems(mythicbotany.ModItems.niflheimRune);
		Ingredient runeNidavellir = Ingredient.fromItems(mythicbotany.ModItems.nidavellirRune);
		Ingredient runeHelheim = Ingredient.fromItems(mythicbotany.ModItems.helheimRune);

		Ingredient redstoneRoot = Ingredient.fromItems(ModItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.fromItems(ModItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.fromItems(ModItems.lifeEssence);

		consumer.accept(make(ModBlocks.exoblaze, yellow, yellow, gray, lightGray, runeFire, Ingredient.fromItems(Items.BLAZE_POWDER)));
		consumer.accept(make(ModBlocks.witherAconite, black, black, runePride, Ingredient.fromItems(Blocks.WITHER_ROSE)));
		consumer.accept(make(ModBlocks.aquapanthus, blue, blue, lightBlue, green, cyan));
		consumer.accept(make(ModBlocks.hellebore, red, red, purple, cyan, runeFire));
		consumer.accept(make(ModBlocks.raindeletia, lightBlue, blue, magenta, white, runeWater));
	}

	private static Ingredient tagIngrBotania(String tag) {
		return Ingredient.fromTag(ItemTags.makeWrapperTag(new ResourceLocation("botania", tag).toString()));
	}

	private static FinishedRecipe make(IItemProvider item, Ingredient... ingredients) {
		//noinspection deprecation
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

		@Nonnull
		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Nonnull
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
