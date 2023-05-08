package mythicbotany.util.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;

import javax.annotation.Nonnull;

// TODO 1.19.4 use LibX
public class DensityLerp implements DensityFunction {

    public static final KeyDispatchDataCodec<DensityLerp> CODEC = KeyDispatchDataCodec.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(d -> d.argument1),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(d -> d.argument2),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("niveau").forGetter(d -> d.niveau),
                    Codec.DOUBLE.optionalFieldOf("mean", 0d).forGetter(d -> d.mean),
                    Codec.DOUBLE.optionalFieldOf("deviation", 1d).forGetter(d -> d.deviation)
            ).apply(instance, DensityLerp::new))
    );

    private final DensityFunction argument1;
    private final DensityFunction argument2;
    private final DensityFunction niveau;
    private final double mean;
    private final double deviation;

    public DensityLerp(DensityFunction argument1, DensityFunction argument2, DensityFunction niveau, double mean, double deviation) {
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.niveau = niveau;
        this.mean = mean;
        this.deviation = deviation;
    }


    @Nonnull
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    @Override
    public double compute(@Nonnull FunctionContext context) {
        double a = this.argument1.compute(context);
        double b = this.argument2.compute(context);
        double n = Mth.clamp(this.niveau.compute(context), this.mean - this.deviation, this.mean + this.deviation);
        return Mth.lerp(((n - this.mean) / (2 * this.deviation)) + 0.5, a, b);
    }

    @Override
    public void fillArray(@Nonnull double[] array, @Nonnull ContextProvider provider) {
        double[] a = new double[array.length];
        double[] b = new double[array.length];
        this.niveau.fillArray(array, provider);
        this.argument1.fillArray(a, provider);
        this.argument2.fillArray(b, provider);
        double n;
        for (int i = 0; i < array.length; i++) {
            n = Mth.clamp(array[i], this.mean - this.deviation, this.mean + this.deviation);
            array[i] = Mth.lerp(((n - this.mean) / (2 * this.deviation)) + 0.5, a[i], b[i]);
        }
    }

    @Nonnull
    @Override
    public DensityFunction mapAll(@Nonnull Visitor visitor) {
        return new DensityLerp(this.argument1.mapAll(visitor), this.argument2.mapAll(visitor), this.niveau.mapAll(visitor), this.mean, this.deviation);
    }

    @Override
    public double minValue() {
        return Math.min(this.argument1.minValue(), this.argument2.minValue());
    }

    @Override
    public double maxValue() {
        return Math.max(this.argument1.maxValue(), this.argument2.maxValue());
    }
}
