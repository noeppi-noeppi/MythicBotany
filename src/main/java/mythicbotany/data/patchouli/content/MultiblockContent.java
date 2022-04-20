package mythicbotany.data.patchouli.content;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;

import javax.annotation.Nullable;

public class MultiblockContent extends CaptionContent {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final String title;
    private final String multiblockData;

    public MultiblockContent(String title, String multiblockData, @Nullable String caption) {
        super(caption);
        this.title = title;
        this.multiblockData = multiblockData;
    }

    @Override
    protected int lineSkip() {
        return 13;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new MultiblockContent(this.title, this.multiblockData, caption);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "patchouli:multiblock");
        json.addProperty("name", builder.translate(this.title));
        json.add("multiblock", GSON.fromJson(this.multiblockData, JsonObject.class));
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json);
    }
}
