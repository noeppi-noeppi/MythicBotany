package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlfheimBiomeLayer implements IAreaTransformer0 {
    
    private static final List<Biome> commonBiomes = ImmutableList.of(
            Alfheim.alfheimPlains,
            Alfheim.alfheimHills,
            Alfheim.dreamwoodForest,
            Alfheim.alfheimLakes
	);
    
    private static final List<Biome> rareBiomes = ImmutableList.of(
            Alfheim.goldenFields
	);
    private final Registry<Biome> biomeRegistry;
    private final List<RegistryKey<Biome>> commonBiomeKeys;
    private final List<RegistryKey<Biome>> rareBiomeKeys;
    
    public AlfheimBiomeLayer(Registry<Biome> biomeRegistry) {
        this.biomeRegistry = biomeRegistry;
        commonBiomeKeys = commonBiomes.stream().map(b -> RegistryKey.getOrCreateKey(
                Registry.BIOME_KEY,
                Objects.requireNonNull(b.getRegistryName(), "Biome not registered.")
        )).collect(Collectors.toList());
        rareBiomeKeys = rareBiomes.stream().map(b -> RegistryKey.getOrCreateKey(
                Registry.BIOME_KEY,
                Objects.requireNonNull(b.getRegistryName(), "Biome not registered.")
        )).collect(Collectors.toList());
    }
    
    @Override
	public int apply(INoiseRandom noiseRandom, int x, int y) {
		if (noiseRandom.random(20) == 10) {
		    return biomeRegistry.getId(biomeRegistry.getValueForKey(rareBiomeKeys.get(noiseRandom.random(rareBiomeKeys.size()))));
		} else {
            return biomeRegistry.getId(biomeRegistry.getValueForKey(commonBiomeKeys.get(noiseRandom.random(commonBiomeKeys.size()))));
		}
	}
}
