package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.SurfaceData;
import io.github.noeppi_noeppi.mods.sandbox.surface.SurfaceRuleSet;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import vazkii.botania.common.block.BotaniaBlocks;

public class AlfheimSurface extends SurfaceData {

    @SuppressWarnings("FieldCanBeLocal")
    private final AlfheimBiomes biomes = this.resolve(AlfheimBiomes.class);
    
    public final Holder<SurfaceRuleSet> alfheimSurface = this.ruleSet()
            .beforeBiomes(
                    SurfaceRules.ifTrue(
                            SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)),
                            SurfaceRules.state(Blocks.BEDROCK.defaultBlockState())
                    )
            )
            .afterBiomes(
                    this.defaultAlfheimSurface(Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState())
            )
            .build();
    
    public AlfheimSurface(Properties properties) {
        super(properties);
        this.biome(this.biomes.goldenFields, this.defaultAlfheimSurface(BotaniaBlocks.goldenGrass.defaultBlockState(), Blocks.DIRT.defaultBlockState()));
        this.biome(this.biomes.alfheimLakes, SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(68), 0)), defaultAlfheimSurface(Blocks.SAND.defaultBlockState(), Blocks.GRAVEL.defaultBlockState())));
    }

    private SurfaceRules.RuleSource defaultAlfheimSurface(BlockState top, BlockState below) {
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
}
