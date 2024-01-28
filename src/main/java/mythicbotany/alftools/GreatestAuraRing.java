package mythicbotany.alftools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class GreatestAuraRing extends BaubleItem {

    public GreatestAuraRing(Properties properties) {
        super(properties);
    }

    public void onWornTick(ItemStack stack, LivingEntity living) {
        if (!living.level().isClientSide && living instanceof Player player && player.tickCount % 2 == 0) {
            ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
        }
    }
}
