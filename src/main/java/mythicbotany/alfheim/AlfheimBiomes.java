package mythicbotany.alfheim;

import mythicbotany.ModEntities;
import mythicbotany.config.MythicConfig;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;

import java.util.function.UnaryOperator;

import static net.minecraft.world.biome.BiomeMaker.getSkyColorWithTemperatureModifier;

public class AlfheimBiomes {

    public static Biome.Builder alfheimBiome() {
        return new Biome.Builder()
                .temperature(0.9f)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .precipitation(Biome.Precipitation.RAIN)
                .downfall(1);
    }

    public static MobSpawnSettings.Builder alfheimMobs() {
        MobSpawnSettings.Builder builder = new MobSpawnInfoBuilder(MobSpawnSettings.EMPTY)
                .creatureGenerationProbability(0.4f);
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
                .skyColor(calculateSkyColor(0.9f));
    }

    public static BiomeGenerationSettings.Builder alfheimGen(ConfiguredSurfaceBuilder<?> surface) {
        return alfheimGen(new AlfBiomeConfig() {
            
            @Override
            public BiomeGenerationSettings.Builder applySurface(BiomeGenerationSettings.Builder builder) {
                return builder.surfaceBuilder(surface);
            }

            @Override
            public BiomeGenerationSettings.Builder applyCarver(BiomeGenerationSettings.Builder builder) {
                return builder;
            }

            @Override
            public BiomeGenerationSettings.Builder applyFeature(BiomeGenerationSettings.Builder builder) {
                return builder;
            }
        });
    }
    
    public static BiomeGenerationSettings.Builder alfheimGen(AlfBiomeConfig type) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder();
        builder = type.applySurface(builder);
        builder = builder
                .addCarver(GenerationStep.Carving.AIR, Carvers.CAVE)
                .addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
        builder = type.applyCarver(builder);
        builder = builder
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_FOREST_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_MOUNTAIN_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_FUNGAL_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_SWAMP_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_DESERT_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_TAIGA_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_MESA_STONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.ELEMENTIUM_ORE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.DRAGONSTONE_ORE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AlfheimFeatures.GOLD_ORE);
        builder = type.applyFeature(builder);
        return builder;
    }

    public interface AlfBiomeConfig {
        BiomeGenerationSettings.Builder applySurface(BiomeGenerationSettings.Builder builder);
        BiomeGenerationSettings.Builder applyCarver(BiomeGenerationSettings.Builder builder);
        BiomeGenerationSettings.Builder applyFeature(BiomeGenerationSettings.Builder builder);
    }
    
    public enum AlfBiomeType implements AlfBiomeConfig {
        GRASSY(
                builder -> builder.surfaceBuilder(AlfheimFeatures.GRASS_SURFACE),
                null,
                builder -> builder
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.ALFHEIM_GRASS)
        ),
        GOLDEN(
                builder -> builder.surfaceBuilder(AlfheimFeatures.GOLD_SURFACE),
                null,
                builder -> builder
                        .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AlfheimFeatures.WHEAT_FIELDS)
        );

        private final UnaryOperator<BiomeGenerationSettings.Builder> actionSurface;
        private final UnaryOperator<BiomeGenerationSettings.Builder> actionCarver;
        private final UnaryOperator<BiomeGenerationSettings.Builder> actionFeature;

        AlfBiomeType(UnaryOperator<BiomeGenerationSettings.Builder> actionSurface, UnaryOperator<BiomeGenerationSettings.Builder> actionCarver, UnaryOperator<BiomeGenerationSettings.Builder> actionFeature) {
            this.actionSurface = actionSurface;
            this.actionCarver = actionCarver;
            this.actionFeature = actionFeature;
        }

        public BiomeGenerationSettings.Builder applySurface(BiomeGenerationSettings.Builder builder) {
            return actionSurface == null ? builder : actionSurface.apply(builder);
        }

        public BiomeGenerationSettings.Builder applyCarver(BiomeGenerationSettings.Builder builder) {
            return actionCarver == null ? builder : actionCarver.apply(builder);
        }

        public BiomeGenerationSettings.Builder applyFeature(BiomeGenerationSettings.Builder builder) {
            return actionFeature == null ? builder : actionFeature.apply(builder);
        }
    }
}
