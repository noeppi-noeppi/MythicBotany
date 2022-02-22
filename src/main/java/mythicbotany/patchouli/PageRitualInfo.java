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

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(MythicBotany.getInstance().modid, "textures/gui/patchouli_ritual_info.png");

    @SerializedName("text")
    public String text;

    private transient ManaComponent manaComponent;
    private transient BookTextRenderer desc;

    @Override
    public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(entry, builder, pageNum);
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
                entry.addRelevantStack(builder, stack, pageNum);
            }
        }
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        if (text != null) {
            Component tc = text.isEmpty() ? new TextComponent("") : (parent.book.i18n ? new TranslatableComponent(text) : new TextComponent(text));
            desc = new BookTextRenderer(parent, tc, 1, 64);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);
        RenderSystem.enableBlend();
        GuiComponent.blit(poseStack, 0, 0, 0, 0, GuiBook.PAGE_WIDTH, GuiBook.PAGE_HEIGHT, 256, 256);
        renderInputs(poseStack, mouseX, mouseY, partialTicks);
        renderOutputs(poseStack, mouseX, mouseY, partialTicks);
        renderManaBar(poseStack, mouseX, mouseY, partialTicks);
        if (desc != null) {
            desc.render(poseStack, mouseX, mouseY);
        }
    }

    private void renderInputs(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null) {
            ArrayList<Ingredient> inputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            inputs.addAll(recipe.getInputs());
            if (recipe.getSpecialInput() != null) {
                inputs.addAll(recipe.getSpecialInput().getJeiInputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                parent.renderIngredient(poseStack, startX + (16 * i), 12, mouseX, mouseY, inputs.get(i));
            }
        }
    }

    private void renderOutputs(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null) {
            ArrayList<ItemStack> outputs = new ArrayList<>();
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            outputs.addAll(recipe.getOutputs());
            if (recipe.getSpecialOutput() != null) {
                outputs.addAll(recipe.getSpecialOutput().getJeiOutputItems());
            }
            int startX = (GuiBook.PAGE_WIDTH / 2) - (8 * outputs.size());
            for (int i = 0; i < outputs.size(); i++) {
                parent.renderItemStack(poseStack, startX + (16 * i), 41, mouseX, mouseY, outputs.get(i));
            }
        }
    }

    private void renderManaBar(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (recipe != null && recipe.getMana() > 0) {
            manaComponent.mana = IVariable.wrap(recipe.getMana());
            manaComponent.onVariablesAvailable(v -> v);
            manaComponent.render(poseStack, parent, partialTicks, mouseX, mouseY);
        }
    }
}
