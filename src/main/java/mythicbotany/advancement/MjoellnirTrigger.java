package mythicbotany.advancement;

import com.google.gson.JsonObject;
import mythicbotany.MythicBotany;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class MjoellnirTrigger extends SimpleCriterionTrigger<MjoellnirTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation(MythicBotany.getInstance().modid, "mjoellnir");
    
    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    @Override
    protected Instance createInstance(@Nonnull JsonObject json, @Nonnull EntityPredicate.Composite entityPredicate, @Nonnull DeserializationContext conditionsParser) {
        return new Instance(entityPredicate, ItemPredicate.fromJson(json.get("item")), EntityPredicate.Composite.fromJson(json, "entity", conditionsParser));
    }

    public void trigger(ServerPlayer player, ItemStack item, Entity entity) {
        LootContext ctx = EntityPredicate.createContext(player, entity);
        this.trigger(player, (instance) -> instance.item.matches(item) && instance.entity.matches(ctx));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public final ItemPredicate item;
        public final EntityPredicate.Composite entity;

        public Instance(ItemPredicate item, EntityPredicate.Composite entity) {
            this(EntityPredicate.Composite.ANY, item, entity);
        }
        
        public Instance(EntityPredicate.Composite player, ItemPredicate item, EntityPredicate.Composite entity) {
            super(MjoellnirTrigger.ID, player);
            this.item = item;
            this.entity = entity;
        }

        public boolean test(ItemStack item, LootContext entity) {
            return this.item.matches(item) && this.entity.matches(entity);
        }

        @Nonnull
        public JsonObject serializeToJson(@Nonnull SerializationContext conditions) {
            JsonObject json = super.serializeToJson(conditions);
            json.add("item", this.item.serializeToJson());
            json.add("entity", this.entity.toJson(conditions));
            return json;
        }
    }
}
