package mythicbotany.loot;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.alftools.AlfsteelPick;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelDisposeModifier extends LootModifier {

    private AlfsteelDisposeModifier(ILootCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        ItemStack tool = context.get(LootParameters.TOOL);
        if (entity != null && tool != null && !tool.isEmpty()) {
            filterDisposable(generatedLoot, entity, tool);
        }
        return generatedLoot;
    }

    public static void filterDisposable(List<ItemStack> drops, Entity entity, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == ModItems.alfsteelPick && AlfsteelPick.isTipped(stack)) {
            drops.removeIf((loot) -> !loot.isEmpty() && (ModTags.Items.DISPOSABLE.contains(loot.getItem()) || ModTags.Items.SEMI_DISPOSABLE.contains(loot.getItem()) && !entity.isSneaking()));
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<AlfsteelDisposeModifier> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        public AlfsteelDisposeModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new AlfsteelDisposeModifier(conditions);
        }

        public JsonObject write(AlfsteelDisposeModifier modifier) {
            return this.makeConditions(modifier.conditions);
        }
    }
}
