package mythicbotany.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mythicbotany.MythicBotany;
import mythicbotany.register.ModItems;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.client.gui.HUDHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class RuneRitualCategory implements IRecipeCategory<RuneRitualRecipe> {
    
    public static final RecipeType<RuneRitualRecipe> TYPE = RecipeType.create(MythicBotany.getInstance().modid, "rune_ritual", RuneRitualRecipe.class);

    private final IDrawable background;
    private final IDrawable slot;
    private final Component localizedName;
    private final IDrawable icon;
    
    public RuneRitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(MythicBotany.getInstance().resource("textures/gui/jei_ritual.png"), 0, 0, 136, 196);
        this.slot = guiHelper.getSlotDrawable();
        this.localizedName = Component.translatable("tooltip.mythicbotany.rune_ritual");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.fimbultyrTablet));
    }

    @Nonnull
    @Override
    public RecipeType<RuneRitualRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return this.localizedName;
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RuneRitualRecipe recipe, @Nonnull IFocusGroup focuses) {
        this.initRunePositioned(builder, new RuneRitualRecipe.RunePosition(recipe.getCenterRune(), 0, 0, true));
        for (int i = 0; i < recipe.getRunes().size(); i++) {
            this.initRunePositioned(builder, recipe.getRunes().get(i));
        }

        ArrayList<Ingredient> inputs = new ArrayList<>(recipe.getInputs());
        if (recipe.getSpecialInput() != null) inputs.addAll(recipe.getSpecialInput().getJeiInputItems());
        
        ArrayList<ItemStack> outputs = new ArrayList<>(recipe.getOutputs());
        if (recipe.getSpecialOutput() != null) outputs.addAll(recipe.getSpecialOutput().getJeiOutputItems());
        
        int startInX = 69 - (inputs.size() * 9);
        for (int i = 0; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, startInX + (i * 18), 138).setBackground(this.slot, -1, -1).addIngredients(inputs.get(i));
        }

        int startOutX = 69 - (outputs.size() * 9);
        for (int i = 0; i < outputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, startOutX + (i * 18), 170).setBackground(this.slot, -1, -1).addItemStack(outputs.get(i));
        }
    }
    
    private void initRunePositioned(@Nonnull IRecipeLayoutBuilder builder, RuneRitualRecipe.RunePosition rune) {
        int realX = 2 + (12 * (rune.getX() + 5));
        int realZ = 2 + (12 * ((-rune.getZ()) + 5));
        builder.addSlot(rune.isConsumed() ? RecipeIngredientRole.INPUT : RecipeIngredientRole.CATALYST, realX, realZ)
                .setCustomRenderer(VanillaTypes.ITEM_STACK, LittleBoxItemRenderer.getRenderer(rune.getX(), rune.getZ(), rune.isConsumed()))
                .addIngredients(rune.getRune());
    }

    @Override
    public void draw(@Nonnull RuneRitualRecipe recipe, @Nonnull IRecipeSlotsView slots, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        if (recipe.getMana() > 0) {
            HUDHandler.renderManaBar(poseStack, 17, 189, 0x0000FF, 0.75f, recipe.getMana(), 1000000);
        }
    }
}
