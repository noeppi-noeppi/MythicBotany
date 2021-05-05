package mythicbotany.mimir;

import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.ItemBase;
import mythicbotany.ModItems;
import mythicbotany.MythicPlayerData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class FullGjallarHorn extends ItemBase {

    public FullGjallarHorn(ModX mod, Properties properties) {
        super(mod, properties);
    }

    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity living) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            if (player instanceof ServerPlayerEntity) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
            }
            player.addStat(Stats.ITEM_USED.get(this));
            if (!world.isRemote) {
                CompoundNBT nbt = MythicPlayerData.getData(player);
                if (nbt.getBoolean("MimirKnowledge")) {
                    player.sendMessage(new TranslationTextComponent("message.mythicbotany.mimir_known").mergeStyle(TextFormatting.GRAY), player.getUniqueID());
                } else {
                    nbt.putBoolean("MimirKnowledge", true);
                    player.sendMessage(new TranslationTextComponent("message.mythicbotany.mimir_knowledge").mergeStyle(TextFormatting.GRAY), player.getUniqueID());
                }
            }
            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new ItemStack(ModItems.gjallarHornEmpty);
                } else {
                    player.inventory.addItemStackToInventory(new ItemStack(ModItems.gjallarHornEmpty));
                }
            }
        }
        return stack;
    }

    public int getUseDuration(@Nonnull ItemStack stack) {
        return 48;
    }

    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.DRINK;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull PlayerEntity playerIn, @Nonnull Hand hand) {
        return DrinkHelper.startDrinking(worldIn, playerIn, hand);
    }
}
