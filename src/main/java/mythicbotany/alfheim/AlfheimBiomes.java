package mythicbotany.alfheim;

import mythicbotany.ModEntities;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;

import java.util.function.UnaryOperator;

import static net.minecraft.world.biome.BiomeMaker.getSkyColorWithTemperatureModifier;

public class AlfheimBiomes {

    public static Biome.Builder alfheimBiome() {
        return new Biome.Builder()
                .temperature(0.9f)
                .withTemperatureModifier(Biome.TemperatureModifier.NONE)
                .precipitation(Biome.RainType.RAIN)
                .downfall(1);
    }

    public static MobSpawnInfo.Builder alfheimMobs() {
        MobSpawnInfo.Builder builder = new MobSpawnInfoBuilder(MobSpawnInfo.EMPTY)
                .withCreatureSpawnProbability(0.4f);
        DefaultBiomeFeatures.withPassiveMobs(builder);
                return builder
                        .withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(ModEntities.alfPixie, 50, 4, 10))
                        .withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.WITCH, 10, 1, 2))
                        .withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ILLUSIONER, 5, 1, 1));
    }

    public static BiomeAmbience.Builder alfheimAmbience() {
        return new BiomeAmbience.Builder()
                .setWaterColor(0x43d5ee)
                .setWaterFogColor(0x041f33)
                .setFogColor(0xc0d8ff)
                .withSkyColor(getSkyColorWithTemperatureModifier(0.9f));
    }

    public static BiomeGenerationSettings.Builder alfheimGen(AlfBiomeType type) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder();
        builder = type.applySurface(builder);
        builder = builder.withCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.field_243767_a)
                .withCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.field_243768_b);
        builder = type.applyCarver(builder);
        builder = builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_FOREST_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_MOUNTAIN_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_FUNGAL_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_SWAMP_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_DESERT_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_TAIGA_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.METAMORPHIC_MESA_STONE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.ELEMENTIUM_ORE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.DRAGONSTONE_ORE)
                .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.GOLD_ORE);
        builder = type.applyFeature(builder);
        return builder;
    }

    public enum AlfBiomeType {
        GRASSY(
                builder -> builder.withSurfaceBuilder(AlfheimFeatures.GRASS_SURFACE),
                null,
                builder -> builder
                        .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.ALFHEIM_GRASS)
        ),
        GOLDEN(
                builder -> builder.withSurfaceBuilder(AlfheimFeatures.GOLD_SURFACE),
                null,
                builder -> builder
                        .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.WHEAT_FIELDS)
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
