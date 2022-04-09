package mythicbotany.loot;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.alftools.AlfsteelPick;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelDisposeModifier extends LootModifier {

    private AlfsteelDisposeModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (entity != null && tool != null && !tool.isEmpty()) {
            filterDisposable(generatedLoot, entity, tool);
        }
        return generatedLoot;
    }

    public static void filterDisposable(List<ItemStack> drops, Entity entity, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == ModItems.alfsteelPick && AlfsteelPick.isTipped(stack)) {
            drops.removeIf((loot) -> !loot.isEmpty() && (loot.is(ModTags.Items.DISPOSABLE) || loot.is(ModTags.Items.SEMI_DISPOSABLE) && !entity.isShiftKeyDown()));
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<AlfsteelDisposeModifier> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        public AlfsteelDisposeModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            return new AlfsteelDisposeModifier(conditions);
        }

        public JsonObject write(AlfsteelDisposeModifier modifier) {
            return this.makeConditions(modifier.conditions);
        }
    }
}
