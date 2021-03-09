package mythicbotany.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.infuser.IInfuserRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.TerraPlateDrawable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfusionCategory implements IRecipeCategory<IInfuserRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(MythicBotany.getInstance().modid, "jei_category_infusion");

    private final IDrawable background;
    private final String localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;
    private final IDrawable infuserPlate;

    public InfusionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(114, 131);
        this.localizedName = I18n.format("block.mythicbotany.mana_infuser");
        this.overlay = guiHelper.createDrawable(new ResourceLocation("botania","textures/gui/terrasteel_jei_overlay.png"), 42, 29, 64, 64);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.manaInfuser));
        IDrawable shimmerrock = guiHelper.createDrawableIngredient(new ItemStack(vazkii.botania.common.block.ModBlocks.shimmerrock));
        this.infuserPlate = new TerraPlateDrawable(shimmerrock, shimmerrock, guiHelper.createDrawableIngredient(new ItemStack(Blocks.GOLD_BLOCK)));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends IInfuserRecipe> getRecipeClass() {
        return IInfuserRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(@Nonnull IInfuserRecipe recipe, @Nonnull IIngredients ii) {
        List<List<ItemStack>> list = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            list.add(Arrays.asList(ingredient.getMatchingStacks()));
        }
        ii.setInputLists(VanillaTypes.ITEM, list);
        ii.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull IInfuserRecipe recipe, @Nonnull IIngredients ii) {
        layout.getItemStacks().init(0, false, 48, 37);
        layout.getItemStacks().set(0, ii.getOutputs(VanillaTypes.ITEM).get(0));
        double angle = 360 / (double) ii.getInputs(VanillaTypes.ITEM).size();
        Vector2f point = new Vector2f(48, 5);
        Vector2f center = new Vector2f(48, 37);
        for (int i = 1; i <= ii.getInputs(VanillaTypes.ITEM).size(); i++) {
            layout.getItemStacks().init(i, true, Math.round(point.x), Math.round(point.y));
            layout.getItemStacks().set(i, ii.getInputs(VanillaTypes.ITEM).get(i - 1));
            point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angle);
        }
        layout.getItemStacks().init(ii.getInputs(VanillaTypes.ITEM).size() + 1, true, 48, 92);
        layout.getItemStacks().set(ii.getInputs(VanillaTypes.ITEM).size() + 1, new ItemStack(ModBlocks.manaInfuser));
    }

    @SuppressWarnings("deprecation")
    public void draw(IInfuserRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        this.overlay.draw(matrixStack, 25, 14);
        HUDHandler.renderManaBar(matrixStack, 6, 126, 255, 0.75f, recipe.getManaUsage(), 4000000);
        this.infuserPlate.draw(matrixStack, 35, 92);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
    }
}
