package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBook;

public abstract class PageRuneRitualBase extends BookPage {
    
    public static final int WIDTH = 130;
    public static final int HEIGHT = 170;

    @SerializedName("title")
    public String title;
    
    @SerializedName("recipe")
    public ResourceLocation recipeId;
    
    protected transient RuneRitualRecipe recipe;
    
    @Override
	public void build(BookEntry entry, int pageNum) {
        super.build(entry, pageNum);

        if (recipeId == null) {
			recipe = null;
		} else {
            IRecipe<?> foundRecipe = Minecraft.getInstance().world != null ? Minecraft.getInstance().world.getRecipeManager().getRecipe(recipeId).orElse(null) : null;
            if (foundRecipe instanceof RuneRitualRecipe) {
                recipe = (RuneRitualRecipe) foundRecipe;
            } else {
                recipe = null;
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (!title.isEmpty()) {
            parent.drawCenteredStringNoShadow(matrixStack, parent.book.i18n ? I18n.format(title) : title, GuiBook.PAGE_WIDTH / 2, 0, 0x000000);
        }
    }
}
