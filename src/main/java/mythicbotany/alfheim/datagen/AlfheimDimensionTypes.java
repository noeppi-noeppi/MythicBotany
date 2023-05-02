package mythicbotany.alfheim.datagen;

import io.github.noeppi_noeppi.mods.sandbox.datagen.ext.DimensionTypeData;
import net.minecraft.core.Holder;
import net.minecraft.world.level.dimension.DimensionType;

public class AlfheimDimensionTypes extends DimensionTypeData {

    public final Holder<DimensionType> alfheim = this.dimension()
            .disableRaids()
            .build();
            
    public AlfheimDimensionTypes(Properties properties) {
        super(properties);
    }
}
