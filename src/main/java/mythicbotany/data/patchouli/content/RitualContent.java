package mythicbotany.data.patchouli.content;

import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class RitualContent extends CaptionContent {

    private final Item item;
    private final ResourceLocation id;

    public RitualContent(Item item, ResourceLocation id, @Nullable String caption) {
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
        return new RitualContent(this.item, this.id, caption);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        builder.flipToEven();
        
        JsonObject json1 = new JsonObject();
        json1.addProperty("type", "mythicbotany:ritual_pattern");
        ResourceLocation itemLoc = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.item), "Item not registered: " + this.item);
        json1.addProperty("title", "item." + itemLoc.getNamespace() + "." + itemLoc.getPath());
        json1.addProperty("recipe", this.id.toString());
        builder.addPage(json1);

        JsonObject json2 = new JsonObject();
        json2.addProperty("type", "mythicbotany:ritual_info");
        json2.addProperty("title", "");
        json2.addProperty("recipe", this.id.toString());
        if (caption != null) {
            json2.addProperty("text", caption);
        }
        builder.addPage(json2);
    }
}
