package mythicbotany.alfheim.datagen;

import mythicbotany.register.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.BiomeProviderBase;

public class AlfheimBiomes extends BiomeProviderBase {

    private final AlfheimFeatures features = this.context.findRegistryProvider(AlfheimFeatures.class);
    private final AlfheimPlacements placements = this.context.findRegistryProvider(AlfheimPlacements.class);
    
    public final Holder<Biome> alfheimPlains = this.alfheimBiome(0.9f, 1f)
            .generation(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.looseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.motifFlowers)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, this.placements.abandonedApothecaries)
            ).build();
    
    public final Holder<Biome> alfheimHills = this.alfheimBiome(0.9f, 1f)
            .generation(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.looseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, this.placements.manaCrystals)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, this.placements.abandonedApothecaries)
            ).build();

    public final Holder<Biome> dreamwoodForest = this.alfheimBiome(0.9f, 1f)
            .generation(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.denseDreamwoodTrees)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, this.placements.abandonedApothecaries)
            ).build();

    public final Holder<Biome> goldenFields = this.alfheimBiome(0.8f, 0.4f)
            .effects(
                    this.alfheimEffects()
                            .waterColor(0x45adf2)
                            .waterFogColor(0x41633)
                            .fogColor(0xc0d8ff)
            ).generation(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.wheatFields)
                            .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.extraGoldOre)
                            .addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, this.placements.abandonedApothecaries)
            ).build();

    public final Holder<Biome> alfheimLakes = this.alfheimBiome(0.9f, 1f)
            .effects(
                    this.alfheimEffects()
                            .waterColor(0x45adf2)
                            .waterFogColor(0x41633)
                            .fogColor(0xc0d8ff)
            ).generation(
                    alfheimGen()
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.alfheimGrass)
                            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, this.placements.motifFlowers)
            ).build();
    
    public AlfheimBiomes(DatagenContext ctx) {
        super(ctx);
    }

    public BiomeBuilder alfheimBiome(float temperature, float downfall) {
        return this.biome(temperature, downfall)
                .effects(this.alfheimEffects())
                .mobSpawns(this.alfheimSpawns());
    }

    public BiomeSpecialEffects.Builder alfheimEffects() {
        return this.effects()
                .waterColor(0x43d5ee)
                .waterFogColor(0x041f33)
                .fogColor(0xc0d8ff);
    }

    public MobSpawnSettings.Builder alfheimSpawns() {
        MobSpawnSettings.Builder builder = this.spawns()
                .creatureGenerationProbability(0.2f);
        BiomeDefaultFeatures.farmAnimals(builder);
        return builder
                .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.alfPixie, 5, 4, 10))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 2, 1, 2))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, 1, 1, 1));
    }

    public BiomeGenerationSettings.PlainBuilder alfheimGen() {
        return this.generation()
                .addCarver(GenerationStep.Carving.AIR, this.features.cave)
                .addCarver(GenerationStep.Carving.AIR, this.features.canyon)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicForestStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicMountainStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicFungalStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicSwampStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicDesertStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicTaigaStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.metamorphicMesaStone)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.elementiumOre)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.dragonstoneOre)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, this.placements.goldOre);
    }
}
