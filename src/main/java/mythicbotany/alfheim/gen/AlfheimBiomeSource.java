package mythicbotany.alfheim.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class AlfheimBiomeSource {

    public static final Codec<Source> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry)
    ).apply(instance, instance.stable(AlfheimBiomeSource::createSource)));
    
    private static Source createSource(Registry<Biome> biomeRegistry) {
        return new Source(biomeRegistry, parameters(biomeRegistry), preset(biomeRegistry));
    }
    
    private static Climate.ParameterList<Supplier<Biome>> parameters(Registry<Biome> biomeRegistry) {
        return Alfheim.buildAlfheimClimate(biomeRegistry::get);
    }
    
    private static MultiNoiseBiomeSource.PresetInstance preset(Registry<Biome> biomeRegistry) {
        return new MultiNoiseBiomeSource.PresetInstance(new MultiNoiseBiomeSource.Preset(Alfheim.DIMENSION.location(), AlfheimBiomeSource::parameters), biomeRegistry);
    }
    
    public static class Source extends MultiNoiseBiomeSource {

        private final Registry<Biome> biomeRegistry;
        
        public Source(Registry<Biome> biomeRegistry, Climate.ParameterList<Supplier<Biome>> parameters, @Nullable PresetInstance preset) {
            super(parameters, Optional.ofNullable(preset));
            this.biomeRegistry = biomeRegistry;
        }
        
        @Nonnull
        @Override
        protected Codec<? extends BiomeSource> codec() {
            return AlfheimBiomeSource.CODEC;
        }
    }
}
