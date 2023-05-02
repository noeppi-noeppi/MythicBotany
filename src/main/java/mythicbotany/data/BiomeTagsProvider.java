package mythicbotany.data;

import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Registry;
import org.moddingx.libx.datagen.provider.TagProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.tags.ModBiomeTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BiomeTagsProvider extends TagProviderBase<Biome> {
    
    public BiomeTagsProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper, Registry<Biome> biomeRegistry) {
        super(mod, generator, biomeRegistry, fileHelper);
    }

    @Override
    protected void setup() {
        this.tag(ModBiomeTags.ALFHEIM).add(
                Alfheim.ALFHEIM_PLAINS, Alfheim.ALFHEIM_HILLS, Alfheim.DREAMWOOD_FOREST,
                Alfheim.GOLDEN_FIELDS, Alfheim.ALFHEIM_LAKES
        );
        this.tag(ModBiomeTags.ANDWARI_CAVE).add(Alfheim.GOLDEN_FIELDS);
    }
}
