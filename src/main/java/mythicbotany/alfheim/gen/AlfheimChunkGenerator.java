package mythicbotany.alfheim.gen;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

public class AlfheimChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.create(Registry.NOISE_REGISTRY).forGetter(generator -> generator.noises),
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
            Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter(generator -> generator.seed),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings)
    ).apply(instance, instance.stable((n, b, s, k) -> new NoiseBasedChunkGenerator(n, b, s, () -> applyStructures(k.get())))));

    private static NoiseGeneratorSettings applyStructures(NoiseGeneratorSettings settings) {
        //noinspection deprecation
        return new NoiseGeneratorSettings(
                new AlfheimStructureSettings(settings.structureSettings()),
                settings.noiseSettings(),
                settings.getDefaultBlock(),
                settings.getDefaultFluid(),
                Alfheim.buildAlfheimSurface(),
                settings.seaLevel(),
                settings.disableMobGeneration(),
                settings.isAquifersEnabled(),
                settings.isNoiseCavesEnabled(),
                settings.isOreVeinsEnabled(),
                settings.isNoodleCavesEnabled(),
                settings.useLegacyRandomSource()
        );
    }
    
    private static class AlfheimStructureSettings extends StructureSettings {
        
        private final Map<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> biomeMap;
        
        public AlfheimStructureSettings(StructureSettings settings) {
            super(Optional.ofNullable(settings.stronghold()), Alfheim.buildAlfheimStructures());
            this.biomeMap = Alfheim.buildAlfheimStructurePlacement();
        }

        @Nonnull
        @Override
        public ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> structures(@Nonnull StructureFeature<?> structure) {
            return this.biomeMap.getOrDefault(structure, super.structures(structure));
        }
    }
}
