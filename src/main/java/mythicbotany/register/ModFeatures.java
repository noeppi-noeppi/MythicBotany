package mythicbotany.register;

import mythicbotany.alfheim.worldgen.feature.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "FEATURE")
public class ModFeatures {
    
    public static final Feature<AbandonedApothecaryConfiguration> abandonedApothecaries = new AbandonedApothecaryFeature();
    public static final Feature<NoneFeatureConfiguration> manaCrystals = new ManaCrystalFeature();
    public static final Feature<NoneFeatureConfiguration> motifFlowers = new MotifFlowerFeature();
    public static final Feature<NoneFeatureConfiguration> wheatFields = new WheatFeature();
}
