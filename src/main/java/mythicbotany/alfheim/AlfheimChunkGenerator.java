package mythicbotany.alfheim;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import vazkii.botania.common.block.ModBlocks;

import java.util.Optional;

public class AlfheimChunkGenerator {

    public static final Codec<NoiseChunkGenerator> CODEC = RecordCodecBuilder.create((p_236091_0_) -> p_236091_0_.group(
            BiomeProvider.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeProvider),
            WorldSeedHolder.CODEC.fieldOf("seed").forGetter((p_236093_0_) -> p_236093_0_.seed)
    ).apply(p_236091_0_, p_236091_0_.stable((b, s) -> new NoiseChunkGenerator(b, s, AlfheimChunkGenerator::settings))));

    private static DimensionSettings settings() {
        DimensionSettings defaultSettings = DimensionSettings.getDefaultDimensionSettings();
        //noinspection deprecation
        return new DimensionSettings(structures(defaultSettings.getStructures()), defaultSettings.getNoise(),
                ModBlocks.livingrock.getDefaultState(), Blocks.WATER.getDefaultState(), defaultSettings.getBedrockRoofPosition(),
                defaultSettings.getBedrockFloorPosition(), defaultSettings.getSeaLevel(), defaultSettings.isMobGenerationDisabled());
    }
    
    private static DimensionStructuresSettings structures(DimensionStructuresSettings parent) {
        return new DimensionStructuresSettings(Optional.ofNullable(parent.getSpreadSettings()), AlfheimBiomeManager.structureMap());
    }
}
