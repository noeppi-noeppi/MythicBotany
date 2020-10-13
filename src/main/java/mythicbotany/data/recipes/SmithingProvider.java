package mythicbotany.data.recipes;

import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SmithingProvider extends RecipeProvider {

    public SmithingProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	@Nonnull
	public String getName() {
		return "MythicBotany smithing recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
    	Ingredient alfsteel = Ingredient.fromItems(ModItems.alfsteelIngot);
    	Ingredient alfsteel2 = Ingredient.fromItems(ModItems.alfsteelArmorUpgrade);

        SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terraSword), alfsteel, ModItems.alfsteelSword).func_240503_a_("alfsteel_sword_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_sword"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_sword_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terraPick), alfsteel, ModItems.alfsteelPick).func_240503_a_("alfsteel_pick_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_pick"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_pick_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terraAxe), alfsteel, ModItems.alfsteelAxe).func_240503_a_("alfsteel_axe_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_axe"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_axe_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terrasteelHelm), alfsteel2, ModItems.alfsteelHelmet).func_240503_a_("alfsteel_helmet_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_helm"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_helmet_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terrasteelChest), alfsteel2, ModItems.alfsteelChest).func_240503_a_("alfsteel_chestplate_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_chest"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_chestplate_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terrasteelLegs), alfsteel2, ModItems.alfsteelLegs).func_240503_a_("alfsteel_leggings_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_legs"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_leggings_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.terrasteelBoots), alfsteel2, ModItems.alfsteelBoots).func_240503_a_("alfsteel_boots_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "terra_boots"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_boots_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.manaRingGreater), alfsteel, ModItems.greatestManaRing).func_240503_a_("alfsteel_mana_ring_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "mana_ring_greater"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_mana_ring_smithing"));
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(vazkii.botania.common.item.ModItems.auraRingGreater), alfsteel, ModItems.greatestAuraRing).func_240503_a_("alfsteel_aura_ring_criteria", RecipeUnlockedTrigger.create(new ResourceLocation("botania", "aura_ring_greater"))).func_240505_a_(consumer, new ResourceLocation(MythicBotany.getInstance().modid, "alfsteel_aura_ring_smithing"));
    }
}
