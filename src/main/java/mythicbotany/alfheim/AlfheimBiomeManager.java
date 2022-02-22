package mythicbotany.alfheim;

import mythicbotany.MythicBotany;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.BiomeDictionary;

import java.util.*;
import java.util.stream.Stream;

public class AlfheimBiomeManager {
    
    public static final BiomeDictionary.Type ALFHEIM = BiomeDictionary.Type.getType(MythicBotany.getInstance() + "_alfheim");
    
    private static final List<ResourceKey<Biome>> COMMON = new ArrayList<>();
    private static final List<ResourceKey<Biome>> UNCOMMON = new ArrayList<>();
    private static final List<ResourceKey<Biome>> RARE = new ArrayList<>();
    private static final Map<StructureFeature<?>, StructureFeatureConfiguration> STRUCTURES = new HashMap<>();
    
    public static void addCommonBiome(ResourceLocation biome) {
        addCommonBiome(ResourceKey.create(Registry.BIOME_REGISTRY, biome));
    }
    
    public static void addCommonBiome(ResourceKey<Biome> biome) {
        COMMON.add(biome);
        BiomeDictionary.addTypes(biome, ALFHEIM);
        Collections.sort(COMMON);
    }

    public static void addUncommonBiome(ResourceLocation biome) {
        addUncommonBiome(ResourceKey.create(Registry.BIOME_REGISTRY, biome));
    }
    
    public static void addUncommonBiome(ResourceKey<Biome> biome) {
        UNCOMMON.add(biome);
        BiomeDictionary.addTypes(biome, ALFHEIM);
        Collections.sort(UNCOMMON);
    }

    public static void addRareBiome(ResourceLocation biome) {
        addRareBiome(ResourceKey.create(Registry.BIOME_REGISTRY, biome));
    }
    
    public static void addRareBiome(ResourceKey<Biome> biome) {
        RARE.add(biome);
        BiomeDictionary.addTypes(biome, ALFHEIM);
        Collections.sort(RARE);
    }
    
    public static void addStructure(StructureFeature<?> structure, StructureFeatureConfiguration settings) {
        STRUCTURES.put(structure, settings);
    }
    
    public static void checkBiomes(Registry<Biome> biomeRegistry) {
        COMMON.stream().filter(key -> !biomeRegistry.getOptional(key.location()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.location() + "");
        });
        UNCOMMON.stream().filter(key -> !biomeRegistry.getOptional(key.location()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.location() + "");
        });
        RARE.stream().filter(key -> !biomeRegistry.getOptional(key.location()).isPresent()).findFirst().ifPresent(key -> {
            throw new IllegalStateException("Alfheim Biome not registered: " + key.location() + "");
        });
    }
    
    public static ResourceKey<Biome> generate(Context noiseRandom, int x, int y) {
		if (!RARE.isEmpty() && noiseRandom.nextRandom(20) == 10) {
            return RARE.get(noiseRandom.nextRandom(RARE.size()));
		} else if (!UNCOMMON.isEmpty() && noiseRandom.nextRandom(3) == 1) {
		    return UNCOMMON.get(noiseRandom.nextRandom(UNCOMMON.size()));
		} else {
		    return COMMON.get(noiseRandom.nextRandom(COMMON.size()));
        }
	}
    
    public static Stream<ResourceKey<Biome>> allBiomes() {
        Set<ResourceKey<Biome>> set = new HashSet<>();
        set.addAll(COMMON);
        set.addAll(UNCOMMON);
        set.addAll(RARE);
        return set.stream().sorted();
    }
	
	public static Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap() {
        return Collections.unmodifiableMap(STRUCTURES);
    }
}
