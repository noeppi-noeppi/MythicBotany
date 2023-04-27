package mythicbotany.data;

import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.TagProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.ModBiomeTags;
import mythicbotany.alfheim.biome.AlfheimBiomes;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

@Datagen
public class BiomeTagsProvider extends TagProviderBase<Biome> {
    
    public BiomeTagsProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, ForgeRegistries.BIOMES, fileHelper);
    }

    @Override
    protected void setup() {
        this.tag(ModBiomeTags.ANDWARI_CAVE).add(AlfheimBiomes.goldenFields);
    }
}
