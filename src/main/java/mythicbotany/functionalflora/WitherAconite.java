package mythicbotany.functionalflora;

import mythicbotany.register.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class WitherAconite extends FunctionalFlowerBase {

    public static final int DEFAULT_MANA_PER_STAR = 1200000;

    public WitherAconite(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0x333333, DEFAULT_MANA_PER_STAR / 500, DEFAULT_MANA_PER_STAR / 2000, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition, this.worldPosition.immutable().offset(1, 1, 1)));
            if (items.size() == 1) {
                ItemEntity item = items.get(0);
                ItemStack stack = item.getItem();
                if (stack.getCount() == 1) {
                    if (stack.getItem() == Items.NETHER_STAR) {
                        stack = new ItemStack(ModItems.fadedNetherStar);
                    }
                    if (stack.getItem() == ModItems.fadedNetherStar) {
                        int manaTransfer = Math.min(this.maxTransfer * 2, Math.min(this.maxMana - this.mana, stack.getMaxDamage() - stack.getDamageValue()));
                        if (manaTransfer > 0) {
                            stack.setDamageValue(stack.getDamageValue() + manaTransfer);
                            if (stack.getDamageValue() >= stack.getMaxDamage())
                                stack = ItemStack.EMPTY;
                            this.mana = Mth.clamp(this.mana + manaTransfer, 0, this.maxMana);
                            this.didWork = true;
                            item.setItem(stack);
                            MythicBotany.getNetwork().setItemMagnetImmune(item);
                        }
                    }
                }
            }
        }
    }
}
