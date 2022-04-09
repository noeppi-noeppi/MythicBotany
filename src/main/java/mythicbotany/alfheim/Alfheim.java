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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
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
    private static final Map<ConfiguredStructureFeature<?, ?>, StructureSettings> STRUCTURES = new HashMap<>();
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

    public static void addStructure(ConfiguredStructureFeature<?, ?> structure, int weight, int spacing, int separation, int salt) {
        addStructure(structure, weight, new RandomSpreadStructurePlacement(spacing, separation, RandomSpreadType.LINEAR, salt));
    }
    
    public static void addStructure(ConfiguredStructureFeature<?, ?> structure, int weight, StructurePlacement placement) {
        synchronized (LOCK) {
            if (STRUCTURES.containsKey(structure)) throw new IllegalStateException("Structure registered twice in alfheim: " + structure);
            STRUCTURES.put(structure, new StructureSettings(weight, placement));
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
                list.add(new Pair<>(entry.getValue(), biomeResolver.apply(key).orElseThrow(() -> new NoSuchElementException("Alfheim biome not regsitered: " + key))));
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
    
    public static HolderSet<StructureSet> buildAlfheimStructures() {
        synchronized (LOCK) {
            List<StructureSet> list = new ArrayList<>();
            for (Map.Entry<ConfiguredStructureFeature<?, ?>, StructureSettings> entry : STRUCTURES.entrySet()) {
                list.add(new StructureSet(List.of(new StructureSet.StructureSelectionEntry(Holder.direct(entry.getKey()), entry.getValue().weight())), entry.getValue().placement()));
            }
            return HolderSet.direct(list.stream().map(Holder::direct).toList());
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
        //noinspection ConstantConditions
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
    
    private record StructureSettings(int weight, StructurePlacement placement) {}
}
