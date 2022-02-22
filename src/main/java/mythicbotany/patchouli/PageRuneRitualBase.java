package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.client.book.BookContentsBuilder;
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
	public void build(BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(entry, builder, pageNum);

        if (recipeId == null) {
			recipe = null;
		} else {
            Recipe<?> foundRecipe = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getRecipeManager().byKey(recipeId).orElse(null) : null;
            if (foundRecipe instanceof RuneRitualRecipe) {
                recipe = (RuneRitualRecipe) foundRecipe;
            } else {
                recipe = null;
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (!title.isEmpty()) {
            parent.drawCenteredStringNoShadow(poseStack, parent.book.i18n ? I18n.get(title) : title, GuiBook.PAGE_WIDTH / 2, 0, 0x000000);
        }
    }
}
