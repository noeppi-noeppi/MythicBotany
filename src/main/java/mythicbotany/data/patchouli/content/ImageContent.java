package mythicbotany.data.patchouli.content;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ImageContent extends CaptionContent {

    private final String title;
    private final List<ResourceLocation> images;
    
    public ImageContent(String title, List<ResourceLocation> images, @Nullable String caption) {
        super(caption);
        this.title = title;
        this.images = List.copyOf(images);
    }

    @Override
    protected int lineSkip() {
        return 12;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new ImageContent(this.title, this.images, caption);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        if (this.images.isEmpty()) throw new IllegalStateException("No images in image page");
        this.images.forEach(builder::checkAssets);
        JsonObject json = new JsonObject();
        json.addProperty("type", "patchouli:image");
        json.addProperty("title", builder.translate(this.title));
        JsonArray array = new JsonArray();
        this.images.forEach(rl -> array.add(rl.toString()));
        json.add("images", array);
        json.addProperty("border", true);
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json);
    }
}
