package mythicbotany.data.patchouli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.moddingx.libx.mod.ModX;
import mythicbotany.data.patchouli.translate.TranslationManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PatchouliProviderBase implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final ModX mod;
    protected final DataGenerator generator;
    protected final ExistingFileHelper fileHelper;
    private final String bookName;
    
    private final List<CategoryBuilder> categories;
    private final Set<ResourceLocation> categoryIds;
    private final List<EntryBuilder> entries;

    public PatchouliProviderBase(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper, String bookName) {
        this.mod = mod;
        this.generator = generator;
        this.fileHelper = fileHelper;
        this.bookName = bookName;
        this.categories = new ArrayList<>();
        this.categoryIds = new HashSet<>();
        this.entries = new ArrayList<>();
    }

    protected abstract void setup();
    
    public CategoryBuilder category(String id) {
        CategoryBuilder builder = new CategoryBuilder(this.mod.resource(id));
        this.categories.add(builder);
        this.categoryIds.add(builder.id);
        return builder;
    }
    
    public EntryBuilder entry(String id) {
        if (this.categories.isEmpty()) throw new IllegalStateException("No categories defined");
        return this.entry(id, this.categories.get(this.categories.size() - 1).id);
    }
    
    public EntryBuilder entry(String id, String category) {
        return entry(id, this.mod.resource(category));
    }
    
    public EntryBuilder entry(String id, ResourceLocation category) {
        if (this.mod.modid.equals(category.getNamespace()) && !this.categoryIds.contains(category)) throw new IllegalArgumentException("Unknown category: " + category);
        EntryBuilder builder = new EntryBuilder(id, category);
        this.entries.add(builder);
        return builder;
    }
    
    @Nonnull
    @Override
    public String getName() {
        return this.mod.modid + " patchouli book";
    }

    @Override
    public void run(@Nonnull HashCache cache) throws IOException {
        this.setup();
        TranslationManager mgr = new TranslationManager(this.bookName);
        for (int i = 0; i < this.categories.size(); i++) {
            CategoryBuilder category = this.categories.get(i);
            Path path = this.generator.getOutputFolder().resolve("assets/" + category.id.getNamespace() + "/patchouli_books/" + this.bookName + "/en_us/categories/" + category.id.getPath() + ".json");
            DataProvider.save(GSON, cache, category.build(mgr, i), path);
        }
        for (EntryBuilder entry : this.entries) {
            Path path = this.generator.getOutputFolder().resolve("assets/" + entry.category.getNamespace() + "/patchouli_books/" + this.bookName + "/en_us/entries/" + entry.category.getPath() + "/" + entry.id + ".json");
            DataProvider.save(GSON, cache, entry.build(mgr, this.fileHelper), path);
        }

        Path langPath = this.generator.getOutputFolder().resolve("assets/" + this.mod.modid + "_" + this.bookName + "/lang/en_us.json");
        DataProvider.save(GSON, cache, mgr.build(), langPath);
    }
}
