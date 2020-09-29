package mythicbotany;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class EventListener {

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {

        if (event.getSource().isFireDamage()) {
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.fireRing, event.getEntityLiving()).isPresent()) {
                event.setCanceled(true);
            }
        }
        if (event.getSource() == DamageSource.CRAMMING && event.getSource() == DamageSource.DRYOUT && event.getSource() == DamageSource.IN_WALL) {
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.iceRing, event.getEntityLiving()).isPresent()) {
                event.setCanceled(true);
            }
        }
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.fireRing, (LivingEntity) event.getSource().getTrueSource()).isPresent()) {
                if (event.getEntityLiving().getFireTimer() <= 1) {
                    event.getEntityLiving().setFire(1);
                }
            }
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.iceRing, (LivingEntity) event.getSource().getTrueSource()).isPresent()) {
                if (!event.getEntityLiving().isPotionActive(Effects.SLOWNESS)) {
                    event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20, 99));
                }
            }
        }
    }
}
