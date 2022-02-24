package mythicbotany.data.recipes;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.recipe.RecipeProviderBase;
import io.github.noeppi_noeppi.libx.data.provider.recipe.SmeltingExtension;
import io.github.noeppi_noeppi.libx.data.provider.recipe.SmithingExtension;
import io.github.noeppi_noeppi.libx.data.provider.recipe.crafting.CompressionExtension;
import io.github.noeppi_noeppi.libx.data.provider.recipe.crafting.CraftingExtension;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.data.recipes.extension.*;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.mjoellnir.MjoellnirRuneOutput;
import mythicbotany.wand.RecipeDreamwoodWand;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.data.recipes.WrapperResult;

import java.util.Objects;

@Datagen
public class RecipeProvider extends RecipeProviderBase implements CraftingExtension, CompressionExtension, SmeltingExtension, SmithingExtension, PetalExtension, ManaInfusionExtension, RuneExtension, ElvenTradeExtension, InfuserExtension, RuneRitualExtension {

    public RecipeProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.makeFloatingFlowerRecipes();
        this.makeDreamwoodWandRecipe();

        this.doubleCompress(ModItems.alfsteelNugget, ModItems.alfsteelIngot, ModBlocks.alfsteelBlock, true);
        this.shaped(ModItems.fireRing, "re ", "e e", " e ", 'r', ModItems.muspelheimRune, 'e', ModTags.Items.INGOTS_ELEMENTIUM);
        this.shaped(ModItems.iceRing, "re ", "e e", " e ", 'r', ModItems.niflheimRune, 'e', ModTags.Items.INGOTS_ELEMENTIUM);

        this.shaped(ModBlocks.manaInfuser, "eee", "wdz", "xay",
                'e', ModTags.Items.INGOTS_ELEMENTIUM,
                'd', vazkii.botania.common.block.ModBlocks.dreamwoodGlimmering,
                'a', ModItems.asgardRune,
                'w', vazkii.botania.common.item.ModItems.runeSpring,
                'x', vazkii.botania.common.item.ModItems.runeSummer,
                'y', vazkii.botania.common.item.ModItems.runeAutumn,
                'z',vazkii.botania.common.item.ModItems.runeWinter
        );

        this.shaped(ModBlocks.alfsteelPylon, " g ", "npn", " g ", 'p', vazkii.botania.common.block.ModBlocks.naturaPylon, 'n', ModItems.alfsteelNugget, 'g', Items.GHAST_TEAR);
        this.shaped(vazkii.botania.common.block.ModBlocks.gaiaPylon, " d ", "epe", " d ", 'p', ModBlocks.alfsteelPylon, 'e', ModTags.Items.INGOTS_ELEMENTIUM, 'd', vazkii.botania.common.item.ModItems.pixieDust);
        this.shaped(ModBlocks.manaCollector, "dgd", "dpd", "dmd", 'd', vazkii.botania.common.block.ModBlocks.dreamwoodGlimmering, 'g', vazkii.botania.common.item.ModItems.gaiaIngot, 'p', vazkii.botania.common.item.ModItems.pixieDust, 'm', ModItems.vanaheimRune);
        this.shaped(ModBlocks.yggdrasilBranch, "lll", "ttt", "lll", 'l', ModTags.Items.LIVINGWOOD_LOGS, 't', ModTags.Items.NUGGETS_TERRASTEEL);
        this.shaped(ModBlocks.runeHolder, " w ", "wdw", 'w', Tags.Items.INGOTS_IRON, 'd', ModTags.Items.DUSTS_MANA);
        this.shaped(ModBlocks.masterRuneHolder, " w ", "wdw", 'w', Tags.Items.INGOTS_IRON, 'd', ModTags.Items.DUSTS_MANA);
        this.shapeless(ModItems.kvasirMead, ModItems.kvasirBlood, Items.HONEY_BOTTLE);

        this.blasting(ModBlocks.elementiumOre, vazkii.botania.common.item.ModItems.elementium, 0.7f, 200);
        this.blasting(ModBlocks.dragonstoneOre, vazkii.botania.common.item.ModItems.dragonstone, 0.7f, 200);

        this.smithing(vazkii.botania.common.item.ModItems.terraSword, ModItems.alfsteelIngot, ModItems.alfsteelSword);
        this.smithing(vazkii.botania.common.item.ModItems.terraPick, ModItems.alfsteelIngot, ModItems.alfsteelPick);
        this.smithing(vazkii.botania.common.item.ModItems.terraAxe, ModItems.alfsteelIngot, ModItems.alfsteelAxe);
        this.smithing(vazkii.botania.common.item.ModItems.terrasteelHelm, ModItems.alfsteelIngot, ModItems.alfsteelHelmet);
        this.smithing(vazkii.botania.common.item.ModItems.terrasteelChest, ModItems.alfsteelIngot, ModItems.alfsteelChestplate);
        this.smithing(vazkii.botania.common.item.ModItems.terrasteelLegs, ModItems.alfsteelIngot, ModItems.alfsteelLeggings);
        this.smithing(vazkii.botania.common.item.ModItems.terrasteelBoots, ModItems.alfsteelIngot, ModItems.alfsteelBoots);
        this.smithing(vazkii.botania.common.item.ModItems.manaRingGreater, ModItems.alfsteelIngot, ModItems.manaRingGreatest);
        this.smithing(vazkii.botania.common.item.ModItems.auraRingGreater, ModItems.alfsteelIngot, ModItems.auraRingGreatest);

        this.petalApothecary(ModBlocks.exoblaze, this.petal(DyeColor.YELLOW), this.petal(DyeColor.YELLOW), this.petal(DyeColor.GRAY), this.petal(DyeColor.LIGHT_GRAY), Ingredient.of(vazkii.botania.common.item.ModItems.runeFire), Ingredient.of(Items.BLAZE_POWDER));
        this.petalApothecary(ModBlocks.witherAconite, this.petal(DyeColor.BLACK), this.petal(DyeColor.BLACK), Ingredient.of(vazkii.botania.common.item.ModItems.runePride), Ingredient.of(Blocks.WITHER_ROSE));
        this.petalApothecary(ModBlocks.aquapanthus, this.petal(DyeColor.BLUE), this.petal(DyeColor.BLUE), this.petal(DyeColor.LIGHT_BLUE), this.petal(DyeColor.GREEN), this.petal(DyeColor.CYAN));
        this.petalApothecary(ModBlocks.hellebore, this.petal(DyeColor.RED), this.petal(DyeColor.RED), this.petal(DyeColor.PURPLE), this.petal(DyeColor.CYAN), Ingredient.of(vazkii.botania.common.item.ModItems.runeFire));
        this.petalApothecary(ModBlocks.raindeletia, this.petal(DyeColor.LIGHT_BLUE), this.petal(DyeColor.BLUE), this.petal(DyeColor.MAGENTA), this.petal(DyeColor.WHITE), Ingredient.of(vazkii.botania.common.item.ModItems.runeWater));
        this.petalApothecary(ModBlocks.petrunia, this.petal(DyeColor.RED), this.petal(DyeColor.RED), this.petal(DyeColor.ORANGE), this.petal(DyeColor.BROWN), Ingredient.of(ModItems.gjallarHornFull), Ingredient.of(vazkii.botania.common.item.ModItems.phantomInk));

        this.manaInfusion(vazkii.botania.common.item.ModItems.grassHorn, ModItems.gjallarHornEmpty, 20000);

        this.runeAltar(ModItems.midgardRune, 16000, Ingredient.of(ModTags.Items.INGOTS_MANASTEEL), Ingredient.of(vazkii.botania.common.item.ModItems.runeEarth), Ingredient.of(vazkii.botania.common.item.ModItems.runeSpring), Ingredient.of(vazkii.botania.common.item.ModItems.runeGreed), Ingredient.of(Blocks.GRASS_BLOCK));
        this.runeAltar(ModItems.alfheimRune, 16000, Ingredient.of(ModTags.Items.INGOTS_ELEMENTIUM), Ingredient.of(vazkii.botania.common.item.ModItems.runeAir), Ingredient.of(vazkii.botania.common.item.ModItems.runeSummer), Ingredient.of(vazkii.botania.common.item.ModItems.runeLust), Ingredient.of(Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES));
        this.runeAltar(ModItems.muspelheimRune, 16000, Ingredient.of(Tags.Items.INGOTS_NETHER_BRICK), Ingredient.of(vazkii.botania.common.item.ModItems.runeFire), Ingredient.of(vazkii.botania.common.item.ModItems.runeSummer), Ingredient.of(vazkii.botania.common.item.ModItems.runeWrath), Ingredient.of(Blocks.MAGMA_BLOCK));
        this.runeAltar(ModItems.niflheimRune, 16000, Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(vazkii.botania.common.item.ModItems.runeWater), Ingredient.of(vazkii.botania.common.item.ModItems.runeWinter), Ingredient.of(vazkii.botania.common.item.ModItems.runeWrath), Ingredient.of(Blocks.BLUE_ICE));
        this.runeAltar(ModItems.asgardRune, 16000, Ingredient.of(Tags.Items.INGOTS_NETHERITE), Ingredient.of(vazkii.botania.common.item.ModItems.runeAir), Ingredient.of(vazkii.botania.common.item.ModItems.runeAutumn), Ingredient.of(vazkii.botania.common.item.ModItems.runePride), Ingredient.of(vazkii.botania.common.item.ModItems.rainbowRod));
        this.runeAltar(ModItems.vanaheimRune, 16000, Ingredient.of(ModTags.Items.INGOTS_TERRASTEEL), Ingredient.of(vazkii.botania.common.item.ModItems.runeEarth), Ingredient.of(vazkii.botania.common.item.ModItems.runeSpring), Ingredient.of(vazkii.botania.common.item.ModItems.runePride), Ingredient.of(vazkii.botania.common.block.ModBlocks.alfPortal));
        this.runeAltar(ModItems.helheimRune, 16000, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(vazkii.botania.common.item.ModItems.runeFire), Ingredient.of(vazkii.botania.common.item.ModItems.runeAutumn), Ingredient.of(vazkii.botania.common.item.ModItems.runeEnvy), Ingredient.of(Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD));
        this.runeAltar(ModItems.nidavellirRune, 16000, Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(vazkii.botania.common.item.ModItems.runeEarth), Ingredient.of(vazkii.botania.common.item.ModItems.runeWinter), Ingredient.of(vazkii.botania.common.item.ModItems.runeSloth), Ingredient.of(Blocks.IRON_BLOCK));
        this.runeAltar(ModItems.joetunheimRune, 16000, Ingredient.of(Tags.Items.INGOTS_BRICK), Ingredient.of(vazkii.botania.common.item.ModItems.runeEarth), Ingredient.of(vazkii.botania.common.item.ModItems.runeAutumn), Ingredient.of(vazkii.botania.common.item.ModItems.runeGluttony), Ingredient.of(Blocks.BLACKSTONE));

        this.elvenTrade(ModBlocks.dreamwoodLeaves, Ingredient.of(ItemTags.LEAVES));

        this.infuser(vazkii.botania.common.item.ModItems.terrasteel)
                .addIngredient(ModTags.Items.INGOTS_MANASTEEL)
                .addIngredient(vazkii.botania.common.item.ModItems.manaPearl)
                .addIngredient(ModTags.Items.GEMS_MANA_DIAMOND)
                .setManaCost(500000)
                .setColors(0x0000FF, 0x00FF00)
                .build();

        this.infuser(ModItems.alfsteelIngot)
                .addIngredient(ModTags.Items.INGOTS_ELEMENTIUM)
                .addIngredient(ModTags.Items.GEMS_DRAGONSTONE)
                .addIngredient(vazkii.botania.common.item.ModItems.pixieDust)
                .setManaCost(2000000)
                .setColors(0xFF008D, 0xFF9600)
                .build();

        this.runeRitual(vazkii.botania.common.item.ModItems.runeMana)
                .rune4(vazkii.botania.common.item.ModItems.runeGreed, 2, 2)
                .rune2(ModItems.alfheimRune, 3, 0)
                .rune2(vazkii.botania.common.item.ModItems.runeGreed, 0, 3)
                .input(Ingredient.of(new ItemStack(ModItems.cursedAndwariRing), new ItemStack(ModItems.andwariRing)))
                .input(vazkii.botania.common.item.ModItems.manaweaveCloth)
                .output(new ItemStack(ModItems.andwariRing))
                .build();

        this.runeRitual(ModItems.fimbultyrTablet)
                .rune4(vazkii.botania.common.item.ModItems.runeWrath, 5, 0)
                .rune4(vazkii.botania.common.item.ModItems.runePride, 4, 4)
                .rune(vazkii.botania.common.item.ModItems.runeAir, -3, 2)
                .rune(vazkii.botania.common.item.ModItems.runeAir, 3, 2)
                .rune(vazkii.botania.common.item.ModItems.runeAir, -2, 3)
                .rune(vazkii.botania.common.item.ModItems.runeAir, 2, 3)
                .rune(vazkii.botania.common.item.ModItems.runeEarth, -3, -2)
                .rune(vazkii.botania.common.item.ModItems.runeEarth, 3, -2)
                .rune(vazkii.botania.common.item.ModItems.runeEarth, -2, -3)
                .rune(vazkii.botania.common.item.ModItems.runeEarth, 2, -3)
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
                .build();

        this.runeRitual(ModItems.fimbultyrTablet)
                .rune2(ModItems.midgardRune, 2, 2)
                .rune2(ModItems.helheimRune, -2, 2)
                .rune2(vazkii.botania.common.item.ModItems.runeSummer, 1, 3)
                .rune2(vazkii.botania.common.item.ModItems.runeSummer, 3, 1)
                .rune2(vazkii.botania.common.item.ModItems.runeFire, -1, 3)
                .rune2(vazkii.botania.common.item.ModItems.runeFire, -3, 1)
                .input(vazkii.botania.common.item.ModItems.enderDagger)
                .input(ModItems.alfsteelNugget)
                .input(vazkii.botania.common.item.ModItems.vial)
                .special(WanderingTraderRuneInput.INSTANCE)
                .output(ModItems.kvasirBlood)
                .mana(20000)
                .build();
    }

    private void makeFloatingFlowerRecipes() {
        //noinspection deprecation
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> MythicBotany.getInstance().modid.equals(Registry.ITEM.getKey(item).getNamespace()))
                .filter(item -> item instanceof BlockItem)
                .filter(item -> ((BlockItem) item).getBlock() instanceof BlockFloatingFunctionalFlower<?>)
                .forEach(item -> this.shapeless(item, ModTags.Items.FLOATING_FLOWERS, ((BlockFloatingFunctionalFlower<?>) ((BlockItem) item).getBlock()).getNonFloatingBlock()));
    }
    
    private void makeDreamwoodWandRecipe() {
        ShapedRecipeBuilder.shaped(ModItems.dreamwoodTwigWand)
                .define('p', ModTags.Items.PETALS)
                .define('t', vazkii.botania.common.item.ModItems.dreamwoodTwig)
                .pattern(" pt")
                .pattern(" tp")
                .pattern("t  ")
                .group(Objects.requireNonNull(ModItems.dreamwoodTwigWand.getRegistryName()).toString())
                .unlockedBy("has_item", has(ModTags.Items.PETALS))
                .save(WrapperResult.ofType(RecipeDreamwoodWand.SERIALIZER, this.consumer()));
    }
}
