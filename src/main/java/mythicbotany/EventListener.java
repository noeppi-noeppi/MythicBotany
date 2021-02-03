package mythicbotany;

import com.google.common.collect.ImmutableSet;
import mythicbotany.alftools.AlfsteelHelm;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EventListener {

    public static final Set<Item> NO_EXPIRE = ImmutableSet.of(
            ModItems.greatestManaRing,
            ModItems.alfsteelPick
    );
    
    private final Set<UUID> crittingPlayers = new HashSet<>();

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

    @SubscribeEvent
    public void itemExpire(ItemExpireEvent event) {
        ItemStack stack = event.getEntityItem().getItem();
        if (!stack.isEmpty()) {
            if (NO_EXPIRE.contains(stack.getItem())) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void citicalHit(CriticalHitEvent event) {
        if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
            float strength = event.getPlayer().getCooledAttackStrength(0.5f);
            if (event.getTarget() instanceof LivingEntity) {
               strength *= EnchantmentHelper.getModifierForCreature(event.getPlayer().getHeldItemMainhand(), ((LivingEntity) event.getTarget()).getCreatureAttribute());
            } else {
               strength *= EnchantmentHelper.getModifierForCreature(event.getPlayer().getHeldItemMainhand(), CreatureAttribute.UNDEFINED);
            }
            event.setDamageModifier(event.getDamageModifier() * (((AlfsteelHelm) ModItems.alfsteelHelmet).onCritDamageCalc(strength, event.getPlayer()) / strength));
            crittingPlayers.add(event.getPlayer().getUniqueID());
        }
    }
    
    @SubscribeEvent
    public void attackEntity(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof PlayerEntity && crittingPlayers.contains(event.getSource().getTrueSource().getUniqueID())) {
            ((AlfsteelHelm) ModItems.alfsteelHelmet).onEntityAttacked(event.getSource(), event.getAmount(), ((PlayerEntity) event.getSource().getTrueSource()), event.getEntityLiving());
        }
    }
    
    @SubscribeEvent
    public void endTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            crittingPlayers.clear();
        }
    }
}
