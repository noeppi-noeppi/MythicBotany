package mythicbotany.alfheim.datagen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.dimension.DimensionType;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.sandbox.DimensionTypeProviderBase;

public class AlfheimDimensionTypes extends DimensionTypeProviderBase {

    public final Holder<DimensionType> alfheim = this.dimension()
            .disableRaids()
            .build();
            
    public AlfheimDimensionTypes(DatagenContext ctx) {
        super(ctx);
    }
}
