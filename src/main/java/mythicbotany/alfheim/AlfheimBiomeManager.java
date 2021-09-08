package mythicbotany.alfheim;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlfheimBiomeManager {
    
    private static final List<RegistryKey<Biome>> COMMON = new ArrayList<>();
    private static final List<RegistryKey<Biome>> UNCOMMON = new ArrayList<>();
    private static final List<RegistryKey<Biome>> RARE = new ArrayList<>();
    
    public static void addCommonBiome(ResourceLocation biome) {
        addCommonBiome(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome));
    }
    
    public static void addCommonBiome(RegistryKey<Biome> biome) {
        COMMON.add(biome);
        Collections.sort(COMMON);
    }

    public static void addUncommonBiome(ResourceLocation biome) {
        addUncommonBiome(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome));
    }
    
    public static void addUncommonBiome(RegistryKey<Biome> biome) {
        UNCOMMON.add(biome);
        Collections.sort(UNCOMMON);
    }

    public static void addRareBiome(ResourceLocation biome) {
        addRareBiome(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome));
    }
    
    public static void addRareBiome(RegistryKey<Biome> biome) {
        RARE.add(biome);
        Collections.sort(RARE);
    }
    
    public static void checkBiomes(Registry<Biome> biomeRegistry) {
        COMMON.stream().filter(key -> !biomeRegistry.getOptional(key.getLocation()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.getLocation() + "");
        });
        UNCOMMON.stream().filter(key -> !biomeRegistry.getOptional(key.getLocation()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.getLocation() + "");
        });
        RARE.stream().filter(key -> !biomeRegistry.getOptional(key.getLocation()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.getLocation() + "");
        });
    }
    
    public static RegistryKey<Biome> generate(INoiseRandom noiseRandom, int x, int y) {
		if (!RARE.isEmpty() && noiseRandom.random(20) == 10) {
            return RARE.get(noiseRandom.random(RARE.size()));
		} else if (!UNCOMMON.isEmpty() && noiseRandom.random(3) == 1) {
		    return UNCOMMON.get(noiseRandom.random(UNCOMMON.size()));
		} else {
		    return COMMON.get(noiseRandom.random(COMMON.size()));
        }
	}
}
