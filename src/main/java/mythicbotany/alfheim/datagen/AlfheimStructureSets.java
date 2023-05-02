package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.StructureSetData;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class AlfheimStructureSets extends StructureSetData {

    private final AlfheimStructures structures = this.resolve(AlfheimStructures.class);
    
    public final Holder<StructureSet> andwariCave = this.structureSet()
            .entry(this.structures.andwariCave)
            .placeRandom(28, 8)
            .frequency(1)
            .build();
    
    public AlfheimStructureSets(Properties properties) {
        super(properties);
    }
}
