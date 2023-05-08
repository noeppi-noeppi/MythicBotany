package mythicbotany.util.density;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import javax.annotation.Nonnull;

// TODO 1.19.4 use LibX
public class DensityDebug implements DensityFunction.SimpleFunction {

    public static final KeyDispatchDataCodec<DensityDebug> CODEC = KeyDispatchDataCodec.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Direction.Axis.CODEC.fieldOf("axis").forGetter(d -> d.axis),
                    Codec.DOUBLE.fieldOf("scale").forGetter(d -> d.scale)
            ).apply(instance, DensityDebug::new))
    );

    private final Direction.Axis axis;
    private final double scale;

    public DensityDebug(Direction.Axis axis, double scale) {
        this.axis = axis;
        this.scale = scale;
    }

    @Nonnull
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    @Override
    public double compute(@Nonnull FunctionContext context) {
        return this.scale * switch (this.axis) {
            case X -> context.blockX();
            case Y -> context.blockY();
            case Z -> context.blockZ();
        };
    }

    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }
}
