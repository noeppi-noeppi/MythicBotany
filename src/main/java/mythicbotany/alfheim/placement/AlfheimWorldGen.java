package mythicbotany.alfheim.placement;

import io.github.noeppi_noeppi.libx.annotation.registration.NoReg;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.ModBlockTags;
import mythicbotany.alfheim.feature.AbandonedApothecaryFeature;
import mythicbotany.alfheim.feature.ManaCrystalFeature;
import mythicbotany.alfheim.feature.MotifFlowerFeature;
import mythicbotany.alfheim.feature.WheatFeature;
import mythicbotany.alfheim.structure.AndwariCave;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

@RegisterClass
public class AlfheimWorldGen {
    
    public static final Feature<NoneFeatureConfiguration> motifFlowers = new MotifFlowerFeature();
    public static final Feature<NoneFeatureConfiguration> manaCrystals = new ManaCrystalFeature();
    public static final Feature<NoneFeatureConfiguration> abandonedApothecaries = new AbandonedApothecaryFeature();
    public static final Feature<NoneFeatureConfiguration> wheatFields = new WheatFeature();

    public static final StructureFeature<JigsawConfiguration> andwariCave = new AndwariCave();

    @NoReg public static final RuleTest livingrock = new BlockMatchTest(vazkii.botania.common.block.ModBlocks.livingrock);
    @NoReg public static final RuleTest alfheimStone = new TagMatchTest(ModBlockTags.BASE_STONE_ALFHEIM);
    
    @NoReg public static final JigsawConfiguration dummyJigsaw = new JigsawConfiguration(PlainVillagePools.START, 0);
}
