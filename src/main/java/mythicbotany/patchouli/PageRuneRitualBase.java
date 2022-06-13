package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
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

        if (this.recipeId == null) {
            this.recipe = null;
		} else {
            Recipe<?> foundRecipe = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getRecipeManager().byKey(this.recipeId).orElse(null) : null;
            if (foundRecipe instanceof RuneRitualRecipe) {
                this.recipe = (RuneRitualRecipe) foundRecipe;
            } else {
                this.recipe = null;
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (!this.title.isEmpty()) {
            this.parent.drawCenteredStringNoShadow(poseStack, this.parent.book.i18n ? I18n.get(this.title) : this.title, GuiBook.PAGE_WIDTH / 2, 0, 0x000000);
        }
    }
}
