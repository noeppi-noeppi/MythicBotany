package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.TemplateData;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class AlfheimTemplates extends TemplateData {

    public final Holder<StructureTemplatePool> andwariCave = this.template()
            .single("andwari_cave")
            .build();
    
    public AlfheimTemplates(Properties properties) {
        super(properties);
    }
}
