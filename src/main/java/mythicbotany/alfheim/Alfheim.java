package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mythicbotany.MythicBotany;
import mythicbotany.alfheim.gen.AlfheimBiomeSource;
import mythicbotany.alfheim.gen.AlfheimChunkGenerator;
import mythicbotany.alfheim.surface.AlfheimSurfaceBuilder;
import mythicbotany.register.HackyHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private static final Set<StructureSet> STRUCTURES = new HashSet<>();
    private static final Set<Holder<StructureSet>> STRUCTURE_HOLDERS = new HashSet<>();

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

    public static void addStructure(Holder<StructureSet> structure) {
        synchronized (LOCK) {
            if (STRUCTURES.contains(structure.value())) throw new IllegalStateException("Structure registered twice in alfheim: " + structure);
            STRUCTURES.add(structure.value());
            STRUCTURE_HOLDERS.add(structure);
        }
    }
    
    public static List<Climate.ParameterPoint> buildAllClimateParameters() {
        synchronized (LOCK) {
            return BIOMES.values().stream().toList();
        }
    }
    
    public static Climate.ParameterList<Holder<Biome>> buildAlfheimClimate(Function<ResourceKey<Biome>, Optional<Holder<Biome>>> biomeResolver) {
        synchronized (LOCK) {
            ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> list = ImmutableList.builder();
            for (Map.Entry<ResourceKey<Biome>, Climate.ParameterPoint> entry : biomes(BIOMES)) {
                ResourceKey<Biome> key = entry.getKey();
                list.add(new Pair<>(entry.getValue(), biomeResolver.apply(key).orElseThrow(() -> new NoSuchElementException("Alfheim biome not registered: " + key))));
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
    
    public static Predicate<Holder<StructureSet>> buildAlfheimStructures(Registry<Biome> biomeRegistry) {
        synchronized (LOCK) {
            Set<ResourceKey<StructureSet>> keys = Set.copyOf(STRUCTURE_HOLDERS).stream().map(holder -> holder.unwrapKey().orElseThrow(() -> new IllegalStateException("Alfheim structure with direct holder detected: " + holder + ", this is not allowed."))).collect(Collectors.toUnmodifiableSet());
            return holder -> keys.stream().anyMatch(holder::is);
        }
    }
    
    private static ResourceKey<Biome> biomeKey(Biome biome) {
        Optional<ResourceKey<Biome>> key = ForgeRegistries.BIOMES.getResourceKey(biome);
        //noinspection ConstantConditions
        if (key != null && key.isPresent()) {
            return key.get();
        } else {
            throw new IllegalStateException("Biome not registered: " + biome.getRegistryName() + ": " + biome);
        }
    }

    private static List<ResourceKey<Biome>> biomeKeys(List<Biome> biomes) {
        return biomes.stream().map(Alfheim::biomeKey).toList();
    }
    
    private static <T> List<Map.Entry<ResourceKey<Biome>, T>> biomes(Map<ResourceKey<Biome>, T> map) {
        synchronized (LOCK) {
            return map.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList();
        }
    }
    
    private record StructureSettings(int weight, StructurePlacement placement) {}
}
