package mythicbotany.alftools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class GreatestAuraRing extends ItemBauble implements IManaGivingItem {

    public GreatestAuraRing(Properties props) {
        super(props);
    }

    public void onWornTick(ItemStack stack, LivingEntity player) {
        if (!player.world.isRemote && player instanceof PlayerEntity) {
            ManaItemHandler.instance().dispatchManaExact(stack, (PlayerEntity)player, 2, true);
        }
    }
}