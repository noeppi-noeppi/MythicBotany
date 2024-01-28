package mythicbotany.alfheim.datagen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.StructureSetProviderBase;

public class AlfheimStructureSets extends StructureSetProviderBase {

    private final AlfheimStructures structures = this.context.findRegistryProvider(AlfheimStructures.class);
    
    public final Holder<StructureSet> andwariCave = this.structureSet()
            .entry(this.structures.andwariCave)
            .placeRandom(28, 8)
            .frequency(1)
            .build();
    
    public AlfheimStructureSets(DatagenContext ctx) {
        super(ctx);
    }
}
