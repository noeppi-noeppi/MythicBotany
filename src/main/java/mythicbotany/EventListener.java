package mythicbotany;

import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.teleporter.AlfheimTeleporter;
import mythicbotany.alfheim.teleporter.TileReturnPortal;
import mythicbotany.alftools.AlfsteelHelm;
import mythicbotany.config.MythicConfig;
import mythicbotany.misc.Andwari;
import mythicbotany.mjoellnir.BlockMjoellnir;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

import javax.annotation.Nullable;
import java.util.*;

public class EventListener {

    @Nullable
    public static LivingEntity lightningImmuneEntity = null;
    
    private final Set<UUID> crittingPlayers = new HashSet<>();

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            if (CuriosApi.getCuriosHelper().findFirstCurio((LivingEntity) event.getSource().getEntity(), ModItems.fireRing).isPresent()) {
                if (event.getEntityLiving().getRemainingFireTicks() <= 1) {
                    event.getEntityLiving().setSecondsOnFire(1);
                }
            }
            if (CuriosApi.getCuriosHelper().findFirstCurio((LivingEntity) event.getSource().getEntity(), ModItems.iceRing).isPresent()) {
                if (!event.getEntityLiving().hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    event.getEntityLiving().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 99));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void citicalHit(CriticalHitEvent event) {
        if (event.getResult() == Event.Result.ALLOW || (event.getResult() == Event.Result.DEFAULT && event.isVanillaCritical())) {
            if (((AlfsteelHelm) ModItems.alfsteelHelmet).hasArmorSet(event.getPlayer())) {
                if (((AlfsteelHelm) ModItems.alfsteelHelmet).hasAncientWill(event.getPlayer().getItemBySlot(EquipmentSlot.HEAD), IAncientWillContainer.AncientWillType.DHAROK)) {
                    float calculatedModifier = event.getDamageModifier() * (1f + (1f - event.getPlayer().getHealth() / event.getPlayer().getMaxHealth()) * 0.5f);
                    if (calculatedModifier != 1 && calculatedModifier != 0 && Float.isFinite(calculatedModifier)) {
                        event.setDamageModifier(calculatedModifier);
                    }
                }
                this.crittingPlayers.add(event.getPlayer().getUUID());
            }
        }
    }

    @SubscribeEvent
    public void attackEntity(LivingAttackEvent event) {
        if (event.getSource() == DamageSource.LIGHTNING_BOLT && event.getEntityLiving() == lightningImmuneEntity) {
            event.setCanceled(true);
            return;
        }
        if (event.getSource().isFire()) {
            if (CuriosApi.getCuriosHelper().findFirstCurio(event.getEntityLiving(), ModItems.fireRing).isPresent()) {
                event.setCanceled(true);
                return;
            }
        }
        if (event.getSource() == DamageSource.CRAMMING && event.getSource() == DamageSource.DRY_OUT && event.getSource() == DamageSource.IN_WALL) {
            if (CuriosApi.getCuriosHelper().findFirstCurio(event.getEntityLiving(), ModItems.iceRing).isPresent()) {
                event.setCanceled(true);
                return;
            }
        }
        if (event.getSource().getEntity() instanceof Player && this.crittingPlayers.contains(event.getSource().getEntity().getUUID())) {
            ItemTerrasteelHelm.onEntityAttacked(event.getSource(), event.getAmount(), ((Player) event.getSource().getEntity()), event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public void endTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.crittingPlayers.clear();
        }
    }

    @SubscribeEvent
    public void playerClone(PlayerEvent.Clone event) {
        MythicPlayerData.copy(event.getOriginal(), event.getPlayer());
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.EntityPlaceEvent event) {
        if (!event.getWorld().isClientSide() && event.getPlacedBlock().getBlock() == Blocks.GOLD_BLOCK && event.getEntity() instanceof LivingEntity) {
            CuriosApi.getCuriosHelper().findFirstCurio((LivingEntity) event.getEntity(), ModItems.andwariRing).ifPresent(result -> {
                boolean hasMana = true;
                if (event.getEntity() instanceof Player) {
                    hasMana = ManaItemHandler.instance().requestManaExact(result.stack(), (Player) event.getEntity(), 2000, true);
                }
                if (hasMana) {
                    if (!(event.getEntity() instanceof Player && ((Player) event.getEntity()).isCreative())) {
                        String id = result.slotContext().identifier();
                        int slot = result.slotContext().index();
                        ItemStack stack = result.stack();
                        //noinspection CodeBlock2Expr
                        stack.hurtAndBreak(1, (LivingEntity) event.getEntity(), e -> {
                            //noinspection CodeBlock2Expr
                            CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity) event.getEntity()).ifPresent(handler -> {
                                handler.getCurios().get(id).getStacks().setStackInSlot(slot, new ItemStack(ModItems.cursedAndwariRing));
                            });
                        });
                    }
                    if (event.getEntity() != null) {
                        ItemStack drop = Andwari.randomAndwariItem(event.getWorld().getRandom());
                        event.getEntity().spawnAtLocation(drop);
                    }
                }
            });
        }
    }
    
    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event) {
        if (!event.getItem().getCommandSenderWorld().isClientSide) {
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
        BlockEntity portal = event.getPortalTile();
        if (event.isOpen() && portal.getLevel() != null && !portal.getLevel().isClientSide) {
            if (Alfheim.DIMENSION.equals(portal.getLevel().dimension())) {
                // Alfheim portals in alfheim make no sense. better close this one.
                if (portal instanceof TileAlfPortal alfPortal) {
                    // A portal will close if it tries to consume mana without pylons.
                    // So we just let it consume 0 mana with 0 pylons which should close
                    // the portal.
                    alfPortal.consumeMana(new ArrayList<>(), 0, true);
                }
            }
            // We only teleport from the overworld.
            if (Level.OVERWORLD.equals(portal.getLevel().dimension()) && AlfheimPortalHandler.shouldCheck(portal.getLevel())) {
                List<Player> playersInPortal = portal.getLevel().getEntitiesOfClass(Player.class, event.getAabb());
                for (Player player : playersInPortal) {
                    if (player instanceof ServerPlayer && MythicPlayerData.getData(player).getBoolean("KvasirKnowledge")) {
                        if (AlfheimPortalHandler.setInPortal(portal.getLevel(), player)) {
                            if (!AlfheimTeleporter.teleportToAlfheim((ServerPlayer) player, portal.getBlockPos())) {
                                player.sendMessage(new TranslatableComponent("message.mythicbotany.alfheim_not_loaded"), player.getUUID());
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void itemDespawn(ItemExpireEvent event) {
        if (!event.getEntityItem().level.isClientSide && Alfheim.DIMENSION.equals(event.getEntityItem().level.dimension())) {
            if (event.getEntityItem().getItem().getItem() == vazkii.botania.common.item.ModItems.dragonstone || event.getEntityItem().getItem().getItem() == vazkii.botania.common.item.ModItems.pixieDust) {
                BlockPos pos = event.getEntityItem().blockPosition();
                if (TileReturnPortal.validPortal(event.getEntityItem().level, pos)) {
                    event.getEntityItem().level.setBlock(pos, ModBlocks.returnPortal.defaultBlockState(), 3);
                    event.getEntityItem().remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.tickCount % 4 == 1 && !event.player.level.isClientSide && Alfheim.DIMENSION.equals(event.player.level.dimension())) {
            if (MythicConfig.lockAlfheim && !MythicPlayerData.getData(event.player).getBoolean("KvasirKnowledge")) {
                // Player used another mod to get to alfheim
                event.player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, true, false, true));
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void sleepFinished(SleepFinishedTimeEvent event) {
        if (event.getWorld() instanceof ServerLevel level && !level.isClientSide) {
            if (Alfheim.DIMENSION.equals(level.dimension())) {
                level.getServer().overworld().setDayTime(event.getNewTime());
            }
        }
    }
}
