package mythicbotany.data.patchouli.content;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class SingleRecipePage extends CaptionContent {

    private final String pageType;
    private final int skip;
    private final boolean multiple;
    private final ResourceLocation recipe;

    public SingleRecipePage(String pageType, int skip, boolean multiple, ResourceLocation recipe, String caption) {
        super(caption);
        this.pageType = pageType;
        this.skip = skip;
        this.multiple = multiple;
        this.recipe = recipe;
    }


    @Override
    protected int lineSkip() {
        return this.skip;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new SingleRecipePage(this.pageType, this.skip, this.multiple, this.recipe, caption);
    }
    
    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.pageType);
        if (this.multiple) {
            JsonArray array = new JsonArray();
            array.add(this.recipe.toString());
            json.add("recipes", array);
        } else {
            json.addProperty("recipe", this.recipe.toString());
        }
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json);
    }
}
