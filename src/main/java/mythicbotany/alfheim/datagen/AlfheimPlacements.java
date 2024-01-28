package mythicbotany.alfheim.datagen;

import mythicbotany.alfheim.worldgen.placement.AlfheimGroundModifier;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.FeatureProviderBase;
import org.moddingx.libx.sandbox.placement.HeightPlacementFilter;

public class AlfheimPlacements extends FeatureProviderBase {
    
    private final AlfheimFeatures features = this.context.findRegistryProvider(AlfheimFeatures.class);

    private final PlacementModifiers trees = this.modifiers()
            .spread()
            .waterDepth(0)
            .heightmap(Heightmap.Types.OCEAN_FLOOR)
            .validGround(Blocks.OAK_SAPLING)
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> metamorphicForestStone = this.metamorphicStone(this.features.metamorphicForestStone);
    public final Holder<PlacedFeature> metamorphicMountainStone = this.metamorphicStone(this.features.metamorphicMountainStone);
    public final Holder<PlacedFeature> metamorphicFungalStone = this.metamorphicStone(this.features.metamorphicFungalStone);
    public final Holder<PlacedFeature> metamorphicSwampStone = this.metamorphicStone(this.features.metamorphicSwampStone);
    public final Holder<PlacedFeature> metamorphicDesertStone = this.metamorphicStone(this.features.metamorphicDesertStone);
    public final Holder<PlacedFeature> metamorphicTaigaStone = this.metamorphicStone(this.features.metamorphicTaigaStone);
    public final Holder<PlacedFeature> metamorphicMesaStone = this.metamorphicStone(this.features.metamorphicMesaStone);
    
    public final Holder<PlacedFeature> looseDreamwoodTrees = this.placement(this.features.dreamwoodTrees)
            .rarity(10)
            .add(this.trees)
            .build();
    
    public final Holder<PlacedFeature> denseDreamwoodTrees = this.placement(this.features.dreamwoodTrees)
            .countExtra(2, 0.1f, 1)
            .add(this.trees)
            .build();
    
    public final Holder<PlacedFeature> motifFlowers = this.placement(this.features.motifFlowers)
            .count(3)
            .spread()
            .add(AlfheimGroundModifier.INSTANCE)
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> alfheimGrass = this.placement(this.features.alfheimGrass)
            .count(15)
            .spread()
            .add(AlfheimGroundModifier.INSTANCE)
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> manaCrystals = this.placement(this.features.manaCrystals)
            .count(1, 4)
            .rarity(2)
            .spread()
            .add(AlfheimGroundModifier.INSTANCE)
            .add(new HeightPlacementFilter(VerticalAnchor.BOTTOM, VerticalAnchor.absolute(84)))
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> abandonedApothecaries = this.placement(this.features.abandonedApothecaries)
            .count(1, 3)
            .rarity(2)
            .spread()
            .add(AlfheimGroundModifier.INSTANCE)
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> wheatFields = this.placement(this.features.wheatFields)
            .count(3)
            .spread()
            .biomeFilter()
            .build();
    
    public final Holder<PlacedFeature> elementiumOre = this.placement(this.features.elementiumOre)
            .add(this.ore(5, VerticalAnchor.bottom(), VerticalAnchor.absolute(70)))
            .build();
    
    public final Holder<PlacedFeature> dragonstoneOre = this.placement(this.features.dragonstoneOre)
            .add(this.ore(1, VerticalAnchor.bottom(), VerticalAnchor.absolute(16)))
            .build();
    
    public final Holder<PlacedFeature> goldOre = this.placement(this.features.goldOre)
            .add(this.ore(2, VerticalAnchor.bottom(), VerticalAnchor.absolute(32)))
            .build();
    
    public final Holder<PlacedFeature> extraGoldOre = this.placement(this.features.goldOre)
            .add(this.ore(5, VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(80)))
            .build();
    
    public AlfheimPlacements(DatagenContext ctx) {
        super(ctx);
    }
    
    private Holder<PlacedFeature> metamorphicStone(Holder<ConfiguredFeature<?, ?>> feature) {
        return this.placement(feature)
                .spread()
                .height(VerticalAnchor.bottom(), VerticalAnchor.top())
                .build();
    }
    
    private PlacementModifiers ore(int count, VerticalAnchor min, VerticalAnchor max) {
        ModifierBuilder builder = this.modifiers();
        if (count > 1) builder.count(count);
        return builder
                .spread()
                .heightTriangle(min, max)
                .build();
    }
}
