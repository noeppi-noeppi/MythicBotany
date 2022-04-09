package mythicbotany.data;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.TagProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
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
