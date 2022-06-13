package mythicbotany.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.infuser.IInfuserRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.TerraPlateDrawable;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfusionCategory implements IRecipeCategory<IInfuserRecipe> {

    public static final ResourceLocation UID = MythicBotany.getInstance().resource("jei_category_infusion");

    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;
    private final IDrawable infuserPlate;

    public InfusionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(114, 141);
        this.localizedName = new TranslatableComponent("block.mythicbotany.mana_infuser");
        this.overlay = guiHelper.createDrawable(new ResourceLocation("botania","textures/gui/terrasteel_jei_overlay.png"), 42, 29, 64, 64);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModBlocks.manaInfuser));
        IDrawable shimmerrock = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(vazkii.botania.common.block.ModBlocks.shimmerrock));
        this.infuserPlate = new TerraPlateDrawable(shimmerrock, shimmerrock, guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Blocks.GOLD_BLOCK)));
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
    public void setIngredients(@Nonnull IInfuserRecipe recipe, @Nonnull IIngredients ii) {
        List<List<ItemStack>> list = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            list.add(Arrays.asList(ingredient.getItems()));
        }
        ii.setInputLists(VanillaTypes.ITEM, list);
        ii.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull IInfuserRecipe recipe, @Nonnull IIngredients ii) {
        layout.getItemStacks().init(0, false, 48, 37);
        layout.getItemStacks().set(0, ii.getOutputs(VanillaTypes.ITEM).get(0));
        double angle = 360 / (double) ii.getInputs(VanillaTypes.ITEM).size();
        Vec2 point = new Vec2(48, 5);
        Vec2 center = new Vec2(48, 37);
        for (int i = 1; i <= ii.getInputs(VanillaTypes.ITEM).size(); i++) {
            layout.getItemStacks().init(i, true, Math.round(point.x), Math.round(point.y));
            layout.getItemStacks().set(i, ii.getInputs(VanillaTypes.ITEM).get(i - 1));
            point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angle);
        }
        layout.getItemStacks().init(ii.getInputs(VanillaTypes.ITEM).size() + 1, true, 48, 92);
        layout.getItemStacks().set(ii.getInputs(VanillaTypes.ITEM).size() + 1, new ItemStack(ModBlocks.manaInfuser));
    }

    public void draw(IInfuserRecipe recipe, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        this.overlay.draw(poseStack, 25, 14);
        HUDHandler.renderManaBar(poseStack, 6, 126, 0x0000FF, 0.75f, recipe.getManaUsage(), 4000000);
        this.infuserPlate.draw(poseStack, 35, 92);
        RenderSystem.disableBlend();
        MutableComponent manaAmount = new TextComponent(BigDecimal.valueOf(recipe.getManaUsage() / 1000000d).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()).withStyle(ChatFormatting.BLUE);
        MutableComponent tc = new TranslatableComponent("tooltip.mythicbotany.cost_pools", manaAmount);
        //noinspection IntegerDivisionInFloatingPointContext
        Minecraft.getInstance().font.draw(poseStack, tc, 57 - (Minecraft.getInstance().font.width(tc) / 2), 133, 0x000000);
    }
}
