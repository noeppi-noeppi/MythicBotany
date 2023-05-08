package mythicbotany.util.density;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.blending.Blender;

import javax.annotation.Nonnull;

// TODO 1.19.4 use LibX
public class DensitySmash implements DensityFunction {

    public static final KeyDispatchDataCodec<DensitySmash> CODEC = KeyDispatchDataCodec.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("density").forGetter(d -> d.wrapped),
                    Direction.Axis.CODEC.fieldOf("axis").forGetter(d -> d.axis)
            ).apply(instance, DensitySmash::new))
    );

    private final DensityFunction wrapped;
    private final Direction.Axis axis;

    private final SmashedContext context;
    private final SmashedProvider provider;

    public DensitySmash(DensityFunction wrapped, Direction.Axis axis) {
        this.wrapped = wrapped;
        this.axis = axis;

        this.context = new SmashedContext();
        this.provider = new SmashedProvider();
    }

    @Nonnull
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC;
    }

    @Override
    public double compute(@Nonnull FunctionContext context) {
        this.context.parent = context;
        return this.wrapped.compute(this.context);
    }

    @Override
    public void fillArray(@Nonnull double[] array, @Nonnull ContextProvider provider) {
        this.provider.parent = provider;
        this.wrapped.fillArray(array, provider);
    }

    @Nonnull
    @Override
    public DensityFunction mapAll(@Nonnull Visitor visitor) {
        return new DensitySmash(this.wrapped.mapAll(visitor), this.axis);
    }

    @Override
    public double minValue() {
        return this.wrapped.minValue();
    }

    @Override
    public double maxValue() {
        return this.wrapped.maxValue();
    }

    @Nonnull
    @Override
    public DensityFunction clamp(double minValue, double maxValue) {
        return new DensitySmash(this.wrapped.clamp(minValue, maxValue), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction abs() {
        return new DensitySmash(this.wrapped.abs(), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction square() {
        return new DensitySmash(this.wrapped.square(), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction cube() {
        return new DensitySmash(this.wrapped.cube(), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction halfNegative() {
        return new DensitySmash(this.wrapped.halfNegative(), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction quarterNegative() {
        return new DensitySmash(this.wrapped.quarterNegative(), this.axis);
    }

    @Nonnull
    @Override
    public DensityFunction squeeze() {
        return new DensitySmash(this.wrapped.squeeze(), this.axis);
    }

    private class SmashedContext implements FunctionContext {

        private FunctionContext parent;

        @Override
        public int blockX() {
            return DensitySmash.this.axis == Direction.Axis.X ? 0 : this.parent.blockX();
        }

        @Override
        public int blockY() {
            return DensitySmash.this.axis == Direction.Axis.Y ? 0 : this.parent.blockY();
        }

        @Override
        public int blockZ() {
            return DensitySmash.this.axis == Direction.Axis.Z ? 0 : this.parent.blockZ();
        }

        @Nonnull
        @Override
        public Blender getBlender() {
            return this.parent.getBlender();
        }
    }

    private class SmashedProvider implements ContextProvider {

        private ContextProvider parent;

        @Nonnull
        @Override
        public FunctionContext forIndex(int arrayIndex) {
            DensitySmash.this.context.parent = this.parent.forIndex(arrayIndex);
            return DensitySmash.this.context;
        }

        @Override
        public void fillAllDirectly(@Nonnull double[] values, @Nonnull DensityFunction function) {
            for(int i = 0; i < values.length; i++) {
                values[i] = function.compute(this.forIndex(i));
            }
        }
    }
}
