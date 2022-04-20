package mythicbotany.data.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mythicbotany.data.patchouli.content.*;
import mythicbotany.data.patchouli.page.PageBuilder;
import mythicbotany.data.patchouli.page.PageJson;
import mythicbotany.data.patchouli.translate.TranslationManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;
import java.util.List;

public class EntryBuilder {
    
    public final String id;
    public final ResourceLocation category;
    private String name;
    private ItemStack icon;
    private Content content;

    public EntryBuilder(String id, ResourceLocation category) {
        this.id = id;
        this.category = category;
        this.name = null;
        this.icon = null;
        this.content = Content.EMPTY;
    }
    
    public EntryBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public EntryBuilder icon(ItemLike icon) {
        return this.icon(new ItemStack(icon));
    }
    
    public EntryBuilder icon(ItemStack icon) {
        this.icon = icon.copy();
        return this;
    }
    
    public EntryBuilder text(String text) {
        return this.add(new TextContent(text, false));
    }
    
    public EntryBuilder flip() {
        return this.add(FlipContent.INSTANCE);
    }
    
    public EntryBuilder caption(String text) {
        return this.add(new TextContent(text, true));
    }
    
    public EntryBuilder image(String title, String... images) {
        return this.image(title, Arrays.stream(images).map(s -> new ResourceLocation(this.category.getNamespace(), s)).toArray(ResourceLocation[]::new));
    }
    
    public EntryBuilder image(String title, ResourceLocation... images) {
        return this.add(new ImageContent(title, List.of(images), null));
    }
    
    public EntryBuilder crafting(String path) {
        return crafting(this.category.getNamespace(), path);
    }
    
    public EntryBuilder crafting(String namespace, String path) {
        return crafting(new ResourceLocation(namespace, path));
    }
    
    public EntryBuilder crafting(ResourceLocation id) {
        return this.add(new DoubleRecipePage("patchouli:crafting", 8, id, null, null));
    }

    public EntryBuilder smelting(String path) {
        return smelting(this.category.getNamespace(), path);
    }

    public EntryBuilder smelting(String namespace, String path) {
        return smelting(new ResourceLocation(namespace, path));
    }
    
    public EntryBuilder smelting(ResourceLocation id) {
        return this.add(new DoubleRecipePage("patchouli:smelting", 4, id, null, null));
    }

    public EntryBuilder item(ItemLike stack) {
        return this.item(new ItemStack(stack), true);
    }

    public EntryBuilder item(ItemLike stack, boolean linkRecipe) {
        return this.item(new ItemStack(stack), linkRecipe);
    }
    
    public EntryBuilder item(ItemStack stack) {
        return this.item(stack, true);
    }
    
    public EntryBuilder item(ItemStack stack, boolean linkRecipe) {
        return this.add(new SpotlightContent(stack, linkRecipe, null));
    }
    
    public EntryBuilder entity(EntityType<?> entity) {
        return this.add(new EntityContent(entity, null));
    }

    public EntryBuilder multiblock(String title, String data) {
        return this.add(new MultiblockContent(title, data, null));
    }
    
    public EntryBuilder ritual(ItemLike item, String path) {
        return this.ritual(item, this.category.getNamespace(), path);
    }
    
    public EntryBuilder ritual(ItemLike item, String namespace, String path) {
        return this.ritual(item, new ResourceLocation(namespace, path));
    }
    
    public EntryBuilder ritual(ItemLike item, ResourceLocation id) {
        return this.add(new RitualContent(item.asItem(), id, null));
    }
    
    public EntryBuilder infuser(String path) {
        return infuser(this.category.getNamespace(), path);
    }

    public EntryBuilder infuser(String namespace, String path) {
        return infuser(new ResourceLocation(namespace, path));
    }
    
    public EntryBuilder infuser(ResourceLocation id) {
        return this.add(new SingleRecipePage("mythicbotany:infuser", 14, false, id, null));
    }

    public EntryBuilder mana(String path) {
        return mana(this.category.getNamespace(), path);
    }

    public EntryBuilder mana(String namespace, String path) {
        return mana(new ResourceLocation(namespace, path));
    }

    public EntryBuilder mana(ResourceLocation id) {
        return this.add(new SingleRecipePage("botania:mana_infusion", 14, true, id, null));
    }

    public EntryBuilder petal(String path) {
        return petal(this.category.getNamespace(), path);
    }

    public EntryBuilder petal(String namespace, String path) {
        return petal(new ResourceLocation(namespace, path));
    }

    public EntryBuilder petal(ResourceLocation id) {
        return this.add(new SingleRecipePage("botania:petal_apothecary", 14, false, id, null));
    }

    public EntryBuilder rune(String path) {
        return rune(this.category.getNamespace(), path);
    }

    public EntryBuilder rune(String namespace, String path) {
        return rune(new ResourceLocation(namespace, path));
    }

    public EntryBuilder rune(ResourceLocation id) {
        return this.add(new SingleRecipePage("botania:runic_altar", 14, false, id, null));
    }
    
    public EntryBuilder trade(String path) {
        return trade(this.category.getNamespace(), path);
    }

    public EntryBuilder trade(String namespace, String path) {
        return trade(new ResourceLocation(namespace, path));
    }

    public EntryBuilder trade(ResourceLocation id) {
        return this.add(new SingleRecipePage("botania:elven_trade", 14, true, id, null));
    }
    
    public EntryBuilder add(Content content) {
        this.content = this.content.with(content);
        return this;
    }
    
    public JsonObject build(TranslationManager mgr, ExistingFileHelper fileHelper) {
        if (this.name == null) throw new IllegalStateException("Entry name not set: " + category + "/" + id);
        if (this.icon == null) throw new IllegalStateException("Entry icon not set: " + category + "/" + id);
        JsonObject json = new JsonObject();
        json.addProperty("name", mgr.add(this.name, "entry", this.category.getNamespace(), this.category.getPath(), this.id));
        json.addProperty("category", this.category.toString());
        json.add("icon", PageJson.stack(this.icon));
        json.addProperty("entry_color", "aa00");

        JsonArray pages = new JsonArray();
        PageBuilder builder = new PageBuilder() {
            
            private int page = 0;
            private int key = 0;

            @Override
            public boolean isFirst() {
                return this.page == 0;
            }

            @Override
            public void addPage(JsonObject page) {
                this.page += 1;
                this.key = 0;
                pages.add(page);
            }

            @Override
            public String translate(String localized) {
                return mgr.add(localized, "entry", EntryBuilder.this.category.getNamespace(), EntryBuilder.this.category.getPath(), EntryBuilder.this.id, "page" + this.page, "text" + (this.key++));
            }

            @Override
            public void flipToEven() {
                if ((this.page % 2) != 0) {
                    JsonObject json = new JsonObject();
                    json.addProperty("type", "patchouli:empty");
                    this.addPage(json);
                }
            }

            @Override
            public void checkAssets(ResourceLocation path) {
                if (!fileHelper.exists(path, PackType.CLIENT_RESOURCES)) {
                    throw new IllegalStateException("Resource " + path + " does not exist.");
                }
            }
        };
        
        this.content.pages(builder);
        
        json.add("pages", pages);
        return json;
    }
}
