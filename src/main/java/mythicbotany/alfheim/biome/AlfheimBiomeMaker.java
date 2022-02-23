package mythicbotany.alfheim.biome;

import mythicbotany.ModEntities;
import mythicbotany.alfheim.placement.AlfheimFeatures;
import mythicbotany.alfheim.placement.AlfheimPlacements;
import mythicbotany.config.MythicConfig;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class AlfheimBiomeMaker {

    public static Biome.BiomeBuilder alfheimBiome() {
        return new Biome.BiomeBuilder()
                .temperature(0.9f)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .precipitation(Biome.Precipitation.RAIN)
                .downfall(1);
    }

    public static MobSpawnSettings.Builder alfheimMobs() {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder()
                .creatureGenerationProbability(0.2f);
        BiomeDefaultFeatures.farmAnimals(builder);
        return builder
                .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.alfPixie, MythicConfig.spawns.pixies.weight, MythicConfig.spawns.pixies.min, MythicConfig.spawns.pixies.max))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, MythicConfig.spawns.witch.weight, MythicConfig.spawns.witch.min, MythicConfig.spawns.witch.max))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, MythicConfig.spawns.illusioner.weight, MythicConfig.spawns.illusioner.min, MythicConfig.spawns.illusioner.max));
    }

    public static BiomeSpecialEffects.Builder alfheimAmbience() {
        return new BiomeSpecialEffects.Builder()
                .waterColor(0x43d5ee)
                .waterFogColor(0x041f33)
                .fogColor(0xc0d8ff)
                .skyColor(OverworldBiomes.calculateSkyColor(0.9f));
    }

    public static BiomeGenerationSettings.Builder alfheimGen() {
        return new BiomeGenerationSettings.Builder()
                .addCarver(GenerationStep.Carving.AIR, AlfheimFeatures.cave)
                .addCarver(GenerationStep.Carving.AIR, AlfheimFeatures.canyon)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicForestStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicMountainStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicFungalStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicSwampStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicDesertStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicTaigaStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.metamorphicMesaStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.elementiumOre)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.dragonstoneOre)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimPlacements.goldOre);
    }
}
