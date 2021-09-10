package mythicbotany.alfheim;

import io.github.noeppi_noeppi.libx.annotation.NoReg;
import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import mythicbotany.MythicBotany;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.BiomeDictionary;

import static mythicbotany.alfheim.AlfheimBiomes.AlfBiomeType.GOLDEN;
import static mythicbotany.alfheim.AlfheimBiomes.AlfBiomeType.GRASSY;
import static mythicbotany.alfheim.AlfheimBiomes.*;
import static net.minecraft.world.biome.BiomeMaker.getSkyColorWithTemperatureModifier;

@RegisterClass
public class Alfheim {
    
    @NoReg
    public static final RegistryKey<World> DIMENSION = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim"));

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR_CODEC, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_generator"), AlfheimChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(MythicBotany.getInstance().modid, "alfheim_biomes"), AlfheimBiomeProvider.CODEC);
    }
    
    public static void setupBiomes() {
        AlfheimBiomeManager.addCommonBiome(alfheimPlains.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(alfheimHills.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(dreamwoodForest.getRegistryName());
        AlfheimBiomeManager.addCommonBiome(alfheimLakes.getRegistryName());
        AlfheimBiomeManager.addRareBiome(goldenFields.getRegistryName());
        
        AlfheimBiomeManager.addStructure(AlfheimWorldGen.andwariCave, new StructureSeparationSettings(28, 8, 438));
    }
    
    public static final Biome alfheimPlains = alfheimBiome()
            .depth(0.025f)
            .scale(0.05f)
            .category(Biome.Category.PLAINS)
            .withMobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).setEffects(
                    alfheimAmbience()
                            .build()
            ).withGenerationSettings(
                    alfheimGen(GRASSY)
                            .withFeature(GenerationStage.Decoration.LAKES, Features.LAKE_WATER)
                            .withFeature(GenerationStage.Decoration.LAKES, Features.SPRING_WATER)
                            .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_LOOSE)
                            .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.MOTIF_FLOWERS)
                            .withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
            
    public static final Biome alfheimHills = alfheimBiome()
            .depth(0.5f)
            .scale(0.2f)
            .category(Biome.Category.PLAINS)
            .withMobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).setEffects(
                    alfheimAmbience()
                            .build()
            ).withGenerationSettings(
                    alfheimGen(GRASSY)
                            .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_LOOSE)
                            .withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.MANA_CRYSTALS)
                            .withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
    
    public static final Biome dreamwoodForest = alfheimBiome()
            .depth(0.1f)
            .scale(0.05f)
            .category(Biome.Category.FOREST)
            .withMobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).setEffects(
                    alfheimAmbience()
                            .build()
            ).withGenerationSettings(
                    alfheimGen(GRASSY)
                            .withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, AlfheimFeatures.TREES_DREAMWOOD_DENSE)
                            .withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, AlfheimFeatures.ABANDONED_APOTHECARIES)
                            .build()
            ).build();
    
    public static final Biome goldenFields = alfheimBiome()
            .depth(0.025f)
            .scale(0.07f)
            .temperature(0.8f)
            .downfall(0.4f)
            .category(Biome.Category.PLAINS)
            .withMobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).setEffects(
                    alfheimAmbience()
                            .setWaterColor(4566514)
                            .setWaterFogColor(267827)
                            .setFogColor(12638463)
                            .withSkyColor(getSkyColorWithTemperatureModifier(0.8f))
                            .build()
            ).withGenerationSettings(
                    alfheimGen(GOLDEN)
                            .withStructure(AlfheimFeatures.ANDWARI_CAVE)
                            .withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, AlfheimFeatures.MORE_GOLD_ORE)
                            .build()
            ).build();

    public static final Biome alfheimLakes = alfheimBiome()
            .depth(-0.5f)
            .scale(0.1f)
            .temperature(0.8f)
            .downfall(0.7f)
            .category(Biome.Category.OCEAN)
            .withMobSpawnSettings(
                    alfheimMobs()
                            .build()
            ).setEffects(
                    alfheimAmbience()
                            .setWaterColor(4566514)
                            .setWaterFogColor(267827)
                            .setFogColor(12638463)
                            .withSkyColor(getSkyColorWithTemperatureModifier(0.8f))
                            .build()
            ).withGenerationSettings(
                    alfheimGen(GOLDEN)
                            
                            .build()
            ).build();
}
