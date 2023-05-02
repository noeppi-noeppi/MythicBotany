package mythicbotany.data.lexicon.page;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.moddingx.libx.datagen.provider.patchouli.content.CaptionContent;
import org.moddingx.libx.datagen.provider.patchouli.page.PageBuilder;

import javax.annotation.Nullable;

public class RitualPage extends CaptionContent {

    private final Item item;
    private final ResourceLocation id;

    public RitualPage(ItemLike item, ResourceLocation id) {
        this(item.asItem(), id, null);
    }
    
    private RitualPage(Item item, ResourceLocation id, @Nullable String caption) {
        super(caption);
        this.item = item;
        this.id = id;
    }

    @Override
    protected int lineSkip() {
        return 9;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new RitualPage(this.item, this.id, caption);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        builder.flipToEven();
        
        JsonObject json1 = new JsonObject();
        json1.addProperty("type", "mythicbotany:ritual_pattern");
        json1.addProperty("title", this.item.getDescriptionId());
        json1.addProperty("recipe", this.id.toString());
        builder.addPage(json1);

        JsonObject json2 = new JsonObject();
        json2.addProperty("type", "mythicbotany:ritual_info");
        json2.addProperty("title", "");
        json2.addProperty("recipe", this.id.toString());
        if (caption != null) {
            json2.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json2);
    }
}
