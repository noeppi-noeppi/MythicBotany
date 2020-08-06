package mythicbotany.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mythicbotany.MythicBotany;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

// Sadly JEI does not have this on it's own.
public class SmithingCategory implements IRecipeCategory<SmithingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(MythicBotany.MODID, "jei_category_smithing");

    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;

    public SmithingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 168, 125, 18).addPadding(0, 20, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Blocks.SMITHING_TABLE));
        this.localizedName = I18n.format("block.minecraft.smithing_table");
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends SmithingRecipe> getRecipeClass() {
        return SmithingRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(@Nonnull SmithingRecipe recipe, @Nonnull IIngredients ii) {
        Ingredient base = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(SmithingRecipe.class, recipe, "field_234837_a_"));
        Ingredient addition = Objects.requireNonNull(ObfuscationReflectionHelper.getPrivateValue(SmithingRecipe.class, recipe, "field_234838_b_"));

        ii.setInputLists(VanillaTypes.ITEM, ImmutableList.of(Arrays.asList(base.getMatchingStacks()), Arrays.asList(addition.getMatchingStacks())));
        ii.setOutputLists(VanillaTypes.ITEM, ImmutableList.of(ImmutableList.of(recipe.getRecipeOutput())));
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull SmithingRecipe recipe, @Nonnull IIngredients ii) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 49, 0);
        guiItemStacks.init(2, false, 107, 0);
        guiItemStacks.set(ii);
    }
}
