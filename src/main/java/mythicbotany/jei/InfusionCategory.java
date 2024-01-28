package mythicbotany.jei;

import com.mojang.blaze3d.systems.RenderSystem;
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
import mythicbotany.infuser.InfuserRecipe;
import mythicbotany.register.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.integration.jei.PetalApothecaryRecipeCategory;
import vazkii.botania.client.integration.jei.TerrestrialAgglomerationDrawable;
import vazkii.botania.common.block.BotaniaBlocks;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class InfusionCategory implements IRecipeCategory<InfuserRecipe> {

    public static final RecipeType<InfuserRecipe> TYPE = RecipeType.create(MythicBotany.getInstance().modid, "infuser", InfuserRecipe.class);
    
    private final IDrawable background;
    private final Component localizedName;
    private final IDrawable overlay;
    private final IDrawable icon;
    private final IDrawable infuserPlate;

    public InfusionCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(114, 141);
        this.localizedName = Component.translatable("block.mythicbotany.mana_infuser");
        this.overlay = guiHelper.createDrawable(new ResourceLocation("botania","textures/gui/terrasteel_jei_overlay.png"), 42, 29, 64, 64);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.manaInfuser));
        IDrawable shimmerrock = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.shimmerrock));
        this.infuserPlate = new TerrestrialAgglomerationDrawable(shimmerrock, shimmerrock, guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.GOLD_BLOCK)));
    }

    @Nonnull
    @Override
    public RecipeType<InfuserRecipe> getRecipeType() {
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull InfuserRecipe recipe, @Nonnull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 48, 37).addItemStack(recipe.getResultItem());
        double angle = 360d / recipe.getIngredients().size();
        Vec2 point = new Vec2(49, 6);
        Vec2 center = new Vec2(49, 38);
        for (int i = 1; i <= recipe.getIngredients().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, Math.round(point.x), Math.round(point.y)).addIngredients(recipe.getIngredients().get(i - 1));
            point = PetalApothecaryRecipeCategory.rotatePointAbout(point, center, angle);
        }
        builder.addSlot(RecipeIngredientRole.CATALYST, 49, 93).addItemStack(new ItemStack(ModBlocks.manaInfuser));
    }

    @Override
    public void draw(@Nonnull InfuserRecipe recipe, @Nonnull IRecipeSlotsView slots, @Nonnull GuiGraphics graphics, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        this.overlay.draw(graphics, 25, 14);
        HUDHandler.renderManaBar(graphics, 6, 126, 0x0000FF, 0.75f, recipe.getManaUsage(), 4000000);
        this.infuserPlate.draw(graphics, 35, 92);
        RenderSystem.disableBlend();
        MutableComponent manaAmount = Component.literal(BigDecimal.valueOf(recipe.getManaUsage() / 1000000d).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()).withStyle(ChatFormatting.BLUE);
        MutableComponent tc = Component.translatable("tooltip.mythicbotany.cost_pools", manaAmount);
        graphics.drawString(Minecraft.getInstance().font, tc, 57 - (Minecraft.getInstance().font.width(tc) / 2), 133, 0x000000, false);
    }
}
