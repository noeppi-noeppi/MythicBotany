package mythicbotany.alfheim;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nonnull;

public class AlfheimBiomeLayer implements IAreaTransformer0 {
    
    public AlfheimBiomeLayer(Registry<Biome> biomeRegistry) {
        AlfheimBiomeManager.checkBiomes(biomeRegistry);
    }
    
    @Override
	public int apply(@Nonnull INoiseRandom noiseRandom, int x, int y) {
        return ((ForgeRegistry<Biome>) ForgeRegistries.BIOMES).getID(AlfheimBiomeManager.generate(noiseRandom, x, y).getLocation());
	}
}
