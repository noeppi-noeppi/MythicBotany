package mythicbotany.alfheim.datagen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.TemplateProviderBase;

public class AlfheimTemplates extends TemplateProviderBase {

    public final Holder<StructureTemplatePool> andwariCave = this.template()
            .single("andwari_cave")
            .build();
    
    public AlfheimTemplates(DatagenContext ctx) {
        super(ctx);
    }
}
