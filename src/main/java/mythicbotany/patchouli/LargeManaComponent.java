package mythicbotany.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.client.core.handler.HUDHandler;
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

    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX != -1 ? componentX : 7;
        this.y = componentY;
    }

    public void render(@Nonnull MatrixStack matrixStack, IComponentRenderContext ctx, float partialTicks, int mouseX, int mouseY) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        IFormattableTextComponent manaUsage = (new TranslationTextComponent("botaniamisc.manaUsage")).setStyle(ctx.getFont());
        //noinspection IntegerDivisionInFloatingPointContext
        font.drawText(matrixStack, manaUsage, this.x + 51 - font.getStringPropertyWidth(manaUsage) / 2, this.y, 0x66000000);
        
        float ratio = ctx.isAreaHovered(mouseX, mouseY, this.x, this.y - 2, 102, 25) ? 1 : 0.25f;

        HUDHandler.renderManaBar(matrixStack, this.x, this.y + 10, 0x00CCFF, 0.75F, this.manaValues[ctx.getTicksInBook() / 20 % this.manaValues.length], Math.round(1000000 / ratio));
        IFormattableTextComponent ratioTc = (new TranslationTextComponent("tooltip.mythicbotany.reverse_ratio", new StringTextComponent(floatToString(ratio)).mergeStyle(TextFormatting.BLUE))).setStyle(ctx.getFont());
        //noinspection IntegerDivisionInFloatingPointContext
        font.drawText(matrixStack, ratioTc, this.x + 51 - font.getStringPropertyWidth(ratioTc) / 2, this.y + 15, 0x777777);
    }

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
