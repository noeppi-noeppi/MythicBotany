package mythicbotany.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mythicbotany.alftools.AlfsteelPick;
import mythicbotany.register.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nonnull;
import java.util.List;

public class AlfsteelDisposeModifier extends LootModifier {
    
    public static final Codec<AlfsteelDisposeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
    ).apply(instance, AlfsteelDisposeModifier::new));

    public AlfsteelDisposeModifier(LootItemCondition... conditions) {
        super(conditions);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (entity != null && tool != null && !tool.isEmpty()) {
            filterDisposable(generatedLoot, entity, tool);
        }
        return generatedLoot;
    }

    public static void filterDisposable(List<ItemStack> drops, Entity entity, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == ModItems.alfsteelPick && AlfsteelPick.isTipped(stack)) {
            drops.removeIf((loot) -> !loot.isEmpty() && (loot.is(BotaniaTags.Items.DISPOSABLE) || (loot.is(BotaniaTags.Items.SEMI_DISPOSABLE) && !entity.isShiftKeyDown())));
        }
    }
}
