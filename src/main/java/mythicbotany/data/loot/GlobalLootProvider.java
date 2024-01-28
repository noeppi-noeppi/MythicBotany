package mythicbotany.data.loot;

import mythicbotany.MythicBotany;
import mythicbotany.loot.AlfsteelDisposeModifier;
import mythicbotany.loot.FimbultyrModifier;
import net.minecraft.world.entity.EntityType;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.GlobalLootProviderBase;

public class GlobalLootProvider extends GlobalLootProviderBase {

    public GlobalLootProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    protected void setup() {
        this.add("dispose", new AlfsteelDisposeModifier());
        this.add("fimbultyr", new FimbultyrModifier());
        this.addition("alfsteel_template", MythicBotany.getInstance().resource("entity_addition/alfsteel_template")).or(this.conditions()
                .forLootTable(EntityType.WITCH.getDefaultLootTable())
                .forLootTable(EntityType.EVOKER.getDefaultLootTable())
        ).build();
    }
}
