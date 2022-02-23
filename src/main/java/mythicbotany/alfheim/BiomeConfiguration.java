package mythicbotany.alfheim;

import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class BiomeConfiguration {

    private Climate.Parameter temperature = Climate.Parameter.point(0.9f);
    private Climate.Parameter humidity = Climate.Parameter.point(1);
    private Climate.Parameter continentalness = Climate.Parameter.span(-0.11f, 0.06f);
    private Climate.Parameter erosion = Climate.Parameter.span(-0.2f, 0.2f);
    private Climate.Parameter depth = Climate.Parameter.span(0.025f, 0.05f);
    private Climate.Parameter weirdness = Climate.Parameter.span(-0.2f, 0.2f);
    private SurfaceRules.RuleSource surface = SurfaceRules.state(Blocks.GRASS.defaultBlockState());

    public BiomeConfiguration() {
    
    }

    public BiomeConfiguration temperature(float value) {
        this.temperature = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration temperature(float min, float max) {
        this.temperature = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration humidity(float value) {
        this.humidity = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration humidity(float min, float max) {
        this.humidity = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration continentalness(float value) {
        this.continentalness = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration continentalness(float min, float max) {
        this.continentalness = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration erosion(float value) {
        this.erosion = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration erosion(float min, float max) {
        this.erosion = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration depth(float value) {
        this.depth = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration depth(float min, float max) {
        this.depth = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration weirdness(float value) {
        this.weirdness = Climate.Parameter.point(value);
        return this;
    }

    public BiomeConfiguration weirdness(float min, float max) {
        this.weirdness = Climate.Parameter.span(min, max);
        return this;
    }

    public BiomeConfiguration surface(Block top, Block below) {
        return surface(top.defaultBlockState(), below.defaultBlockState());
    }

    public BiomeConfiguration surface(BlockState top, BlockState below) {
        return this.surface(SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), SurfaceRules.state(below)),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(top)),
                SurfaceRules.state(below)
        ));
    }

    public BiomeConfiguration surface(SurfaceRules.RuleSource rule) {
        this.surface = rule;
        return this;
    }

    public Climate.ParameterPoint buildClimate() {
        return new Climate.ParameterPoint(
                this.temperature, this.humidity, this.continentalness,
                this.erosion, this.depth, this.weirdness, 0
        );
    }

    public SurfaceRules.RuleSource buildSurface() {
        return this.surface;
    }
}
