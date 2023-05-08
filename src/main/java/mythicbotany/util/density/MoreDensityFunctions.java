package mythicbotany.util.density;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.DensityFunction;

// TODO 1.19.4 use LibX
public class MoreDensityFunctions {
    
    public static DensityFunction smashX(DensityFunction density) {
        return smash(density, Direction.Axis.X);
    }

    public static DensityFunction smashY(DensityFunction density) {
        return smash(density, Direction.Axis.Y);
    }

    public static DensityFunction smashZ(DensityFunction density) {
        return smash(density, Direction.Axis.Z);
    }
    
    public static DensityFunction smash(DensityFunction density, Direction.Axis axis) {
        return new DensitySmash(density, axis);
    }

    public static DensityFunction lerp(DensityFunction a, DensityFunction b, DensityFunction niveau) {
        return lerp(a, b, niveau, 0, 1);
    }
    
    public static DensityFunction lerp(DensityFunction a, DensityFunction b, DensityFunction niveau, double mean, double deviation) {
        return new DensityLerp(a, b, niveau, mean, deviation);
    }
    
    public static DensityFunction clamp(DensityFunction density, double min, double max) {
        return new DensityClamp(density, min, max);
    }
}
