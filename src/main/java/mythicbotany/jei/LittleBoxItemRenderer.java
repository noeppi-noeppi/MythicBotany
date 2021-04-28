package mythicbotany.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LittleBoxItemRenderer implements IIngredientRenderer<ItemStack> {

    private static final Map<Long, LittleBoxItemRenderer> renders = new HashMap<>();
    private static IIngredientRenderer<ItemStack> parent;
    
    public static LittleBoxItemRenderer getRenderer(int x, int z) {
        long id = x | (((long) z) << 32l);
        if (!renders.containsKey(id)) {
            renders.put(id, new LittleBoxItemRenderer(x, z));
        }
        return renders.get(id);
    }

    public static void setParent(IIngredientRenderer<ItemStack> parent) {
        LittleBoxItemRenderer.parent = parent;
    }

    private final int x;
    private final int z;

    private LittleBoxItemRenderer(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void render(@Nonnull MatrixStack matrixStack, int x, int y, @Nullable ItemStack stack) {
        if (parent != null) {
            parent.render(matrixStack, x - 2, y - 2, stack);
        }
    }

    @Nonnull
    public List<ITextComponent> getTooltip(@Nonnull ItemStack stack, @Nonnull ITooltipFlag flag) {
        List<ITextComponent> tooltip = new ArrayList<>();
        if (parent != null) {
            tooltip.addAll(parent.getTooltip(stack, flag));
        }
        if (x != 0 || z != 0) {
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.rune_offset", x, z).mergeStyle(TextFormatting.GOLD));
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.rune_keep").mergeStyle(TextFormatting.DARK_GREEN));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.rune_master").mergeStyle(TextFormatting.GOLD));
            tooltip.add(new TranslationTextComponent("tooltip.mythicbotany.rune_consume").mergeStyle(TextFormatting.DARK_RED));
        }
        return tooltip;
    }

    @Nonnull
    public FontRenderer getFontRenderer(@Nonnull Minecraft mc, @Nonnull ItemStack stack) {
        if (parent != null) {
            return parent.getFontRenderer(mc, stack);
        } else {
            return Minecraft.getInstance().fontRenderer;
        }
    }
}
