package mythicbotany.data.recipes;

import com.feywild.feywild.item.ModItems;
import mythicbotany.register.tags.ModItemTags;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.tags.CommonTagsProviderBase;

import java.util.Objects;

public class FeywildTagsProvider extends CommonTagsProviderBase {

    public FeywildTagsProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    public void setup() {
        this.item(ModItemTags.FEYSYTHIA_LEVEL_1).addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.feyDust)));
        this.item(ModItemTags.FEYSYTHIA_LEVEL_2).addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.lesserFeyGem)));
        this.item(ModItemTags.FEYSYTHIA_LEVEL_3).addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.greaterFeyGem)));
        this.item(ModItemTags.FEYSYTHIA_LEVEL_4).addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.shinyFeyGem)));
        this.item(ModItemTags.FEYSYTHIA_LEVEL_5).addOptional(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(ModItems.brilliantFeyGem)));
    }
}
