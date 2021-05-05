package mythicbotany.data.recipes;

import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModItems;
import mythicbotany.data.recipes.builder.RuneRitualRecipeBuilder;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.mjoellnir.MjoellnirRuneOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RitualProvider extends RecipeProviderBase {

    public RitualProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        
        RuneRitualRecipeBuilder.runeRitual(ModTags.Items.RUNES_MANA)
                .rune4(ModTags.Items.RUNES_PRIDE, -2, -2)
                .rune2(ModItems.alfheimRune, 3, 0)
                .rune2(ModTags.Items.RUNES_WATER, 0, 3)
                .input(Ingredient.fromStacks(new ItemStack(ModItems.cursedAndwariRing), new ItemStack(ModItems.andwariRing)))
                .input(vazkii.botania.common.item.ModItems.manaweaveCloth)
                .output(new ItemStack(ModItems.andwariRing))
                .build(consumer);
        
        RuneRitualRecipeBuilder.runeRitual(ModItems.fimbultyrTablet)
                .rune4(ModTags.Items.RUNES_WRATH, 5, 0)
                .rune4(ModTags.Items.RUNES_GREED, 4, 4)
                .rune(ModTags.Items.RUNES_AIR, -3, 2)
                .rune(ModTags.Items.RUNES_AIR, 3, 2)
                .rune(ModTags.Items.RUNES_AIR, -2, 3)
                .rune(ModTags.Items.RUNES_AIR, 2, 3)
                .rune(ModTags.Items.RUNES_EARTH, -3, -2)
                .rune(ModTags.Items.RUNES_EARTH, 3, -2)
                .rune(ModTags.Items.RUNES_EARTH, -2, -3)
                .rune(ModTags.Items.RUNES_EARTH, 2, -3)
                .rune2(ModItems.nidavellirRune, 2, 0)
                .rune(ModItems.asgardRune, 0, 2)
                .rune(ModItems.joetunheimRune, 0, -2)
                .input(Items.POLISHED_ANDESITE)
                .input(Tags.Items.INGOTS_GOLD)
                .input(vazkii.botania.common.item.ModItems.goldenSeeds)
                .input(vazkii.botania.common.item.ModItems.redString)
                .input(vazkii.botania.common.item.ModItems.tinyPlanet)
                .special(MjoellnirRuneOutput.INSTANCE)
                .mana(500000)
                .build(consumer);
        
        RuneRitualRecipeBuilder.runeRitual(ModItems.fimbultyrTablet)
                .rune2(ModItems.midgardRune, 2, 2)
                .rune2(ModItems.helheimRune, -2, 2)
                .rune2(ModTags.Items.RUNES_SUMMER, 1, 3)
                .rune2(ModTags.Items.RUNES_SUMMER, 3, 1)
                .rune2(ModTags.Items.RUNES_FIRE, -1, 3)
                .rune2(ModTags.Items.RUNES_FIRE, -3, 1)
                .input(vazkii.botania.common.item.ModItems.enderDagger)
                .input(ModItems.alfsteelNugget)
                .input(vazkii.botania.common.item.ModItems.vial)
                .special(WanderingTraderRuneInput.INSTANCE)
                .output(ModItems.kvasirBlood)
                .mana(20000)
                .build(consumer);
    }
}
