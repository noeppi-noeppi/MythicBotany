package mythicbotany.data.lexicon.page;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.moddingx.libx.datagen.provider.patchouli.content.RecipePage;

import javax.annotation.Nullable;
import java.util.List;

public class ManaPage extends RecipePage {

    public ManaPage(ResourceLocation... recipes) {
        this(List.of(recipes), null);
    }
    
    private ManaPage(List<ResourceLocation> recipes, @Nullable String caption) {
        super("botania:mana_infusion", recipes, caption);
    }

    @Override
    protected int lineSkip() {
        return 14;
    }

    @Override
    protected void addRecipeKey(JsonObject json) {
        JsonArray array = new JsonArray();
        for (ResourceLocation recipe : this.recipes) {
            array.add(recipe.toString());
        }
        json.add("recipes", array);
    }

    @Override
    protected ManaPage withCaption(String caption) {
        return new ManaPage(this.recipes, caption);
    }
}
