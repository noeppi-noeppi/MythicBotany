package mythicbotany.alfheim;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import mythicbotany.MythicBotany;
import mythicbotany.alfheim.gen.AlfheimBiomeSource;
import mythicbotany.alfheim.gen.AlfheimChunkGenerator;
import mythicbotany.alfheim.surface.AlfheimSurfaceBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Alfheim {
    
    public static final ResourceKey<Level> DIMENSION = ResourceKey.create(Registry.DIMENSION_REGISTRY, MythicBotany.getInstance().resource("alfheim"));
    public static final BiomeDictionary.Type BIOME_TYPE = BiomeDictionary.Type.getType(MythicBotany.getInstance().modid + "_alfheim");
    
    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, DIMENSION.location(), AlfheimChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, DIMENSION.location(), AlfheimBiomeSource.CODEC);
    }
    
    private static final Object LOCK = new Object();
    private static final Map<ResourceKey<Biome>, Climate.ParameterPoint> BIOMES = new HashMap<>();
    private static final Map<ResourceKey<Biome>, SurfaceRules.RuleSource> BIOME_SURFACE = new HashMap<>();
    private static final Map<StructureFeature<?>, StructureFeatureConfiguration> STRUCTURES = new HashMap<>();
    private static final Multimap<ResourceKey<Biome>, ConfiguredStructureFeature<?, ?>> BIOME_STRUCTURES = HashMultimap.create();

    public static void addBiome(Biome biome, BiomeConfiguration settings) {
        addBiome(biomeKey(biome), settings);
    }

    public static void addBiome(ResourceKey<Biome> biome, BiomeConfiguration settings) {
        synchronized (LOCK) {
            if (BIOMES.containsKey(biome)) throw new IllegalStateException("Biome registered twice in alfheim: " + biome);
            BIOMES.put(biome, settings.buildClimate());
            BIOME_SURFACE.put(biome, settings.buildSurface());
            BiomeDictionary.addTypes(biome, BIOME_TYPE);
        }
    }
    
    public static void addStructure(ConfiguredStructureFeature<?, ?> structure, StructureFeatureConfiguration settings, Biome... biomes) {
        addStructure(structure, settings, biomeKeys(biomes));
    }
    
    @SafeVarargs
    public static void addStructure(ConfiguredStructureFeature<?, ?> structure, StructureFeatureConfiguration settings, ResourceKey<Biome>... biomes) {
        synchronized (LOCK) {
            if (STRUCTURES.containsKey(structure.feature)) throw new IllegalStateException("Structure registered twice in alfheim: " + structure);
            STRUCTURES.put(structure.feature, settings);
            for (ResourceKey<Biome> biome : biomes) {
                BIOME_STRUCTURES.put(biome, structure);
            }
        }
    }
    
    public static List<Climate.ParameterPoint> buildAllClimateParameters() {
        synchronized (LOCK) {
            return BIOMES.values().stream().toList();
        }
    }
    
    public static Climate.ParameterList<Supplier<Biome>> buildAlfheimClimate(Function<ResourceKey<Biome>, Biome> biomeResolver) {
        synchronized (LOCK) {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, Supplier<Biome>>> list = ImmutableList.builder();
            for (Map.Entry<ResourceKey<Biome>, Climate.ParameterPoint> entry : biomes(BIOMES)) {
                ResourceKey<Biome> key = entry.getKey();
                list.add(new Pair<>(entry.getValue(), () -> {
                    Biome biome = biomeResolver.apply(key);
                    if (biome == null) throw new IllegalStateException("Alfheim biome not regsitered: " + key);
                    return biome;
                }));
            }
            return new Climate.ParameterList<>(list.build());
        }
    }
    
    public static SurfaceRules.RuleSource buildAlfheimSurface() {
        synchronized (LOCK) {
            ImmutableList.Builder<SurfaceRules.RuleSource> sequence = ImmutableList.builder();
            for (Map.Entry<ResourceKey<Biome>, SurfaceRules.RuleSource> entry : biomes(BIOME_SURFACE)) {
                sequence.add(SurfaceRules.ifTrue(SurfaceRules.isBiome(entry.getKey()), entry.getValue()));
            }
            return AlfheimSurfaceBuilder.buildSurface(SurfaceRules.sequence(sequence.build().toArray(new SurfaceRules.RuleSource[]{})));
        }
    }
    
    public static Map<StructureFeature<?>, StructureFeatureConfiguration> buildAlfheimStructures() {
        synchronized (LOCK) {
            return Map.copyOf(STRUCTURES);
        }
    }
    
    public static Map<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> buildAlfheimStructurePlacement() {
        synchronized (LOCK) {
            Map<StructureFeature<?>, Multimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> map = new HashMap<>();
            for (Map.Entry<ResourceKey<Biome>, ConfiguredStructureFeature<?, ?>> entry : BIOME_STRUCTURES.entries()) {
                map.computeIfAbsent(entry.getValue().feature, k -> HashMultimap.create()).put(entry.getValue(), entry.getKey());
            }
            return map.entrySet().stream().map(e -> Map.entry(e.getKey(), ImmutableMultimap.copyOf(e.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }
    
    private static ResourceKey<Biome> biomeKey(Biome biome) {
        Optional<ResourceKey<Biome>> key = ForgeRegistries.BIOMES.getResourceKey(biome);
        if (key != null && key.isPresent()) {
            return key.get();
        } else {
            throw new IllegalStateException("Biome not registered: " + biome.getRegistryName() + ": " + biome);
        }
    }

    private static ResourceKey<Biome>[] biomeKeys(Biome[] biomes) {
        //noinspection unchecked
        return Arrays.stream(biomes).map(Alfheim::biomeKey).toArray(ResourceKey[]::new);
    }
    
    private static <T> List<Map.Entry<ResourceKey<Biome>, T>> biomes(Map<ResourceKey<Biome>, T> map) {
        synchronized (LOCK) {
            return map.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
        }
    }
}
