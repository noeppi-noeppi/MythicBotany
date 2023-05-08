package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.NoiseData;
import mythicbotany.util.density.MoreDensityFunctions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CubicSpline;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import vazkii.botania.common.block.BotaniaBlocks;

public class AlfheimNoise extends NoiseData {

    // Amount of blocks, the alfheim base height can influence the variation noise shift
    private static final double HEIGHT_FACTOR = 224;
    
    // Factor for weirdness to influence squashing
    private static final double WEIRDNESS_FACTOR = 0.03;
    
    // Minimum amount of squashing
    private static final double WEIRDNESS_SHIFT = 7;

    // Basic noise to sample terrain height
    public final Holder<DensityFunction> alfheimContinentalness = this.density(DensityFunctions.cache2d(this.shifted(Noises.BADLANDS_SURFACE, 0.088)));
    
    // Noise to lerp between terrain shapes
    public final Holder<DensityFunction> alfheimErosion = this.density(DensityFunctions.cache2d(this.shifted(Noises.CONTINENTALNESS, 2)));

    // Terrain shaper for low erosion (beaches, plateaus)
    public final Holder<DensityFunction> alfheimLow = this.density(DensityFunctions.spline(
            CubicSpline.builder(new DensityFunctions.Spline.Coordinate(this.alfheimContinentalness))
                    .addPoint(-1, -0.8f)
                    .addPoint(-0.4f, -0.2f, 1)
                    .addPoint(0, 0)
                    .addPoint(0.2f, 0.02f)
                    .addPoint(0.25f, 0.095f)
                    .addPoint(0.37f, 0.1f)
                    .addPoint(0.38f, 0.18f)
                    .addPoint(0.42f, 0.18f)
                    .addPoint(0.58f, 0.38f)
                    .addPoint(0.65f, 0.38f)
                    .addPoint(0.75f, 0.7f)
                    .addPoint(1, 0.7f)
                    .build()
    ));
    
    // Terrain shaper for high erosion (mountains, deep water)
    public final Holder<DensityFunction> alfheimHigh = this.density(DensityFunctions.spline(
            CubicSpline.builder(new DensityFunctions.Spline.Coordinate(this.alfheimContinentalness))
                    .addPoint(-1, -1)
                    .addPoint(-0.7f, -0.9f)
                    .addPoint(-0.25f, -0.5f, 2)
                    .addPoint(-0.1f, -0.1f)
                    .addPoint(0, 0)
                    .addPoint(0.3f, 0.25f, 1)
                    .addPoint(0.8f, 1, 0.5f)
                    .addPoint(0.9f, 0.9f)
                    .addPoint(1, 1)
                    .build()
    ));
    
    // Base terrain height. Combines low and high shaper (slightly biased towards the low shaper)
    public final Holder<DensityFunction> alfheimHeight = this.density(MoreDensityFunctions.smashY(
            DensityFunctions.interpolated(MoreDensityFunctions.lerp(
                    new DensityFunctions.HolderHolder(alfheimLow),
                    new DensityFunctions.HolderHolder(alfheimHigh),
                    new DensityFunctions.HolderHolder(alfheimErosion),
                    0.15, 0.4
            ))
    ));

    // Influences squashing for terrain 3d noise. -1 is maximum squashing, 1 is minimum squashing
    public final Holder<DensityFunction> alfheimWeirdness = this.density(this.clampNormal(DensityFunctions.mul(
            this.shifted(Noises.RIDGE, 0.25),
            DensityFunctions.constant(2)
    )));

    // Surface 3d noise
    public final Holder<DensityFunction> alfheimVariation = this.density(DensityFunctions.noise(this.holder(Noises.SURFACE)));

    // Combines height and variation into an initial surface density
    public final Holder<DensityFunction> alfheimInitial = this.density(DensityFunctions.add(
            new DensityFunctions.HolderHolder(alfheimVariation),
            DensityFunctions.mul(DensityFunctions.constant(-1 / WEIRDNESS_FACTOR), DensityFunctions.mul(
                    DensityFunctions.mul(
                            DensityFunctions.add(
                                    DensityFunctions.add(
                                            DensityFunctions.yClampedGradient(-64, 320, 64 / HEIGHT_FACTOR, -320 / HEIGHT_FACTOR),
                                            this.clampNormal(new DensityFunctions.HolderHolder(alfheimHeight).quarterNegative())
                                    ),
                                    DensityFunctions.constant(64 / HEIGHT_FACTOR)
                            ),
                            DensityFunctions.constant(-HEIGHT_FACTOR / 256d)
                    ),
                    DensityFunctions.add(
                            DensityFunctions.mul(
                                    DensityFunctions.add(
                                            this.clampNormal(new DensityFunctions.HolderHolder(alfheimWeirdness)),
                                            DensityFunctions.constant(-1)
                                    ),
                                    DensityFunctions.constant(-0.5)
                            ),
                            DensityFunctions.constant(WEIRDNESS_SHIFT * WEIRDNESS_FACTOR)
                    )
            ))
    ));
    
    // Measure for how deep a point is below the surface. Higher values mean deeper.
    public final Holder<DensityFunction> alfheimDepth = this.density(DensityFunctions.add(
            DensityFunctions.yClampedGradient(-64, 320, 0.5, -1),
            this.clampNormal(new DensityFunctions.HolderHolder(alfheimHeight))
    ));
    
    // Currently unused, might be used in the future if more biomes are added
    public final Holder<DensityFunction> alfheimTemperature = this.density(this.clampNormal(DensityFunctions.zero()));
    public final Holder<DensityFunction> alfheimHumidity = this.density(this.clampNormal(DensityFunctions.zero()));

    // Caves. Values less than 0 will be empty blocks.
    public final Holder<DensityFunction> alfheimCaves = this.density(DensityFunctions.interpolated(NoiseRouterData.underground(
            this.registries.registry(Registry.DENSITY_FUNCTION_REGISTRY),
            NoiseRouterData.entrances(this.registries.registry(Registry.DENSITY_FUNCTION_REGISTRY))
    )));
    
    public final Holder<DensityFunction> alfheimFinal = this.density(DensityFunctions.min(
            new DensityFunctions.HolderHolder(this.alfheimInitial),
            new DensityFunctions.HolderHolder(this.alfheimCaves)
    ));

    public final Holder<NoiseGeneratorSettings> alfheim = this.generator()
            .defaultBlock(BotaniaBlocks.livingrock)
            .disableOreVeins()
            .router().initialDensityWithoutJaggedness(this.alfheimInitial)
            .router().finalDensity(this.alfheimFinal)
            .router().continents(this.alfheimContinentalness)
            .router().erosion(this.alfheimErosion)
            .router().ridges(this.alfheimWeirdness)
            .router().depth(this.alfheimDepth)
            .router().temperature(this.alfheimTemperature)
            .router().vegetation(this.alfheimHumidity)
            .build();

    public AlfheimNoise(Properties properties) {
        super(properties);
    }

    private DensityFunction shifted(ResourceKey<NormalNoise.NoiseParameters> noise, double scale) {
        return shifted(this.holder(noise), scale);
    }

    private DensityFunction shifted(Holder<NormalNoise.NoiseParameters> noise, double scale) {
        return DensityFunctions.shiftedNoise2d(
                DensityFunctions.cacheOnce(DensityFunctions.shiftA(this.holder(Noises.SHIFT))),
                DensityFunctions.cacheOnce(DensityFunctions.shiftB(this.holder(Noises.SHIFT))),
                scale, noise
        );
    }

    private DensityFunction clampNormal(DensityFunction density) {
        return MoreDensityFunctions.clamp(density, -1, 1);
    }
}
