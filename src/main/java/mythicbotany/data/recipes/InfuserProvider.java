package mythicbotany.data.recipes;

import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.data.recipes.builder.InfuserRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.util.ResourceLocation;
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
        //noinspection ConstantConditions
        InfuserRecipeBuilder.infuserRecipe(vazkii.botania.common.item.ModItems.terrasteel)
                .addIngredient(ModTags.Items.INGOTS_MANASTEEL)
                .addIngredient(vazkii.botania.common.item.ModItems.manaPearl)
                .addIngredient(ModTags.Items.GEMS_MANA_DIAMOND)
                .setManaCost(500000)
                .setColors(0x0000FF, 0x00FF00)
                .build(consumer, new ResourceLocation(MythicBotany.MODID, vazkii.botania.common.item.ModItems.terrasteel.getRegistryName().getPath()));

        InfuserRecipeBuilder.infuserRecipe(ModItems.alfsteelIngot)
                .addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
                .addIngredient(ModTags.Items.GEMS_DRAGONSTONE)
                .addIngredient(vazkii.botania.common.item.ModItems.pixieDust)
                .setManaCost(2000000)
                .setColors(0xFF008D, 0xFF9600)
                .build(consumer);
    }
}
