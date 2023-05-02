package mythicbotany.mimir;

import net.minecraft.network.chat.Component;
import org.moddingx.libx.base.ItemBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.ModItems;
import mythicbotany.MythicPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class FullGjallarHorn extends ItemBase {

    public FullGjallarHorn(ModX mod, Properties properties) {
        super(mod, properties);
    }

    @Nonnull
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity living) {
        if (living instanceof Player player) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!level.isClientSide) {
                CompoundTag nbt = MythicPlayerData.getData(player);
                if (nbt.getBoolean("MimirKnowledge")) {
                    player.sendSystemMessage(Component.translatable("message.mythicbotany.mimir_known").withStyle(ChatFormatting.GRAY));
                } else {
                    nbt.putBoolean("MimirKnowledge", true);
                    player.sendSystemMessage(Component.translatable("message.mythicbotany.mimir_knowledge").withStyle(ChatFormatting.GRAY));
                }
            }
            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new ItemStack(ModItems.gjallarHornEmpty);
                } else {
                    player.getInventory().add(new ItemStack(ModItems.gjallarHornEmpty));
                }
            }
        }
        return stack;
    }

    public int getUseDuration(@Nonnull ItemStack stack) {
        return 48;
    }

    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
