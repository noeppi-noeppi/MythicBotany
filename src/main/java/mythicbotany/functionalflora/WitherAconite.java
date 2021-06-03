package mythicbotany.functionalflora;

import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class WitherAconite extends FunctionalFlowerBase {

    public static final int DEFAULT_MANA_PER_STAR = 2000000;

    public WitherAconite(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 0x333333, DEFAULT_MANA_PER_STAR / 500, DEFAULT_MANA_PER_STAR / 2000, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!world.isRemote) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos, pos.toImmutable().add(1, 1, 1)));
            if (items.size() == 1) {
                ItemEntity item = items.get(0);
                ItemStack stack = item.getItem();
                if (stack.getCount() == 1) {
                    if (stack.getItem() == Items.NETHER_STAR) {
                        stack = new ItemStack(ModItems.fadedNetherStar);
                    }
                    if (stack.getItem() == ModItems.fadedNetherStar) {
                        int manaTransfer = Math.min(maxTransfer * 2, Math.min(maxMana - mana, stack.getMaxDamage() - stack.getDamage()));
                        if (manaTransfer > 0) {
                            stack.setDamage(stack.getDamage() + manaTransfer);
                            if (stack.getDamage() >= stack.getMaxDamage())
                                stack = ItemStack.EMPTY;
                            mana = MathHelper.clamp(mana + manaTransfer, 0, maxMana);
                            didWork = true;
                            item.setItem(stack);
                            MythicBotany.getNetwork().setItemMagnetImmune(item);
                        }
                    }
                }
            }
        }
    }
}
