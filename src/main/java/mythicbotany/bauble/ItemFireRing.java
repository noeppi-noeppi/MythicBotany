package mythicbotany.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemFireRing extends ItemBauble {

    public ItemFireRing(Properties props) {
        super(props);
    }

    public void onWornTick(ItemStack stack, LivingEntity player) {
        if (player.world.isRemote && player instanceof PlayerEntity && Math.random() < 0.01) {
            player.getEntityWorld().addParticle(ParticleTypes.FLAME,
                    player.getPosX() - 0.25 + (Math.random() / 2),
                    player.getPosY(),
                    player.getPosZ() - 0.25 + (Math.random() / 2),
                    0, 0.1, 0);
        }
    }
}
