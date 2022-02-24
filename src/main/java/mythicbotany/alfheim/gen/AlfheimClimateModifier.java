package mythicbotany.alfheim.gen;

import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Climate;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

// Alfheim does not need to cover the full range of the overworld noise sampling
// so we modify the sampled targets so they will always be in the range that
// alfheim biomes use.
public class AlfheimClimateModifier {
    
    private final NoiseParameterRange temperature;
    private final NoiseParameterRange humidity;
    private final NoiseParameterRange continentalness;
    private final NoiseParameterRange erosion;
    private final NoiseParameterRange depth;
    private final NoiseParameterRange weirdness;
    
    public AlfheimClimateModifier(List<Climate.ParameterPoint> biomeData) {
        this.temperature = build("temperature", biomeData, 1, Climate.ParameterPoint::temperature);
        this.humidity = build("humidity", biomeData, 1, Climate.ParameterPoint::humidity);
        this.continentalness = build("continentalness", biomeData, 7, Climate.ParameterPoint::continentalness);
        this.erosion = build("erosion", biomeData, 1, Climate.ParameterPoint::erosion);
        this.depth = build("depth", biomeData, 0.25, Climate.ParameterPoint::depth);
        this.weirdness = build("weirdness", biomeData, 2.5, Climate.ParameterPoint::weirdness);
    }
    
    public Climate.TargetPoint modify(Climate.TargetPoint target) {
        return new Climate.TargetPoint(
                this.temperature.modify(target.temperature()),
                this.humidity.modify(target.humidity()),
                this.continentalness.modify(target.continentalness()),
                this.erosion.modify(target.erosion()),
                this.depth.modify(target.depth()),
                this.weirdness.modify(target.weirdness())
        );
    }
    
    private static NoiseParameterRange build(String name, List<Climate.ParameterPoint> biomeData, double bias, Function<Climate.ParameterPoint, Climate.Parameter> extractor) {
        List<Climate.Parameter> params = biomeData.stream().map(extractor).toList();
        long min = params.stream().map(Climate.Parameter::min).min(Comparator.comparingLong(l -> l)).orElse(-10000l);
        long max = params.stream().map(Climate.Parameter::max).max(Comparator.comparingLong(l -> l)).orElse(10000l);
        if (min == max) throw new IllegalStateException("Fixed value available in climate value: " + name + ". Alfheim can't generate.");
        return new NoiseParameterRange(Math.min(min, max), Math.max(min, max), bias);
    }
    
    
    
    private record NoiseParameterRange(long min, long max, double bias) {
        
        public long modify(long value) {
            if (this.min() == this.max()) return this.min();
            return Mth.clamp(Math.round(this.min() + ((this.max() - this.min()) * Math.pow((value + 10000) / 20000d, this.bias()))), this.min(), this.max());
        }
    }
}
