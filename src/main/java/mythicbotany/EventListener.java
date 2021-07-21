package mythicbotany;

import com.google.common.collect.ImmutableSet;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.teleporter.AlfheimTeleporter;
import mythicbotany.alfheim.teleporter.TileReturnPortal;
import mythicbotany.alftools.AlfsteelHelm;
import mythicbotany.misc.Andwari;
import mythicbotany.mjoellnir.BlockMjoellnir;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.mana.BlockEnchanter;
import vazkii.botania.common.block.tile.TileAlfPortal;

import javax.annotation.Nullable;
import java.util.*;

public class EventListener {

    @Nullable
    public static LivingEntity lightningImmuneEntity = null;
    
    private final Set<UUID> crittingPlayers = new HashSet<>();

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
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
        if (event.getSource() == DamageSource.LIGHTNING_BOLT && event.getEntityLiving() == lightningImmuneEntity) {
            event.setCanceled(true);
            return;
        }
        if (event.getSource().isFireDamage()) {
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.fireRing, event.getEntityLiving()).isPresent()) {
                event.setCanceled(true);
                return;
            }
        }
        if (event.getSource() == DamageSource.CRAMMING && event.getSource() == DamageSource.DRYOUT && event.getSource() == DamageSource.IN_WALL) {
            if (CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.iceRing, event.getEntityLiving()).isPresent()) {
                event.setCanceled(true);
                return;
            }
        }
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

    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack held = event.getItemStack();
        if (!held.isEmpty() && held.getItem() == ModItems.dreamwoodTwigWand) {
            BlockState state = event.getWorld().getBlockState(event.getPos());
            if (state.getBlock() instanceof BlockEnchanter) {
                event.setUseBlock(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        MythicPlayerData.copy(event.getOriginal(), event.getPlayer());
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isRemote() && event.getPlacedBlock().getBlock() == Blocks.GOLD_BLOCK && event.getEntity() instanceof LivingEntity) {
            CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.andwariRing, (LivingEntity) event.getEntity()).ifPresent(entry -> {
                boolean hasMana = true;
                if (event.getEntity() instanceof PlayerEntity) {
                    hasMana = ManaItemHandler.instance().requestManaExact(entry.getRight(), (PlayerEntity) event.getEntity(), 2000, true);
                }
                if (hasMana) {
                    if (!(event.getEntity() instanceof PlayerEntity && ((PlayerEntity) event.getEntity()).isCreative())) {
                        String id = entry.getLeft();
                        int slot = entry.getMiddle();
                        ItemStack stack = entry.getRight();
                        //noinspection CodeBlock2Expr
                        stack.damageItem(1, (LivingEntity) event.getEntity(), e -> {
                            //noinspection CodeBlock2Expr
                            CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity) event.getEntity()).ifPresent(handler -> {
                                handler.getCurios().get(id).getStacks().setStackInSlot(slot, new ItemStack(ModItems.cursedAndwariRing));
                            });
                        });
                    }
                    if (event.getEntity() != null) {
                        ItemStack drop = Andwari.randomAndwariItem(event.getWorld().getRandom());
                        event.getEntity().entityDropItem(drop);
                    }
                }
            });
        }
    }
    
    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event) {
        if (!event.getItem().getEntityWorld().isRemote) {
            // YEP. Sometimes method names are weird.
            if (event.getItem().getItem().getItem() == ModBlocks.mjoellnir.asItem()) {
                if (!BlockMjoellnir.canHold(event.getPlayer())) {
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void alfPortalUpdate(ElvenPortalUpdateEvent event) {
        if (event.open && event.portalTile.getWorld() != null && !event.portalTile.getWorld().isRemote) {
            if (Alfheim.DIMENSION.equals(event.portalTile.getWorld().getDimensionKey())) {
                // Alfheim portals in alfheim make no sense. better close this one.
                if (event.portalTile instanceof TileAlfPortal) {
                    // A portal will close if it tries to consume mana without pylons.
                    // So we just let it consume 0 mana with 0 ylons which should close
                    // the portal.
                    ((TileAlfPortal) event.portalTile).consumeMana(new ArrayList<>(), 0, true);
                }
            }
            // We only teleport from the overworld.
            if (World.OVERWORLD.equals(event.portalTile.getWorld().getDimensionKey()) && AlfheimPortalHandler.shouldCheck(event.portalTile.getWorld())) {
                List<PlayerEntity> playersInPortal = event.portalTile.getWorld().getEntitiesWithinAABB(PlayerEntity.class, event.aabb);
                for (PlayerEntity player : playersInPortal) {
                    if (player instanceof ServerPlayerEntity && MythicPlayerData.getData(player).getBoolean("KvasirKnowledge")) {
                        if (AlfheimPortalHandler.setInPortal(event.portalTile.getWorld(), player)) {
                            AlfheimTeleporter.teleportToAlfheim((ServerPlayerEntity) player, event.portalTile.getPos());
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemDespawn(ItemExpireEvent event) {
        if (!event.getEntityItem().getEntityWorld().isRemote && Alfheim.DIMENSION.equals(event.getEntityItem().getEntityWorld().getDimensionKey())) {
            if (event.getEntityItem().getItem().getItem() == vazkii.botania.common.item.ModItems.dragonstone) {
                BlockPos pos = event.getEntityItem().getPosition();
                if (TileReturnPortal.validPortal(event.getEntityItem().getEntityWorld(), pos)) {
                    event.getEntityItem().getEntityWorld().setBlockState(pos, ModBlocks.returnPortal.getDefaultState(), 3);
                    event.getEntityItem().remove();
                }
            }
        }
    }
}
