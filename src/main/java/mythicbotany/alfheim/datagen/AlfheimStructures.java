package mythicbotany.alfheim.datagen;

import mythicbotany.register.tags.ModWorldGenTags;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.StructureProviderBase;

public class AlfheimStructures extends StructureProviderBase {

    private final AlfheimTemplates templates = this.context.findRegistryProvider(AlfheimTemplates.class);
    
    public final Holder<Structure> andwariCave = this.jigsaw(templates.andwariCave)
            .height(Heightmap.Types.WORLD_SURFACE_WG, -6)
            .structure()
            .step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
            .biomes(ModWorldGenTags.ANDWARI_CAVE)
            .build();
    
    public AlfheimStructures(DatagenContext ctx) {
        super(ctx);
    }
}
