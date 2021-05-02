package mythicbotany.advancement;

import com.google.gson.JsonObject;
import mythicbotany.MythicBotany;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MjoellnirTrigger extends AbstractCriterionTrigger<MjoellnirTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(MythicBotany.getInstance().modid, "mjoellnir");
    
    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    @Override
    protected Instance deserializeTrigger(@Nonnull JsonObject json, @Nonnull EntityPredicate.AndPredicate player, @Nonnull ConditionArrayParser conditions) {
        return new Instance(player, ItemPredicate.deserialize(json.get("item")), EntityPredicate.AndPredicate.deserializeJSONObject(json, "entity", conditions));
    }

    public void trigger(ServerPlayerEntity player, ItemStack item, Entity entity) {
        LootContext ctx = EntityPredicate.getLootContext(player, entity);
        this.triggerListeners(player, (instance) -> instance.item.test(item) && instance.entity.testContext(ctx));
    }

    public static class Instance extends CriterionInstance {

        public final ItemPredicate item;
        public final EntityPredicate.AndPredicate entity;

        public Instance(ItemPredicate item, EntityPredicate.AndPredicate entity) {
            this(EntityPredicate.AndPredicate.ANY_AND, item, entity);
        }
        
        public Instance(EntityPredicate.AndPredicate player, ItemPredicate item, EntityPredicate.AndPredicate entity) {
            super(MjoellnirTrigger.ID, player);
            this.item = item;
            this.entity = entity;
        }

        public boolean test(ItemStack item, LootContext entity) {
            return this.item.test(item) && this.entity.testContext(entity);
        }

        @Nonnull
        public JsonObject serialize(@Nonnull ConditionArraySerializer conditions) {
            JsonObject json = super.serialize(conditions);
            json.add("item", this.item.serialize());
            json.add("entity", this.entity.serializeConditions(conditions));
            return json;
        }
    }
}
