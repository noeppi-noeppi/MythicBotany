package mythicbotany.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemIceRing extends ItemBauble {

    public ItemIceRing(Properties props) {
        super(props);
    }

    public void onWornTick(ItemStack stack, LivingEntity player) {
        if (player.world.isRemote && player instanceof PlayerEntity && Math.random() < 0.01) {
            player.getEntityWorld().addParticle(ParticleTypes.ITEM_SNOWBALL,
                    player.getPosX(),
                    player.getPosY() + 1.85,
                    player.getPosZ(),
                    0, 0, 0);
        }
    }
}
