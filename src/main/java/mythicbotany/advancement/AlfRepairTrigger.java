package mythicbotany.advancement;

import com.google.gson.JsonObject;
import mythicbotany.MythicBotany;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class AlfRepairTrigger extends AbstractCriterionTrigger<AlfRepairTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(MythicBotany.getInstance().modid, "alf_repair");
    
    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    @Override
    protected Instance deserializeTrigger(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate player, @Nonnull ConditionArrayParser conditions) {
        return new Instance(player, ItemPredicate.deserialize(json.get("item")));
    }

    public void trigger(ServerPlayerEntity player, ItemStack item) {
        this.triggerListeners(player, (instance) -> instance.item.test(item));
    }

    public static class Instance extends CriterionInstance {

        public final ItemPredicate item;

        public Instance(ItemPredicate item) {
            this(EntityPredicate.AndPredicate.ANY_AND, item);
        }
        
        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item) {
            super(AlfRepairTrigger.ID, player);
            this.item = item;
        }
        
        @Nonnull
        public JsonObject serialize(@Nonnull ConditionArraySerializer conditions) {
            JsonObject json = super.serialize(conditions);
            json.add("item", this.item.serialize());
            return json;
        }
    }
}
