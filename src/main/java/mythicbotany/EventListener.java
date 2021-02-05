package mythicbotany;

import com.google.common.collect.ImmutableSet;
import mythicbotany.alftools.AlfsteelHelm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.item.IAncientWillContainer;

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
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void citicalHit(CriticalHitEvent event) {
        if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
            if (((AlfsteelHelm) ModItems.alfsteelHelmet).hasArmorSet(event.getPlayer())) {
                if (((AlfsteelHelm) ModItems.alfsteelHelmet).hasAncientWill(event.getPlayer().getItemStackFromSlot(EquipmentSlotType.HEAD), IAncientWillContainer.AncientWillType.DHAROK)) {
                    float calculatedModifier = event.getDamageModifier() * (1f + (1f - event.getPlayer().getHealth() / event.getPlayer().getMaxHealth()) * 0.5f);
                    if (calculatedModifier != 1 && calculatedModifier != 0 && Float.isFinite(calculatedModifier)) {
                        event.setDamageModifier(calculatedModifier);
                    }
                }
                crittingPlayers.add(event.getPlayer().getUniqueID());
            }
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
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void livingHurt(LivingHurtEvent event) {
        // Required for old worlds which have been generated with bug #22.
        // Removes NaN from health and absorption amount
        if (!event.getEntityLiving().getPersistentData().getBoolean("mythicbotany-fix-22a")) {
            event.getEntityLiving().getPersistentData().putBoolean("mythicbotany-fix-22a", true);
            if (Float.isNaN(event.getEntityLiving().getHealth()) || Float.isNaN(event.getEntityLiving().getAbsorptionAmount())) {
                event.getEntityLiving().setHealth(Float.isFinite(event.getEntityLiving().getMaxHealth()) ? event.getEntityLiving().getMaxHealth() : 1);
                event.getEntityLiving().setAbsorptionAmount(0);
                MythicBotany.getInstance().logger.info("Fixed #22 for entity " + event.getEntityLiving().getUniqueID() + ".");
            }
        }
    }
}
