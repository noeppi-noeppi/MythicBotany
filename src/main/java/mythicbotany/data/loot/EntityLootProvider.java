package mythicbotany.data.loot;

import mythicbotany.register.ModEntities;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.EntityLootProviderBase;
import vazkii.botania.common.item.BotaniaItems;

public class EntityLootProvider extends EntityLootProviderBase {

    public EntityLootProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setup() {
        this.drops(ModEntities.alfPixie, this.stack(BotaniaItems.pixieDust)
                .with(this.count(0, 1))
                .with(this.looting(2))
        );
    }
}
