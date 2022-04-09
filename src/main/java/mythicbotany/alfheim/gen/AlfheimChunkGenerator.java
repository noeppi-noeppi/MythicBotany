package mythicbotany.alfheim.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSamplingSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class AlfheimChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(generator -> generator.structureSets),
            RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
            Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter(generator -> generator.seed),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").orElseGet(NoiseGeneratorSettings::bootstrap).forGetter(generator -> generator.settings)
    ).apply(instance, instance.stable((t, n, b, s, k) -> new Generator(t, n, b, s, new Holder.Direct<>(applyStructures(k.value()))))));
    
    private static NoiseGeneratorSettings applyStructures(NoiseGeneratorSettings settings) {
        //noinspection deprecation
        return new NoiseGeneratorSettings(
                withSampling(settings.noiseSettings()),
                ModBlocks.livingrock.defaultBlockState(),
                Blocks.WATER.defaultBlockState(),
                settings.noiseRouter(),
                Alfheim.buildAlfheimSurface(),
                settings.seaLevel(),
                settings.disableMobGeneration(),
                settings.isAquifersEnabled(),
                false, // true would enable massive iron and copper veins
                settings.useLegacyRandomSource()
        );
    }
    
    private static NoiseSettings withSampling(NoiseSettings settings) {
        NoiseSamplingSettings sampling = new NoiseSamplingSettings(1.2, 18, 80, 120);
        return new NoiseSettings(
                settings.minY(), settings.height(), sampling, settings.topSlideSettings(), settings.bottomSlideSettings(),
                settings.noiseSizeHorizontal(), settings.noiseSizeVertical(), settings.terrainShaper()
        );
    }
    
    private static class Generator extends NoiseBasedChunkGenerator {
        
        private final HolderSet<StructureSet> structureSettings;
        
        public Generator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, BiomeSource biomeSource, long seed, Holder<NoiseGeneratorSettings> settings) {
            super(structureSets, noises, biomeSource, seed, settings);
            this.structureSettings = Alfheim.buildAlfheimStructures(); 
        }
        
        @Nonnull
        @Override
        public Stream<Holder<StructureSet>> possibleStructureSets() {
            return this.structureSettings.stream();
        }
    }
}
