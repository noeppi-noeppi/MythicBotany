/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data.recipes;

import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.wand.RecipeDreamwoodWand;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.data.recipes.WrapperResult;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RecipeProvider extends RecipeProviderBase {

    public RecipeProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        makeFloatingFlowerRecipes(consumer);

        makeBlockItemNugget(consumer, ModBlocks.alfsteelBlock, ModItems.alfsteelIngot, ModItems.alfsteelNugget);
        makeRing(consumer, ModItems.fireRing, vazkii.botania.common.item.ModItems.elementium, ModItems.muspelheimRune);
        makeRing(consumer, ModItems.iceRing, vazkii.botania.common.item.ModItems.elementium, ModItems.niflheimRune);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaInfuser)
                .key('e', ModTags.Items.INGOTS_ELEMENTIUM)
                .key('d', vazkii.botania.common.block.ModBlocks.dreamwoodGlimmering)
                .key('a', ModItems.asgardRune)
                .key('w', ModTags.Items.RUNES_SPRING)
                .key('x', ModTags.Items.RUNES_SUMMER)
                .key('y', ModTags.Items.RUNES_AUTUMN)
                .key('z', ModTags.Items.RUNES_WINTER)
                .patternLine("eee")
                .patternLine("wdz")
                .patternLine("xay")
                .setGroup(MythicBotany.getInstance().modid + ":infuser")
                .addCriterion("has_item", hasItem(ModItems.asgardRune))
                .build(consumer);

        ShapelessRecipeBuilder.shapelessRecipe(ModItems.alfsteelArmorUpgrade)
                .addIngredient(ModItems.alfsteelIngot, 2)
                .addIngredient(ModTags.Items.DUSTS_MANA)
                .setGroup(ModItems.alfsteelArmorUpgrade.getRegistryName().toString())
                .addCriterion("has_item", hasItem(ModItems.alfsteelIngot))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.alfsteelPylon)
                .key('n', ModItems.alfsteelNugget)
                .key('g', Items.GHAST_TEAR)
                .key('p', vazkii.botania.common.block.ModBlocks.naturaPylon)
                .patternLine(" n ")
                .patternLine("npn")
                .patternLine(" g ")
                .setGroup(MythicBotany.getInstance().modid + ":alfsteel_pylon")
                .addCriterion("has_item", hasItem(vazkii.botania.common.block.ModBlocks.naturaPylon))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(vazkii.botania.common.block.ModBlocks.gaiaPylon)
                .key('d', vazkii.botania.common.item.ModItems.pixieDust)
                .key('e', ModTags.Items.INGOTS_ELEMENTIUM)
                .key('p', ModBlocks.alfsteelPylon)
                .patternLine(" d ")
                .patternLine("epe")
                .patternLine(" d ")
                .setGroup(MythicBotany.getInstance().modid + ":modified_gaia_pylon_with_alfsteel")
                .addCriterion("has_item", hasItem(ModBlocks.alfsteelPylon))
                .build(consumer, MythicBotany.getInstance().modid + ":modified_gaia_pylon_with_alfsteel");

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.manaCollector)
                .key('d', vazkii.botania.common.block.ModBlocks.dreamwoodGlimmering)
                .key('g', vazkii.botania.common.item.ModItems.gaiaIngot)
                .key('p', vazkii.botania.common.item.ModItems.pixieDust)
                .key('m', ModItems.vanaheimRune)
                .patternLine("dgd")
                .patternLine("dpd")
                .patternLine("dmd")
                .setGroup(MythicBotany.getInstance().modid + ":mana_collector")
                .addCriterion("has_item", hasItem(vazkii.botania.common.item.ModItems.gaiaIngot))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(ModItems.dreamwoodWand)
                .key('p', ModTags.Items.PETALS)
                .key('t', vazkii.botania.common.item.ModItems.dreamwoodTwig)
                .patternLine(" pt")
                .patternLine(" tp")
                .patternLine("t  ")
                .setGroup(ModItems.dreamwoodWand.getRegistryName().toString())
                .addCriterion("has_item", hasItem(ModTags.Items.PETALS))
                .build(WrapperResult.ofType(RecipeDreamwoodWand.SERIALIZER, consumer));
    }

    private void makeRing(Consumer<IFinishedRecipe> consumer, IItemProvider ring, IItemProvider material, IItemProvider gem) {
        //noinspection ConstantConditions
        ShapedRecipeBuilder.shapedRecipe(ring)
                .key('m', material)
                .key('g', gem)
                .patternLine("gm ")
                .patternLine("m m")
                .patternLine(" m ")
                .setGroup(ring.asItem().getRegistryName().toString())
                .addCriterion("has_item", hasItem(gem))
                .build(consumer);
    }

    private void makeFloatingFlowerRecipes(Consumer<IFinishedRecipe> consumer) {
        //noinspection deprecation
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(i -> MythicBotany.getInstance().modid.equals(Registry.ITEM.getKey(i).getNamespace()))
                .filter(i -> i instanceof BlockItem)
                .filter(i -> ((BlockItem) i).getBlock() instanceof BlockFloatingFunctionalFlower<?>)
                .forEach(i -> {
                    //noinspection ConstantConditions
                    ShapelessRecipeBuilder.shapelessRecipe(i)
                            .addIngredient(ModTags.Items.FLOATING_FLOWERS)
                            .addIngredient(((BlockFloatingFunctionalFlower<?>) ((BlockItem) i).getBlock()).getNonFloatingBlock())
                            .setGroup(i.getRegistryName().toString())
                            .addCriterion("has_item", hasItem(((BlockFloatingFunctionalFlower<?>) ((BlockItem) i).getBlock()).getNonFloatingBlock()))
                            .build(consumer);
                });
    }
}
