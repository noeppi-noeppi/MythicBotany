package mythicbotany.data.patchouli;

import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.page.PageJson;
import mythicbotany.data.patchouli.translate.TranslationManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CategoryBuilder {

    public final ResourceLocation id;
    private String name;
    private String description;
    private ItemStack icon;
    private int sort;

    public CategoryBuilder(ResourceLocation id) {
        this.id = id;
        this.sort = -1;
    }

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder icon(ItemLike icon) {
        return this.icon(new ItemStack(icon));
    }

    public CategoryBuilder icon(ItemStack icon) {
        this.icon = icon.copy();
        return this;
    }
    
    public CategoryBuilder sort(int sort) {
        this.sort = sort;
        return this;
    }
    
    public JsonObject build(TranslationManager mgr, int num) {
        if (this.name == null) throw new IllegalStateException("Category name not set: " + id);
        if (this.description == null) throw new IllegalStateException("Category description not set: " + id);
        if (this.icon == null) throw new IllegalStateException("Category icon not set: " + id);
        JsonObject json = new JsonObject();
        json.addProperty("name", mgr.add(this.name, "category", this.id.getNamespace(), this.id.getPath(), "name"));
        json.addProperty("description", mgr.add(this.description, "category", this.id.getNamespace(), this.id.getPath(), "description"));
        json.add("icon", PageJson.stack(this.icon));
        json.addProperty("sortnum", this.sort < 0 ? num : this.sort);
        return json;
    }
}
