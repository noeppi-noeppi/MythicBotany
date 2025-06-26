package mythicbotany.data.tags;

import mythicbotany.alfheim.datagen.AlfheimBiomes;
import mythicbotany.register.tags.ModWorldGenTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.tags.TagProviderBase;

public class BiomeTagsProvider extends TagProviderBase<Biome> {
    
    private final AlfheimBiomes biomes;
    
    public BiomeTagsProvider(DatagenContext ctx) {
        super(ctx, Registries.BIOME);
        this.biomes = ctx.findRegistryProvider(AlfheimBiomes.class);
    }

    @Override
    protected void setup() {
        this.tag(ModWorldGenTags.ALFHEIM).add(
                this.biomes.alfheimPlains.value(),
                this.biomes.alfheimHills.value(),
                this.biomes.dreamwoodForest.value(),
                this.biomes.goldenFields.value(),
                this.biomes.alfheimLakes.value()
        );
        this.tag(ModWorldGenTags.ANDWARI_CAVE).add(this.biomes.goldenFields.value());
        this.tag(ModWorldGenTags.ELVEN_HOUSES).add(this.biomes.alfheimPlains.value(), this.biomes.dreamwoodForest.value());
    }
}
