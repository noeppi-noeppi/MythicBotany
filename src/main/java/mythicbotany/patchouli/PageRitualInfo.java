package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import vazkii.botania.client.patchouli.component.ManaComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.ArrayList;

public class PageRitualInfo extends PageRuneRitualBase {

    public static final ResourceLocation OVERLAY_TEXTURE = MythicBotany.getInstance().resource("textures/gui/patchouli_ritual_info.png");

    @SerializedName("text")
    public String text;

    private transient ManaComponent manaComponent;
    private transient BookTextRenderer desc;

    @Override
    public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(entry, builder, pageNum);
        this.manaComponent = new ManaComponent();
        this.manaComponent.build((GuiBook.PAGE_WIDTH / 2) - 51, 115, pageNum);
        if (this.recipe != null) {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            outputs.addAll(this.recipe.getOutputs());
            if (this.recipe.getSpecialOutput() != null) {
                outputs.addAll(this.recipe.getSpecialOutput().getJeiOutputItems());
            }
            for (ItemStack stack : outputs) {
                entry.addRelevantStack(builder, stack, pageNum);
            }
        }
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        if (this.text != null) {
            Component tc = this.text.isEmpty() ? new TextComponent("") : (parent.book.i18n ? new TranslatableComponent(this.text) : new TextComponent(this.text));
            this.desc = new BookTextRenderer(parent, tc, 1, 64);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);
        RenderSystem.enableBlend();
        GuiComponent.blit(poseStack, 0, 0, 0, 0, GuiBook.PAGE_WIDTH, GuiBook.PAGE_HEIGHT, 256, 256);
        this.renderInputs(poseStack, mouseX, mouseY, partialTicks);
        this.renderOutputs(poseStack, mouseX, mouseY, partialTicks);
        this.renderManaBar(poseStack, mouseX, mouseY, partialTicks);
        if (this.desc != null) {
            this.desc.render(poseStack, mouseX, mouseY);
        }
    }

    private void renderInputs(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null) {
            ArrayList<Ingredient> inputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            inputs.addAll(this.recipe.getInputs());
            if (this.recipe.getSpecialInput() != null) {
                inputs.addAll(this.recipe.getSpecialInput().getJeiInputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                this.parent.renderIngredient(poseStack, startX + (16 * i), 12, mouseX, mouseY, inputs.get(i));
            }
        }
    }

    private void renderOutputs(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null) {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            outputs.addAll(this.recipe.getOutputs());
            if (this.recipe.getSpecialOutput() != null) {
                outputs.addAll(this.recipe.getSpecialOutput().getJeiOutputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * outputs.size());
            for (int i = 0; i < outputs.size(); i++) {
                this.parent.renderItemStack(poseStack, startX + (16 * i), 41, mouseX, mouseY, outputs.get(i));
            }
        }
    }

    private void renderManaBar(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.recipe != null && this.recipe.getMana() > 0) {
            this.manaComponent.mana = IVariable.wrap(this.recipe.getMana());
            this.manaComponent.onVariablesAvailable(v -> v);
            this.manaComponent.render(poseStack, this.parent, partialTicks, mouseX, mouseY);
        }
    }
}
