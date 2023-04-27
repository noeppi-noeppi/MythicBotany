package mythicbotany.alfheim.placement;

import org.moddingx.libx.annotation.registration.RegisterClass;
import mythicbotany.register.HackyHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

@RegisterClass(prefix = "wg", priority = -3)
public class AlfheimPlacements {
    
    public static final Holder<PlacedFeature> metamorphicForestStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicForestStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicMountainStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicMountainStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicFungalStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicFungalStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicSwampStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicSwampStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicDesertStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicDesertStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicTaigaStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicTaigaStone, AlfheimModifiers.metamorphicStone));
    public static final Holder<PlacedFeature> metamorphicMesaStone = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.metamorphicMesaStone, AlfheimModifiers.metamorphicStone));

    public static final Holder<PlacedFeature> looseDreamwoodTrees = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.dreamwoodTrees, AlfheimModifiers.looseTrees));
    public static final Holder<PlacedFeature> denseDreamwoodTrees = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.dreamwoodTrees, AlfheimModifiers.denseTrees));

    public static final Holder<PlacedFeature> motifFlowers = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.motifFlowers, List.of()));
    public static final Holder<PlacedFeature> alfheimGrass = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.alfheimGrass, AlfheimModifiers.vegetation));
    public static final Holder<PlacedFeature> manaCrystals = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.manaCrystals, List.of()));
    public static final Holder<PlacedFeature> abandonedApothecaries = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.abandonedApothecaries, List.of()));
    public static final Holder<PlacedFeature> elementiumOre = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.elementiumOre, AlfheimModifiers.ore(5, VerticalAnchor.bottom(), VerticalAnchor.absolute(70))));
    public static final Holder<PlacedFeature> dragonstoneOre = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.dragonstoneOre, AlfheimModifiers.ore(1, VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(16))));
    public static final Holder<PlacedFeature> goldOre = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.goldOre, AlfheimModifiers.ore(2, VerticalAnchor.bottom(), VerticalAnchor.absolute(32))));
    public static final Holder<PlacedFeature> extraGoldOre = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.goldOre, AlfheimModifiers.ore(20, VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(80))));
    public static final Holder<PlacedFeature> wheatFields = new HackyHolder<>(Registry.PLACED_FEATURE_REGISTRY, new PlacedFeature(AlfheimFeatures.wheatFields, List.of()));

    public static final Holder<StructureSet> andwariCave = new HackyHolder<>(Registry.STRUCTURE_SET_REGISTRY, new StructureSet(AlfheimFeatures.andwariCave, new RandomSpreadStructurePlacement(28, 8, RandomSpreadType.LINEAR, Math.abs("andwari_cave".hashCode()))));
}
