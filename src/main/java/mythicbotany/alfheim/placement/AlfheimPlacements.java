package mythicbotany.alfheim.placement;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@RegisterClass(prefix = "wg", priority = -3)
public class AlfheimPlacements {
    
    public static final PlacedFeature metamorphicForestStone = AlfheimFeatures.metamorphicForestStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicMountainStone = AlfheimFeatures.metamorphicMountainStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicFungalStone = AlfheimFeatures.metamorphicFungalStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicSwampStone = AlfheimFeatures.metamorphicSwampStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicDesertStone = AlfheimFeatures.metamorphicDesertStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicTaigaStone = AlfheimFeatures.metamorphicTaigaStone.placed(AlfheimModifiers.metamorphicStone);
    public static final PlacedFeature metamorphicMesaStone = AlfheimFeatures.metamorphicMesaStone.placed(AlfheimModifiers.metamorphicStone);

    public static final PlacedFeature looseDreamwoodTrees = AlfheimFeatures.dreamwoodTrees.placed(AlfheimModifiers.looseTrees);
    public static final PlacedFeature denseDreamwoodTrees = AlfheimFeatures.dreamwoodTrees.placed(AlfheimModifiers.denseTrees);

    public static final PlacedFeature motifFlowers = AlfheimFeatures.motifFlowers.placed();
    public static final PlacedFeature alfheimGrass = AlfheimFeatures.alfheimGrass.placed(AlfheimModifiers.vegetation);
    public static final PlacedFeature manaCrystals = AlfheimFeatures.manaCrystals.placed();
    public static final PlacedFeature abandonedApothecaries = AlfheimFeatures.abandonedApothecaries.placed();
    public static final PlacedFeature elementiumOre = AlfheimFeatures.elementiumOre.placed(AlfheimModifiers.ore(5, VerticalAnchor.bottom(), VerticalAnchor.absolute(70)));
    public static final PlacedFeature dragonstoneOre = AlfheimFeatures.dragonstoneOre.placed(AlfheimModifiers.ore(1, VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(16)));
    public static final PlacedFeature goldOre = AlfheimFeatures.goldOre.placed(AlfheimModifiers.ore(2, VerticalAnchor.bottom(), VerticalAnchor.absolute(32)));
    public static final PlacedFeature extraGoldOre = AlfheimFeatures.goldOre.placed(AlfheimModifiers.ore(20, VerticalAnchor.aboveBottom(16), VerticalAnchor.absolute(80)));
    public static final PlacedFeature wheatFields = AlfheimFeatures.wheatFields.placed();
}
