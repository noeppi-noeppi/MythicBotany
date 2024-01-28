package mythicbotany.bauble;

import mythicbotany.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;

public class ItemFireRing extends BaubleItem {

    public ItemFireRing(Properties properties) {
        super(properties);
    }

    public void onWornTick(ItemStack stack, LivingEntity player) {
        if (player.level().isClientSide) {
            this.displayParticles(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void displayParticles(LivingEntity player) {
        if (Math.random() < 0.03 && (ClientConfig.ringParticles.get() || player != Minecraft.getInstance().player)) {
            player.getCommandSenderWorld().addParticle(ParticleTypes.FLAME,
                    player.getX() - 0.25 + (Math.random() / 2),
                    player.getY(),
                    player.getZ() - 0.25 + (Math.random() / 2),
                    0, 0.1, 0);
        }
    }
}
