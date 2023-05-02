package mythicbotany.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LittleBoxItemRenderer implements IIngredientRenderer<ItemStack> {

    private static final Map<Triple<Integer, Integer, Boolean>, LittleBoxItemRenderer> renders = new HashMap<>();
    private static IIngredientRenderer<ItemStack> parent;
    
    public static LittleBoxItemRenderer getRenderer(int x, int z, boolean consume) {
        Triple<Integer, Integer, Boolean> triple = Triple.of(x, z, consume);
        if (!renders.containsKey(triple)) {
            renders.put(triple, new LittleBoxItemRenderer(x, z, consume));
        }
        return renders.get(triple);
    }

    public static void setParent(IIngredientRenderer<ItemStack> parent) {
        LittleBoxItemRenderer.parent = parent;
    }

    private final int x;
    private final int z;
    private final boolean consume;

    private LittleBoxItemRenderer(int x, int z, boolean consume) {
        this.x = x;
        this.z = z;
        this.consume = (x == 0 && z == 0) || consume;
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull ItemStack stack) {
        if (parent != null) {
            poseStack.pushPose();
            poseStack.translate(-2, -2, 0);
            parent.render(poseStack, stack);
            poseStack.popPose();
        }
    }

    @Nonnull
    public List<Component> getTooltip(@Nonnull ItemStack stack, @Nonnull TooltipFlag flag) {
        List<Component> tooltip = new ArrayList<>();
        if (parent != null) {
            tooltip.addAll(parent.getTooltip(stack, flag));
        }
        if (this.x != 0 || this.z != 0) {
            tooltip.add(Component.translatable("tooltip.mythicbotany.rune_offset", this.x, this.z).withStyle(ChatFormatting.GOLD));
        } else {
            tooltip.add(Component.translatable("tooltip.mythicbotany.rune_central").withStyle(ChatFormatting.GOLD));
        }
        if (this.consume) {
            tooltip.add(Component.translatable("tooltip.mythicbotany.rune_consume").withStyle(ChatFormatting.DARK_RED));
        } else {
            tooltip.add(Component.translatable("tooltip.mythicbotany.rune_keep").withStyle(ChatFormatting.DARK_GREEN));
        }
        return tooltip;
    }

    @Nonnull
    public Font getFontRenderer(@Nonnull Minecraft mc, @Nonnull ItemStack stack) {
        if (parent != null) {
            return parent.getFontRenderer(mc, stack);
        } else {
            return Minecraft.getInstance().font;
        }
    }
}
