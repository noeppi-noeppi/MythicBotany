package mythicbotany.util.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import javax.annotation.Nonnull;

// TODO 1.19.4 use LibX
public class DensityClamp implements DensityFunction {

    public static final KeyDispatchDataCodec<DensityClamp> CODEC = KeyDispatchDataCodec.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("density").forGetter(d -> d.density),
                    Codec.DOUBLE.fieldOf("min").forGetter(d -> d.min),
                    Codec.DOUBLE.fieldOf("max").forGetter(d -> d.max)
            ).apply(instance, DensityClamp::new))
    );

    private final DensityFunction density;
    private final double min;
    private final double max;

    public DensityClamp(DensityFunction density, double min, double max) {
        this.density = density;
        this.min = min;
        this.max = max;
    }

    @Nonnull
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    @Override
    public double compute(@Nonnull FunctionContext context) {
        return Mth.clamp(this.density.compute(context), this.min, this.max);
    }

    @Override
    public void fillArray(@Nonnull double[] array, @Nonnull ContextProvider provider) {
        this.density.fillArray(array, provider);
        for (int i = 0; i < array.length; i++) {
            array[i] = Mth.clamp(array[i], this.min, this.max);
        }
    }

    @Nonnull
    @Override
    public DensityFunction mapAll(@Nonnull Visitor visitor) {
        return new DensityClamp(this.density.mapAll(visitor), this.min, this.max);
    }

    @Override
    public double minValue() {
        return Math.max(this.density.minValue(), this.min);
    }

    @Override
    public double maxValue() {
        return Math.min(this.density.maxValue(), this.max);
    }
}
