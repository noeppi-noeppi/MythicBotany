package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.WorldGenProviderBase;
import io.github.noeppi_noeppi.mods.sandbox.datagen.registry.WorldGenRegistries;
import mythicbotany.data.BiomeTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.mod.ModX;

@Datagen
public class AlfheimProvider extends WorldGenProviderBase {

    public AlfheimProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.addData(AlfheimFeatures::new);
        this.addData(AlfheimPlacements::new);
        
        this.addData(AlfheimTemplates::new);
        this.addData(AlfheimStructures::new);
        this.addData(AlfheimStructureSets::new);
        
        this.addData(AlfheimBiomes::new);
        this.addData(AlfheimSurface::new);
        
        this.addData(AlfheimNoise::new);
        this.addData(AlfheimDimensionTypes::new);
        this.addData(AlfheimBiomeLayers::new);
        this.addData(AlfheimDimension::new);
    }

    @Override
    protected void addAdditionalProviders(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper, WorldGenRegistries registries) {
        generator.addProvider(true, new BiomeTagsProvider(mod, generator, fileHelper, registries.registry(Registry.BIOME_REGISTRY)));
    }
}
