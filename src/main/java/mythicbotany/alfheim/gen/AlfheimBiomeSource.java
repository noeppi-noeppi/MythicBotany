package mythicbotany.alfheim.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
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
        private final AlfheimClimateModifier modifier;
        
        public Source(Registry<Biome> biomeRegistry, Climate.ParameterList<Supplier<Biome>> parameters, @Nullable PresetInstance preset) {
            super(parameters, Optional.ofNullable(preset));
            this.biomeRegistry = biomeRegistry;
            this.modifier = new AlfheimClimateModifier(Alfheim.buildAllClimateParameters());
        }
        
        @Nonnull
        @Override
        protected Codec<? extends BiomeSource> codec() {
            return AlfheimBiomeSource.CODEC;
        }

        @Nonnull
        @Override
        public Biome getNoiseBiome(@Nonnull Climate.TargetPoint target) {
            return super.getNoiseBiome(this.modifier.modify(target));
        }

        @Override
        public void addMultinoiseDebugInfo(@Nonnull List<String> list, @Nonnull BlockPos pos, @Nonnull Climate.Sampler sampler) {
            super.addMultinoiseDebugInfo(list, pos, sampler);
            int x = QuartPos.fromBlock(pos.getX());
            int y = QuartPos.fromBlock(pos.getY());
            int z = QuartPos.fromBlock(pos.getZ());
            Climate.TargetPoint target = sampler.sample(x, y, z);
            DecimalFormat format = new DecimalFormat("0.000");
            list.add("multinoise-depth: " + format.format(Climate.unquantizeCoord(target.weirdness())));
            target = this.modifier.modify(target);
            list.add("Alfheim Noise: C: " + format.format(Climate.unquantizeCoord(target.continentalness()))
                    + " E: " + format.format(Climate.unquantizeCoord(target.erosion()))
                    + " T: " + format.format(Climate.unquantizeCoord(target.temperature()))
                    + " H: " + format.format(Climate.unquantizeCoord(target.humidity()))
                    + " W: " + format.format(Climate.unquantizeCoord(target.weirdness()))
                    + " D: " + format.format(Climate.unquantizeCoord(target.depth()))
            );
        }
    }
}
