package mythicbotany.data.lexicon.page;

import net.minecraft.resources.ResourceLocation;
import org.moddingx.libx.datagen.provider.patchouli.content.RecipePage;

import javax.annotation.Nullable;
import java.util.List;

public class RunePage extends RecipePage {

    public RunePage(ResourceLocation recipe) {
        this(List.of(recipe), null);
    }
    
    private RunePage(List<ResourceLocation> recipes, @Nullable String caption) {
        super("botania:runic_altar", recipes, caption);
    }

    @Override
    protected int lineSkip() {
        return 14;
    }

    @Override
    protected RunePage withCaption(String caption) {
        return new RunePage(this.recipes, caption);
    }
}
