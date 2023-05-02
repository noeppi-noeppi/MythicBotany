package mythicbotany.data.lexicon.page;

import net.minecraft.resources.ResourceLocation;
import org.moddingx.libx.datagen.provider.patchouli.content.RecipePage;

import javax.annotation.Nullable;
import java.util.List;

public class InfuserPage extends RecipePage {

    public InfuserPage(ResourceLocation recipe) {
        this(List.of(recipe), null);
    }
    
    private InfuserPage(List<ResourceLocation> recipes, @Nullable String caption) {
        super("mythicbotany:infuser", recipes, caption);
    }

    @Override
    protected int lineSkip() {
        return 14;
    }

    @Override
    protected InfuserPage withCaption(String caption) {
        return new InfuserPage(this.recipes, caption);
    }
}
