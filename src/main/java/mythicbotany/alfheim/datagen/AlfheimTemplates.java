package mythicbotany.alfheim.datagen;

import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.Id;
import org.moddingx.libx.datagen.provider.sandbox.TemplateProviderBase;

public class AlfheimTemplates extends TemplateProviderBase {

    public final Holder<StructureTemplatePool> andwariCave = this.template()
            .single("andwari_cave")
            .build();

    @Id("mystical_flowers")
    public final Holder<StructureTemplatePool> mysticalFlowers = colored(this.template(), "mystical_flowers").build();
    
    @Id("tall_mystical_flowers")
    public final Holder<StructureTemplatePool> tallMysticalFlowers = colored(this.template(), "tall_mystical_flowers").build();
    
    @Id("crops")
    public final Holder<StructureTemplatePool> crops = this.template()
            .single("crops/wheat")
            .single("crops/carrots")
            .single("crops/potatoes")
            .single("crops/beetroot")
            .build();
    
    @Id("elven_houses/gardens")
    public final Holder<StructureTemplatePool> elvenHouseGardens = this.template()
            .empty()
            .single(2, "elven_houses/gardens/flower_garden")
            .single(2, "elven_houses/gardens/crop_garden")
            .build();
    
    @Id("elven_houses/buildings")
    public final Holder<StructureTemplatePool> elvenHouseBuildings = this.template()
            .single(2, "elven_houses/buildings/house")
            .single("elven_houses/buildings/shed")
            .single("elven_houses/buildings/tower")
            .build();
    
    @Id("elven_houses/basement_entrances")
    public final Holder<StructureTemplatePool> elvenHouseBasementEntrances = this.template()
            .empty()
            .build();
    
    public AlfheimTemplates(DatagenContext ctx) {
        super(ctx);
    }
    
    private static PoolBuilder colored(PoolBuilder builder, String prefix) {
        for (DyeColor color : DyeColor.values()) {
            builder.single(prefix + "/" + color.getName());
        }
        return builder;
    }
}
