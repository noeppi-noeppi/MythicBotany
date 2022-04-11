package mythicbotany.alfheim.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
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

    public static final Codec<Generator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryOps.retrieveRegistry(Registry.STRUCTURE_SET_REGISTRY).forGetter(generator -> generator.structureSets),
            RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
            RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(s -> s.biomeRegistry),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
            Codec.LONG.fieldOf("seed").orElse(0l).forGetter(generator -> generator.seed)
            // Don't serialise the noise generator settings as they are not present in the registry
            // Adding it to the codec would corrupt level.dat
    ).apply(instance, instance.stable(Generator::new)));

    private static NoiseGeneratorSettings generatorSettings() {
        NoiseGeneratorSettings settings = NoiseGeneratorSettings.bootstrap().value();
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

        private final Registry<Biome> biomeRegistry;
        private final HolderSet<StructureSet> structureSettings;

        public Generator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noises, Registry<Biome> biomeRegistry, BiomeSource biomeSource, long seed) {
            super(structureSets, noises, biomeSource, seed, Holder.direct(generatorSettings()));
            this.biomeRegistry = biomeRegistry;
            this.structureSettings = HolderSet.direct(structureSets.holders().filter(Alfheim.buildAlfheimStructures(biomeRegistry)).toList());
        }

        @Nonnull
        @Override
        protected Codec<? extends ChunkGenerator> codec() {
            return AlfheimChunkGenerator.CODEC;
        }
        
        @Nonnull
        @Override
        public Generator withSeed(long seed) {
            // Called from forge with the actual seed when building the level stem.
            return new Generator(this.structureSets, this.noises, this.biomeRegistry, this.biomeSource, seed);
        }

        @Nonnull
        @Override
        public Stream<Holder<StructureSet>> possibleStructureSets() {
            return this.structureSettings.stream();
        }
    }
}
