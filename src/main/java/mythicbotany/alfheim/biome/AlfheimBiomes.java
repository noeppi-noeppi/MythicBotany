package mythicbotany.alfheim.biome;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.alfheim.placement.AlfheimPlacements;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import static mythicbotany.alfheim.biome.AlfheimBiomeMaker.*;

@RegisterClass(priority = -1)
public class AlfheimBiomes {

    public static final Biome alfheimPlains = alfheimBiome()
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(alfheimMobs().build())
            .specialEffects(alfheimAmbience().build())
            .generationSettings(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.looseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.motifFlowers)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.abandonedApothecaries)
                            .build()
            ).build();
    
    public static final Biome alfheimHills = alfheimBiome()
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(alfheimMobs().build())
            .specialEffects(alfheimAmbience().build())
            .generationSettings(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.looseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.manaCrystals)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.abandonedApothecaries)
                            .build()
            ).build();

    public static final Biome dreamwoodForest = alfheimBiome()
            .biomeCategory(Biome.BiomeCategory.FOREST)
            .mobSpawnSettings(alfheimMobs().build())
            .specialEffects(alfheimAmbience().build())
            .generationSettings(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.denseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.abandonedApothecaries)
                            .build()
            ).build();

    public static final Biome goldenFields = alfheimBiome()
            .temperature(0.8f)
            .downfall(0.4f)
            .biomeCategory(Biome.BiomeCategory.PLAINS)
            .mobSpawnSettings(alfheimMobs().build())
            .specialEffects(
                    alfheimAmbience()
                            .waterColor(0x45adf2)
                            .waterFogColor(0x41633)
                            .fogColor(0xc0d8ff)
                            .skyColor(OverworldBiomes.calculateSkyColor(0.8f))
                            .build()
            ).generationSettings(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.wheatFields)
                            .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.extraGoldOre)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.abandonedApothecaries)
                            .build()
            ).build();

    public static final Biome alfheimLakes = alfheimBiome()
            .temperature(0.8f)
            .downfall(0.7f)
            .biomeCategory(Biome.BiomeCategory.OCEAN)
            .mobSpawnSettings(alfheimMobs().build())
            .specialEffects(
                    alfheimAmbience()
                            .waterColor(0x45adf2)
                            .waterFogColor(0x41633)
                            .fogColor(0xc0d8ff)
                            .skyColor(OverworldBiomes.calculateSkyColor(0.8f))
                            .build()
            ).generationSettings(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimPlacements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, AlfheimPlacements.manaCrystals)
                            .build()
            ).build();
}
