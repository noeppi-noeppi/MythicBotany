package mythicbotany.patchouli;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;
import vazkii.patchouli.client.RenderHelper;
import vazkii.patchouli.client.book.gui.GuiBook;

public class PageRitualPattern extends PageRuneRitualBase {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/gui/patchouli_ritual_pattern.png");

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);
        RenderSystem.enableBlend();
        GuiComponent.blit(poseStack, 0, 9, 0, 0, 116, 116, 256, 256);
        if (recipe != null) {
            addRune(poseStack, recipe.getCenterRune(), 0, 0, true, mouseX, mouseY);
            for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
                addRune(poseStack, rune.getRune(), rune.getX(), rune.getZ(), rune.isConsumed(), mouseX, mouseY);
            }
        }
    }

    private void addRune(PoseStack poseStack, Ingredient rune, int x, int z, boolean consume, int mouseX, int mouseY) {
        int realX = 3 + (10 * (x + 5));
        int realY = 12 + (10 * ((-z) + 5));
        ItemStack[] stacks = rune.getItems();
        if (stacks.length > 0) {
            ItemStack stack = stacks[(parent.getTicksInBook() / 20) % stacks.length];
            poseStack.pushPose();
            poseStack.translate(realX - 1, realY - 1, 0);
            poseStack.scale(12 / 16f, 12 / 16f, 12 / 16f);
            RenderHelper.transferMsToGl(poseStack, () -> {
                Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
                Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(parent.font, stack, realX, realY);
            });
            poseStack.popPose();
            if (parent.isMouseInRelativeRange(mouseX, mouseY, realX, realY, 10, 10)) {
                parent.setTooltipStack(stack);
                boolean keep = (x != 0 || z != 0) && !consume;
                String text1 = keep ? I18n.get("tooltip.mythicbotany.rune_offset", x, z) : I18n.get("tooltip.mythicbotany.rune_master");
                int color1 = ChatFormatting.GOLD.getColor() == null ? 0x000000 : ChatFormatting.GOLD.getColor();

                String text2 = I18n.get(keep ? "tooltip.mythicbotany.rune_keep" : "tooltip.mythicbotany.rune_consume");
                Integer colorInt2 = keep ? ChatFormatting.DARK_GREEN.getColor() : ChatFormatting.DARK_RED.getColor();
                int color2 = colorInt2 == null ? 0x000000 : colorInt2;

                poseStack.pushPose();
                //noinspection IntegerDivisionInFloatingPointContext
                poseStack.translate(GuiBook.PAGE_WIDTH / 2, 126, 0);
                poseStack.scale(0.7f, 0.7f, 0.7f);
                parent.drawCenteredStringNoShadow(poseStack, text1, 0, 0, color1);
                parent.drawCenteredStringNoShadow(poseStack, text2, 0, 2 + parent.font.lineHeight, color2);
                poseStack.popPose();
            }
        }
    }
}
