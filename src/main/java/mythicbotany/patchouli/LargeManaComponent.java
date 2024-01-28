package mythicbotany.patchouli;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import javax.annotation.Nonnull;
import java.util.function.UnaryOperator;

public class LargeManaComponent implements ICustomComponent {
    
    private transient int x;
    private transient int y;
    private transient int[] manaValues;
    public IVariable mana;
    
    public LargeManaComponent() {
        
    }

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX != -1 ? componentX : 7;
        this.y = componentY;
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, IComponentRenderContext ctx, float partialTicks, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        MutableComponent manaUsage = (Component.translatable("botaniamisc.manaUsage")).setStyle(ctx.getFont());
        graphics.drawString(font, manaUsage, this.x + 51 - font.width(manaUsage) / 2, this.y, 0x66000000, false);
        
        float ratio = ctx.isAreaHovered(mouseX, mouseY, this.x, this.y - 2, 102, 25) ? 1 : 0.25f;

        HUDHandler.renderManaBar(graphics, this.x, this.y + 10, 0x00CCFF, 0.75F, this.manaValues[ctx.getTicksInBook() / 20 % this.manaValues.length], Math.round(1000000 / ratio));
        MutableComponent ratioTc = (Component.translatable("tooltip.mythicbotany.reverse_ratio", Component.literal(floatToString(ratio)).withStyle(ChatFormatting.BLUE))).setStyle(ctx.getFont());
        graphics.drawString(font, ratioTc, this.x + 51 - font.width(ratioTc) / 2, this.y + 15, 0x777777, false);
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        IVariable manaVariable = lookup.apply(this.mana);
        if (manaVariable.unwrap().isJsonArray()) {
            this.manaValues = manaVariable.asStream().map(IVariable::asNumber).mapToInt(Number::intValue).toArray();
        } else {
            this.manaValues = new int[]{ manaVariable.asNumber(0).intValue() };
        }
    }
    
    private static String floatToString(float f) {
        String str = Float.toString(f);
        if (str.endsWith(".0")) {
            return str.substring(0, str.length() - 2);
        } else {
            return str;
        }
    }
}
