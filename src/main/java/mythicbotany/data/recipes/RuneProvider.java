package mythicbotany.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RuneProvider extends RecipeProvider {
	public RuneProvider(DataGenerator gen) {
		super(gen);
	}

	@Nonnull
    @Override
	public String getName() {
		return "MythicBotany runic altar recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		final int costTier4 = 16000;

		Ingredient ingot_midgard = Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL);
		Ingredient ingot_alfheim = Ingredient.fromTag(ModTags.Items.INGOTS_ELEMENTIUM);
		Ingredient ingot_muspelheim = Ingredient.fromTag(Tags.Items.INGOTS_NETHER_BRICK);
		Ingredient ingot_niflheim = Ingredient.fromTag(Tags.Items.INGOTS_IRON);
		Ingredient ingot_asgard = Ingredient.fromItems(Items.NETHERITE_INGOT);
		Ingredient ingot_vanaheim = Ingredient.fromItems(Items.NETHERITE_INGOT);
		Ingredient ingot_helheim = Ingredient.fromTag(Tags.Items.INGOTS_GOLD);
		Ingredient ingot_nidavellir = Ingredient.fromTag(Tags.Items.INGOTS_BRICK);
		Ingredient ingot_joetunheim = Ingredient.fromTag(Tags.Items.INGOTS_BRICK);

		Ingredient fire = Ingredient.fromTag(ModTags.Items.RUNES_FIRE);
		Ingredient water = Ingredient.fromTag(ModTags.Items.RUNES_WATER);
		Ingredient earth = Ingredient.fromTag(ModTags.Items.RUNES_EARTH);
		Ingredient air = Ingredient.fromTag(ModTags.Items.RUNES_AIR);

		Ingredient spring = Ingredient.fromTag(ModTags.Items.RUNES_SPRING);
		Ingredient summer = Ingredient.fromTag(ModTags.Items.RUNES_SUMMER);
		Ingredient autumn = Ingredient.fromTag(ModTags.Items.RUNES_AUTUMN);
		Ingredient winter = Ingredient.fromTag(ModTags.Items.RUNES_WINTER);

		Ingredient lust = Ingredient.fromTag(ModTags.Items.RUNES_LUST);
		Ingredient gluttony = Ingredient.fromTag(ModTags.Items.RUNES_GLUTTONY);
		Ingredient greed = Ingredient.fromTag(ModTags.Items.RUNES_GREED);
		Ingredient sloth = Ingredient.fromTag(ModTags.Items.RUNES_SLOTH);
		Ingredient wrath = Ingredient.fromTag(ModTags.Items.RUNES_WRATH);
		Ingredient envy = Ingredient.fromTag(ModTags.Items.RUNES_ENVY);
		Ingredient pride = Ingredient.fromTag(ModTags.Items.RUNES_PRIDE);

		consumer.accept(new FinishedRecipe(idFor("midgard"), new ItemStack(ModItems.midgardRune), costTier4, ingot_midgard, earth, spring, greed, Ingredient.fromItems(Blocks.GRASS_BLOCK)));
		consumer.accept(new FinishedRecipe(idFor("alfheim"), new ItemStack(ModItems.alfheimRune), costTier4, ingot_alfheim, air, summer, lust, Ingredient.fromItems(Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES)));
		consumer.accept(new FinishedRecipe(idFor("muspelheim"), new ItemStack(ModItems.muspelheimRune), costTier4, ingot_muspelheim, fire, summer, wrath, Ingredient.fromItems(Blocks.MAGMA_BLOCK)));
		consumer.accept(new FinishedRecipe(idFor("niflheim"), new ItemStack(ModItems.niflheimRune), costTier4, ingot_niflheim, water, winter, wrath, Ingredient.fromItems(Blocks.BLUE_ICE)));
		consumer.accept(new FinishedRecipe(idFor("asgard"), new ItemStack(ModItems.asgardRune), costTier4, ingot_asgard, air, autumn, pride, Ingredient.fromItems(vazkii.botania.common.item.ModItems.rainbowRod)));
		consumer.accept(new FinishedRecipe(idFor("vanaheim"), new ItemStack(ModItems.vanaheimRune), costTier4, ingot_vanaheim, earth, spring, pride, Ingredient.fromItems(ModBlocks.alfPortal)));
		consumer.accept(new FinishedRecipe(idFor("helheim"), new ItemStack(ModItems.helheimRune), costTier4, ingot_helheim, fire, autumn, envy, Ingredient.fromItems(Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD)));
		consumer.accept(new FinishedRecipe(idFor("nidavellir"), new ItemStack(ModItems.nidavellirRune), costTier4, ingot_nidavellir, earth, winter, sloth, Ingredient.fromItems(Blocks.IRON_BLOCK)));
		consumer.accept(new FinishedRecipe(idFor("joetunheim"), new ItemStack(ModItems.joetunheimRune), costTier4, ingot_joetunheim, earth, autumn, gluttony, Ingredient.fromItems(Blocks.BLACKSTONE)));

	}

	private static ResourceLocation idFor(String s) {
		return new ResourceLocation(MythicBotany.getInstance().modid, "runic_altar/" + s);
	}

	private static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final ItemStack output;
		private final int mana;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
			this.id = id;
			this.output = output;
			this.mana = mana;
			this.inputs = inputs;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.serialize());
			}
			json.addProperty("mana", mana);
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
			return ModRecipeTypes.RUNE_SERIALIZER;
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
