package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.StructureData;
import mythicbotany.register.tags.ModBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

public class AlfheimStructures extends StructureData {

    private final AlfheimTemplates templates = this.resolve(AlfheimTemplates.class);
    
    public final Holder<Structure> andwariCave = this.jigsaw(templates.andwariCave)
            .height(Heightmap.Types.WORLD_SURFACE_WG, -6)
            .structure()
            .step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
            .biomes(ModBiomeTags.ANDWARI_CAVE)
            .build();
    
    public AlfheimStructures(Properties properties) {
        super(properties);
    }
}
