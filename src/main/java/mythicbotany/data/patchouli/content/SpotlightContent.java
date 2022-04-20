package mythicbotany.data.patchouli.content;

import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageBuilder;
import mythicbotany.data.patchouli.page.PageJson;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SpotlightContent extends CaptionContent {

    private final ItemStack stack;
    private final boolean recipe;
    
    public SpotlightContent(ItemStack stack, boolean recipe, @Nullable String caption) {
        super(caption);
        this.stack = stack.copy();
        this.recipe = recipe;
    }

    @Override
    protected int lineSkip() {
        return 4;
    }

    @Override
    protected boolean canTakeRegularText() {
        return true;
    }

    @Override
    protected CaptionContent withCaption(String caption) {
        return new SpotlightContent(this.stack, this.recipe, caption);
    }

    @Override
    protected void specialPage(PageBuilder builder, @Nullable String caption) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "patchouli:spotlight");
        json.add("item", PageJson.stack(this.stack));
        json.addProperty("link_recipe", this.recipe);
        if (caption != null) {
            json.addProperty("text", builder.translate(caption));
        }
        builder.addPage(json);
    }
}
