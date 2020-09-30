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
import mythicbotany.recipes.InfuserRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.jei.petalapothecary.PetalApothecaryRecipeCategory;

import javax.annotation.Nonnull;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InfusionCategory implements IRecipeCategory<InfuserRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(MythicBotany.MODID, "jei_category_infusion");

    private final IDrawable background;
    private final String localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;

    public InfusionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 120);
        this.localizedName = I18n.format("block.mythicbotany.mana_infuser");
        this.overlay = guiHelper.createDrawable(new ResourceLocation("botania", "textures/gui/petal_overlay.png"), 0, 0, 150, 110);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.manaInfuser));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends InfuserRecipe> getRecipeClass() {
        return InfuserRecipe.class;
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
    public void setIngredients(InfuserRecipe recipe, @Nonnull IIngredients ii) {
        List<List<ItemStack>> list = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            list.add(Arrays.asList(ingredient.getMatchingStacks()));
        }
        ii.setInputLists(VanillaTypes.ITEM, list);
        ii.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @SuppressWarnings("deprecation")
    public void draw(InfuserRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        this.overlay.draw(matrixStack);
        HUDHandler.renderManaBar(matrixStack, 24, 105, 255, 0.75F, recipe.getManaUsage(), 8000000);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        String text = new BigDecimal(recipe.getManaUsage() / (double) 1000000).setScale(2, RoundingMode.HALF_UP).toPlainString() + " Mana Pools";
        fontRenderer.drawString(matrixStack, text, (float) (75 - fontRenderer.getStringWidth(text) / 2), 114, Color.BLACK.getRGB());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull InfuserRecipe recipe, @Nonnull IIngredients ii) {
        layout.getItemStacks().init(0, true, 64, 52);
        layout.getItemStacks().set(0, new ItemStack(ModBlocks.manaInfuser));
        int index = 1;
        double angleBetweenEach = 360.0D / (double)ii.getInputs(VanillaTypes.ITEM).size();
        Vector2f point = new Vector2f(64.0F, 20.0F);
        Vector2f center = new Vector2f(64.0F, 52.0F);

        for(Iterator<List<ItemStack>> var9 = ii.getInputs(VanillaTypes.ITEM).iterator(); var9.hasNext(); point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angleBetweenEach)) {
            List<ItemStack> o = var9.next();
            layout.getItemStacks().init(index, true, (int)point.x, (int)point.y);
            layout.getItemStacks().set(index, o);
            ++index;
        }

        layout.getItemStacks().init(index, false, 103, 17);
        layout.getItemStacks().set(index, ii.getOutputs(VanillaTypes.ITEM).get(0));
    }
}
