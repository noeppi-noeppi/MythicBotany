package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.annotation.registration.NoReg;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.MythicBotany;
import mythicbotany.alfheim.placement.AlfheimFeatures;
import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

import static mythicbotany.alfheim.AlfheimBiomes.AlfBiomeType.GOLDEN;
import static mythicbotany.alfheim.AlfheimBiomes.AlfBiomeType.GRASSY;
import static mythicbotany.alfheim.AlfheimBiomes.*;
import static net.minecraft.world.biome.BiomeMaker.getSkyColorWithTemperatureModifier;

@RegisterClass
public class Alfheim {
    
    @NoReg
    public static final ResourceKey<Level> DIMENSION = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim"));

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_generator"), AlfheimChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_biomes"), AlfheimBiomeProvider.CODEC);
    }
    
    public static void setupBiomes() {
        AlfheimBiomeManager.addCommonBiome(alfheimPlains.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(alfheimHills.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(dreamwoodForest.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(alfheimLakes.getRegistryName());
        AlfheimBiomeManager.addRareBiome(goldenFields.getRegistryName());
        
        AlfheimBiomeManager.addStructure(AlfheimWorldGenUtil.andwariCave, new StructureFeatureConfiguration(28, 8, 438));
    }
    
    public static final Biome alfheimPlains = alfheimBiome()
            .depth(0.025f)
            .scale(0.05f)
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).specialEffects(
                    alfheimAmbience()
                            .build()
            ).generationSettings(
                    alfheimGen(GRASSY)
                            .addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER)
                            .addFeature(GenerationStep.Decoration.LAKES, Features.SPRING_WATER)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_LOOSE)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.MOTIF_FLOWERS)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
            
    public static final Biome alfheimHills = alfheimBiome()
            .depth(0.5f)
            .scale(0.2f)
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).specialEffects(
                    alfheimAmbience()
                            .build()
            ).generationSettings(
                    alfheimGen(GRASSY)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_LOOSE)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.MANA_CRYSTALS)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
    
    public static final Biome dreamwoodForest = alfheimBiome()
            .depth(0.1f)
            .scale(0.05f)
            .biomeCategory(Biome.BiomeCategory.FOREST)
            .mobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).specialEffects(
                    alfheimAmbience()
                            .build()
            ).generationSettings(
                    alfheimGen(GRASSY)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_DENSE)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
    
    public static final Biome goldenFields = alfheimBiome()
            .depth(0.025f)
            .scale(0.07f)
            .temperature(0.8f)
            .downfall(0.4f)
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).specialEffects(
                    alfheimAmbience()
                            .waterColor(4566514)
                            .waterFogColor(267827)
                            .fogColor(12638463)
                            .skyColor(calculateSkyColor(0.8f))
                            .build()
            ).generationSettings(
                    alfheimGen(GOLDEN)
                            .addStructureStart(AlfheimFeatures.ANDWARI_CAVE)
                            .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.MORE_GOLD_ORE)
                            .build()
            ).build();

    public static final Biome alfheimLakes = alfheimBiome()
            .depth(-0.5f)
            .scale(0.1f)
            .temperature(0.8f)
            .downfall(0.7f)
            .biomeCategory(Biome.BiomeCategory.OCEAN)
            .mobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).specialEffects(
                    alfheimAmbience()
                            .waterColor(4566514)
                            .waterFogColor(267827)
                            .fogColor(12638463)
                            .skyColor(calculateSkyColor(0.8f))
                            .build()
            ).generationSettings(
                    alfheimGen(GOLDEN)
                            
                            .build()
            ).build();
}
