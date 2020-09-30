package mythicbotany.data.recipes;

import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.recipes.InfuserRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class InfuserProvider extends RecipeProvider {
    public InfuserProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Nonnull
    @Override
    public String getName() {
        return "MythicBotany infuser recipes";
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        InfuserRecipeBuilder.infuserRecipe(ModItems.terrasteel)
                .addIngredient(ModTags.Items.INGOTS_MANASTEEL)
                .addIngredient(ModItems.manaPearl)
                .addIngredient(ModTags.Items.GEMS_MANA_DIAMOND)
                .addManaCost(500_000)
                .addColors(0x0000FF, 0x00FF00)
                .setGroup("infuser")
                .addCriterion("has_item", hasItem(ModBlocks.manaInfuser))
                .build(consumer, new ResourceLocation(MythicBotany.MODID, "infuser/" + Registry.ITEM.getKey(ModItems.terrasteel).getPath()));

        InfuserRecipeBuilder.infuserRecipe(mythicbotany.ModItems.alfsteelIngot)
                .addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
                .addIngredient(ModTags.Items.GEMS_DRAGONSTONE)
                .addIngredient(ModItems.pixieDust)
                .addManaCost(2_000_000)
                .addColors(0xFF008D, 0xFF9600)
                .setGroup("infuser")
                .addCriterion("has_item", hasItem(ModBlocks.manaInfuser))
                .build(consumer, new ResourceLocation(MythicBotany.MODID, "infuser/" + Registry.ITEM.getKey(mythicbotany.ModItems.alfsteelIngot).getPath()));
    }
}
