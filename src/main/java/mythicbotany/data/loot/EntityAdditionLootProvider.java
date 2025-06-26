package mythicbotany.data.loot;

import mythicbotany.register.ModItems;
import mythicbotany.register.tags.ModItemTags;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.LootProviderBase;

import javax.annotation.Nullable;

public class EntityAdditionLootProvider extends LootProviderBase<String> {
    
    public EntityAdditionLootProvider(DatagenContext ctx) {
        super(ctx, "entity_addition", LootContextParamSets.ENTITY, ctx.mod()::resource);
    }
    
    @Override
    protected void setup() {
        this.drops("alfsteel_template", this.stack(ModItems.alfsteelTemplate)
                .with(LootItemKilledByPlayerCondition.killedByPlayer())
                .with(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.DIRECT_KILLER, new EntityPredicate.Builder()
                        .equipment(EntityEquipmentPredicate.Builder.equipment()
                                .mainhand(ItemPredicate.Builder.item()
                                        .of(ModItemTags.ELEMENTIUM_WEAPONS).build()
                                ).build()
                        )
                ))
                .with(this.random(0.15f))
        );
    }

    @Nullable
    @Override
    protected LootTable.Builder defaultBehavior(String item) {
        return null;
    }
}
