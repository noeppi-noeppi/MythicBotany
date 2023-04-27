package mythicbotany.alftools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import net.minecraft.world.item.Item.Properties;

public class GreatestAuraRing extends ItemBauble {

    public GreatestAuraRing(Properties properties) {
        super(properties);
    }

    public void onWornTick(ItemStack stack, LivingEntity living) {
        if (!living.level.isClientSide && living instanceof Player player) {
            ManaItemHandler.instance().dispatchManaExact(stack, player, 20, true);
        }
    }
}
