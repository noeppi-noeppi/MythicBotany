package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.patchouli.component.ManaComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.ArrayList;

public class PageRitualInfo extends PageRuneRitualBase {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/gui/patchouli_ritual_info.png");

    @SerializedName("text")
    public String text;

    private transient ManaComponent manaComponent;
    private transient BookTextRenderer desc;

    @Override
    public void build(BookEntry entry, int pageNum) {
        super.build(entry, pageNum);
        manaComponent = new ManaComponent();
        manaComponent.build((GuiBook.PAGE_WIDTH / 2) - 51, 115, pageNum);
        if (recipe != null) {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            outputs.addAll(recipe.getOutputs());
            if (recipe.getSpecialOutput() != null) {
                outputs.addAll(recipe.getSpecialOutput().getJeiOutputItems());
            }
            for (ItemStack stack : outputs) {
                entry.addRelevantStack(stack, pageNum);
            }
        }
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        if (text != null) {
            String translated = text.isEmpty() ? "" : (parent.book.i18n ? I18n.format(text) : text);
            desc = new BookTextRenderer(parent, translated, 1, 64);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        mc.textureManager.bindTexture(OVERLAY_TEXTURE);
        RenderSystem.enableBlend();
        AbstractGui.blit(matrixStack, 0, 0, 0, 0, GuiBook.PAGE_WIDTH, GuiBook.PAGE_HEIGHT, 256, 256);
        renderInputs(matrixStack, mouseX, mouseY, partialTicks);
        renderOutputs(matrixStack, mouseX, mouseY, partialTicks);
        renderManaBar(matrixStack, mouseX, mouseY, partialTicks);
        if (desc != null) {
            desc.render(matrixStack, mouseX, mouseY);
        }
    }

    private void renderInputs(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null) {
            ArrayList<Ingredient> inputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            inputs.addAll(recipe.getInputs());
            if (recipe.getSpecialInput() != null) {
                inputs.addAll(recipe.getSpecialInput().getJeiInputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                parent.renderIngredient(matrixStack, startX + (16 * i), 12, mouseX, mouseY, inputs.get(i));
            }
        }
    }

    private void renderOutputs(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null) {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            outputs.addAll(recipe.getOutputs());
            if (recipe.getSpecialOutput() != null) {
                outputs.addAll(recipe.getSpecialOutput().getJeiOutputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * outputs.size());
            for (int i = 0; i < outputs.size(); i++) {
                parent.renderItemStack(matrixStack, startX + (16 * i), 41, mouseX, mouseY, outputs.get(i));
            }
        }
    }

    private void renderManaBar(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null && recipe.getMana() > 0) {
            manaComponent.mana = IVariable.wrap(recipe.getMana());
            manaComponent.onVariablesAvailable(v -> v);
            manaComponent.render(matrixStack, parent, partialTicks, mouseX, mouseY);
        }
    }
}
