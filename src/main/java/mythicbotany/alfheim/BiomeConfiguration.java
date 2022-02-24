package mythicbotany.alfheim;

import mythicbotany.alfheim.surface.BiomeTemplates;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import java.util.function.Consumer;

public class BiomeConfiguration {

    private Climate.Parameter temperature = Climate.Parameter.span(0.89f, 0.91f);
    private Climate.Parameter humidity = Climate.Parameter.span(0.89f, 0.91f);
    private Climate.Parameter continentalness = Climate.Parameter.span(-0.11f, 0.06f);
    private Climate.Parameter erosion = Climate.Parameter.span(-0.2f, 0.2f);
    private Climate.Parameter depth = Climate.Parameter.span(0.025f, 0.05f);
    private Climate.Parameter weirdness = Climate.Parameter.span(-0.2f, 0.2f);
    private SurfaceRules.RuleSource surface = normalAlfheimSurface(Blocks.GRASS_BLOCK, Blocks.DIRT);

    public BiomeConfiguration() {

    }

    public BiomeConfiguration(Template template) {
        template.action.accept(this);
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
        return this.surface(normalAlfheimSurface(top, below));
    }

    public BiomeConfiguration surface(BlockState top, BlockState below) {
        return this.surface(normalAlfheimSurface(top, below));
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
    
    public static SurfaceRules.RuleSource normalAlfheimSurface(Block top, Block below) {
        return normalAlfheimSurface(top.defaultBlockState(), below.defaultBlockState());
    }
    
    public static SurfaceRules.RuleSource normalAlfheimSurface(BlockState top, BlockState below) {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0),
                        SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(-1, 0)), SurfaceRules.state(below)),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(top)),
                                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.steep()), SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.state(below))
                                ))
                        ))
                ),
                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0)),
                        SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(-1, 0)), SurfaceRules.state(below)),
                                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.steep()), SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(below))
                                ))
                        ))
                )
        );
    }
    
    public enum Template {
        
        DEFAULT(gen -> {}),
        OCEAN(BiomeTemplates::ocean),
        LAKE(BiomeTemplates::lake),
        FLAT(BiomeTemplates::flat),
        MODERATE(BiomeTemplates::moderate),
        HILLS(BiomeTemplates::hills),
        MOUNTAINS(BiomeTemplates::mountains);
        
        private final Consumer<BiomeConfiguration> action;

        Template(Consumer<BiomeConfiguration> action) {
            this.action = action;
        }
    }
}
