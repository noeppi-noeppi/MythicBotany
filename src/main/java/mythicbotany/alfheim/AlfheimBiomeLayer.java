package mythicbotany.alfheim;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;

public class AlfheimBiomeLayer implements AreaTransformer0 {
    
    public AlfheimBiomeLayer(Registry<Biome> biomeRegistry) {
        AlfheimBiomeManager.checkBiomes(biomeRegistry);
    }
    
    @Override
    public int applyPixel(@Nonnull Context noiseRandom, int x, int y) {
        return ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(AlfheimBiomeManager.generate(noiseRandom, x, y).location());
    }
}
