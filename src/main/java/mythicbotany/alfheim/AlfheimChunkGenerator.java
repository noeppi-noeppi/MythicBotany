package mythicbotany.alfheim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import vazkii.botania.common.block.ModBlocks;

import java.util.Optional;

public class AlfheimChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
            Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter(generator -> generator.seed)
    ).apply(instance, instance.stable((b, s) -> new NoiseBasedChunkGenerator(b, s, AlfheimChunkGenerator::settings))));

    private static NoiseGeneratorSettings settings() {
        NoiseGeneratorSettings defaultSettings = NoiseGeneratorSettings.bootstrap();
        //noinspection deprecation
        return new NoiseGeneratorSettings(structures(defaultSettings.structureSettings()), defaultSettings.noiseSettings(),
                ModBlocks.livingrock.defaultBlockState(), Blocks.WATER.defaultBlockState(), defaultSettings.getBedrockRoofPosition(),
                defaultSettings.getBedrockFloorPosition(), defaultSettings.seaLevel(), defaultSettings.disableMobGeneration());
    }
    
    private static StructureSettings structures(StructureSettings parent) {
        return new StructureSettings(Optional.ofNullable(parent.stronghold()), AlfheimBiomeManager.structureMap());
    }
}
