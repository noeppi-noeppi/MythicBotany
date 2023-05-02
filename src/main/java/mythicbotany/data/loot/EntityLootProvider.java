package mythicbotany.data.loot;

import mythicbotany.register.ModEntities;
import net.minecraft.data.DataGenerator;
import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.loot.EntityLootProviderBase;
import org.moddingx.libx.mod.ModX;
import vazkii.botania.common.item.BotaniaItems;

@Datagen
public class EntityLootProvider extends EntityLootProviderBase {

    public EntityLootProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
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
