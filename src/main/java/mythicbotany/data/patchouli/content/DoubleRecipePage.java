package mythicbotany.data.patchouli.content;

import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class DoubleRecipePage extends CaptionContent {

    private final String pageType;
    private final int skip;
    private final ResourceLocation recipe1;
    
    @Nullable
    private final ResourceLocation recipe2;

    public DoubleRecipePage(String pageType, int skip, ResourceLocation recipe1, @Nullable ResourceLocation recipe2, String caption) {
        super(caption);
        this.pageType = pageType;
        this.skip = skip;
        this.recipe1 = recipe1;
        this.recipe2 = recipe2;
    }


    @Override
    protected int lineSkip() {
        return this.recipe2 == null ? this.skip : this.skip * 2;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new DoubleRecipePage(this.pageType, this.skip, this.recipe1, this.recipe2, caption);
    }

    @Override
    public void pages(PageBuilder builder) {
        if (this.lineSkip() >= 16 && this.caption != null) {
            this.withCaption(null).pages(builder);
            new TextContent(this.caption, false).pages(builder);
        } else {
            super.pages(builder);
        }
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.pageType);
        json.addProperty("recipe", this.recipe1.toString());
        if (this.recipe2 != null) {
            json.addProperty("recipe2", this.recipe2.toString());
        }
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json);
    }

    @Override
    public Content with(Content next) {
        if (this.recipe2 == null && next instanceof DoubleRecipePage recipe && pageType.equals(recipe.pageType) && recipe.recipe2 == null) {
            String caption = this.caption == null ? recipe.caption : (recipe.caption == null ? this.caption : this.caption + " " + recipe.caption);
            return new DoubleRecipePage(this.pageType, this.skip, this.recipe1, recipe.recipe1, caption);
        } else {
            return super.with(next);
        }
    }
}
