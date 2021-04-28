package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.gui.GuiBook;

public class PageRitualPattern extends PageRuneRitualBase {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/gui/patchouli_ritual_pattern.png");

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        mc.textureManager.bindTexture(OVERLAY_TEXTURE);
        RenderSystem.enableBlend();
        AbstractGui.blit(matrixStack, 0, 9, 0, 0, 116, 116, 256, 256);
        if (recipe != null) {
            addRune(matrixStack, recipe.getCenterRune(), 0, 0, mouseX, mouseY);
            for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                addRune(matrixStack, rune.getRune(), rune.getX(), rune.getZ(), mouseX, mouseY);
            }
        }
    }

    private void addRune(MatrixStack matrixStack, Ingredient rune, int x, int z, int mouseX, int mouseY) {
        int realX = 3 + (10 * (x + 5));
        int realY = 12 + (10 * ((-z) + 5));
        ItemStack[] stacks = rune.getMatchingStacks();
        if (stacks.length > 0) {
            ItemStack stack = stacks[(parent.getTicksInBook() / 20) % stacks.length];
            matrixStack.push();
            matrixStack.translate(realX - 1, realY - 1, 0);
            matrixStack.scale(12 / 16f, 12 / 16f, 12 / 16f);
            RenderHelper.transferMsToGl(matrixStack, () -> {
                Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderItemOverlays(parent.font, stack, realX, realY);
            });
            matrixStack.pop();
            if (parent.isMouseInRelativeRange(mouseX, mouseY, realX, realY, 10, 10)) {
                parent.setTooltipStack(stack);
                boolean keep = x != 0 || z != 0;
                String text1 = keep ? I18n.format("tooltip.mythicbotany.rune_offset", x, z) : I18n.format("tooltip.mythicbotany.rune_master");
                int color1 = TextFormatting.GOLD.getColor() == null ? 0x000000 : TextFormatting.GOLD.getColor();

                String text2 = I18n.format(keep ? "tooltip.mythicbotany.rune_keep" : "tooltip.mythicbotany.rune_consume");
                Integer colorInt2 = keep ? TextFormatting.DARK_GREEN.getColor() : TextFormatting.DARK_RED.getColor();
                int color2 = colorInt2 == null ? 0x000000 : colorInt2;

                matrixStack.push();
                //noinspection IntegerDivisionInFloatingPointContext
                matrixStack.translate(GuiBook.PAGE_WIDTH / 2, 126, 0);
                matrixStack.scale(0.7f, 0.7f, 0.7f);
                parent.drawCenteredStringNoShadow(matrixStack, text1, 0, 0, color1);
                parent.drawCenteredStringNoShadow(matrixStack, text2, 0, 2 + parent.font.FONT_HEIGHT, color2);
                matrixStack.pop();
            }
        }
    }
}
