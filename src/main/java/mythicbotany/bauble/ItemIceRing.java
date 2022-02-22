package mythicbotany.bauble;

import mythicbotany.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemIceRing extends ItemBauble {

    public ItemIceRing(Properties props) {
        super(props);
    }

    public void onWornTick(ItemStack stack, LivingEntity player) {
        if (player.level.isClientSide) {
            displayParticles(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void displayParticles(LivingEntity player) {
        if (Math.random() < 0.03 && (ClientConfig.ringParticles.get() || player != Minecraft.getInstance().player)) {
            player.getCommandSenderWorld().addParticle(ParticleTypes.ITEM_SNOWBALL,
                    player.getX(),
                    player.getY() + 1.85,
                    player.getZ(),
                    0, 0, 0);
        }
    }
}
