package mythicbotany.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.patchouli.client.book.gui.GuiBook;

public class PageRitualPattern extends PageRuneRitualBase {

    public static final ResourceLocation OVERLAY_TEXTURE = MythicBotany.getInstance().resource("textures/gui/patchouli_ritual_pattern.png");

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.enableBlend();
        graphics.blit(OVERLAY_TEXTURE, 0, 9, 0, 0, 116, 116, 256, 256);
        if (this.recipe != null) {
            this.addRune(graphics, this.recipe.getCenterRune(), 0, 0, true, mouseX, mouseY);
            for (RuneRitualRecipe.RunePosition rune : this.recipe.getRunes()) {
                this.addRune(graphics, rune.getRune(), rune.getX(), rune.getZ(), rune.isConsumed(), mouseX, mouseY);
            }
        }
    }

    private void addRune(GuiGraphics graphics, Ingredient rune, int x, int z, boolean consume, int mouseX, int mouseY) {
        int realX = 3 + (10 * (x + 5));
        int realY = 12 + (10 * ((-z) + 5));
        ItemStack[] stacks = rune.getItems();
        if (stacks.length > 0) {
            ItemStack stack = stacks[(this.parent.getTicksInBook() / 20) % stacks.length];
            graphics.pose().pushPose();
            graphics.pose().translate(realX - 1, realY - 1, 0);
            graphics.pose().scale(12 / 16f, 12 / 16f, 12 / 16f);
            graphics.renderItem(stack, 0, 0);
            graphics.renderItemDecorations(this.parent.font, stack, realX, realY);
            graphics.pose().popPose();
            if (this.parent.isMouseInRelativeRange(mouseX, mouseY, realX, realY, 10, 10)) {
                this.parent.setTooltipStack(stack);
                boolean keep = (x != 0 || z != 0) && !consume;
                String text1 = keep ? I18n.get("tooltip.mythicbotany.rune_offset", x, z) : I18n.get("tooltip.mythicbotany.rune_central");
                int color1 = ChatFormatting.GOLD.getColor() == null ? 0x000000 : ChatFormatting.GOLD.getColor();

                String text2 = I18n.get(keep ? "tooltip.mythicbotany.rune_keep" : "tooltip.mythicbotany.rune_consume");
                Integer colorInt2 = keep ? ChatFormatting.DARK_GREEN.getColor() : ChatFormatting.DARK_RED.getColor();
                int color2 = colorInt2 == null ? 0x000000 : colorInt2;

                graphics.pose().pushPose();
                //noinspection IntegerDivisionInFloatingPointContext
                graphics.pose().translate(GuiBook.PAGE_WIDTH / 2, 126, 0);
                graphics.pose().scale(0.7f, 0.7f, 0.7f);
                this.parent.drawCenteredStringNoShadow(graphics, text1, 0, 0, color1);
                this.parent.drawCenteredStringNoShadow(graphics, text2, 0, 2 + this.parent.font.lineHeight, color2);
                graphics.pose().popPose();
            }
        }
    }
}
