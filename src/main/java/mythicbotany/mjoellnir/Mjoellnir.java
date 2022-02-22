package mythicbotany.mjoellnir;

import mythicbotany.EventListener;
import mythicbotany.ModBlocks;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.config.MythicConfig;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.network.NetworkHooks;

public class Mjoellnir extends Projectile {

    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(Mjoellnir.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(Mjoellnir.class, EntityDataSerializers.ITEM_STACK);

    private boolean returning = false;
    private ItemStack stack = ItemStack.EMPTY;
    private UUID thrower = null;
    private Vec3 throwPos = null;
    // 0-8: hotbar, 9: offhand
    private int hotbarSlot = 0;
    private int lifeLeft = 40;

    public Mjoellnir(Level level) {
        this(ModBlocks.mjoellnir.getEntityType(), level);
    }

    public Mjoellnir(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(RETURNING, false);
        entityData.define(STACK, ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onSyncedDataUpdated(@Nonnull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (RETURNING.equals(key)) {
            returning = entityData.get(RETURNING);
        } else if (STACK.equals(key)) {
            stack = entityData.get(STACK);
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Will be changed before moving.
        // But we need the original value later on
        boolean returningTickStart = returning;

        if (!returning) {
            checkForCollision();
        } else if (!level.isClientSide) {
            Vec3 returnPoint = getReturnPoint();
            if (returnPoint != null) {
                applyReturnMotion(returnPoint);
            }
            tryReturn(returnPoint);
        }


        Vec3 motion = this.getDeltaMovement();
        if (returningTickStart) {
            // The move method applies collisions. We don't want that
            // when returning or we might be stuck
            Vec3 position = this.position();
            setPos(position.x + motion.x, position.y + motion.y, position.z + motion.z);
//            move(MoverType.SELF, motion);
        } else {
            move(MoverType.SELF, motion);
        }

        lifeLeft -= 1;

        if (lifeLeft <= 0) {
            startReturn();
        }

        Vec3 position = position();
        this.setPos(position.x, position.y, position.z);
    }

    private void checkForCollision() {
        // We also check on the client because it takes time for the server to update and this makes it smoother
        // however we don't trigger any logic here, just set the motion.
        Vec3 motion = getDeltaMovement();
        Vec3 position = position();
        Vec3 rayCast = position.add(motion);

        if (isAlive() && !entityData.get(RETURNING)) {
            HitResult result = ProjectileUtil.getEntityHitResult(level, this, position, rayCast, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1),
                    entity -> entity != this && !entity.isSpectator() && entity.isAlive() && entity.isPickable() && entity != getThrower());

            if (result == null || result.getType() == HitResult.Type.MISS) {
                result = level.clip(new ClipContext(position, rayCast, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            }
            //noinspection ConstantConditions
            if (result != null && result.getType() != HitResult.Type.MISS) {
                if (!level.isClientSide) {
                    onHit(result);
                } else {
                    Vec3 returnPoint = getReturnPoint();
                    if (returnPoint != null) {
                        applyReturnMotionClient(returnPoint);
                    }
                }
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        if (!returning) {
            super.onHit(result);
            startReturn();
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity) {
            attackEntities((LivingEntity) result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult hit) {
        AABB aabb = new AABB(hit.getLocation(), hit.getLocation()).inflate(2);
        List<LivingEntity> living = level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> !entity.isSpectator() && entity.isAlive() && entity != getThrower());
        if (!living.isEmpty()) {
            attackEntities(living.get(level.random.nextInt(living.size())));
        }
    }

    @Nullable
    @Override
    protected PortalInfo findDimensionEntryPoint(@Nonnull ServerLevel level) {
        return null;
    }
    
    @Nullable
    private Vec3 getReturnPoint() {
        Player throwerEntity = getThrower();
        if (throwerEntity != null) {
            return throwerEntity.position();
        } else if (throwPos != null) {
            return throwPos;
        } else {
            return null;
        }
    }

    private void applyReturnMotion(@Nonnull Vec3 returnPoint) {
        if (!level.isClientSide) {
            Vec3 motion = getDeltaMovement();
            Vec3 position = position();
            Vec3 returnVec = new Vec3(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().multiply(0.6, 0.6, 0.6);
            // clamp because some mods think, it's a good idea to over enchant stuff on any type of tool they don't know about
            double loyalty = 1 + (0.07 * Mth.clamp(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.LOYALTY, stack), 0, 3));
            Vec3 newMotion = new Vec3(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4).multiply(loyalty, loyalty, loyalty);
            setDeltaMovement(newMotion);
        }
    }

    private void applyReturnMotionClient(@Nonnull Vec3 returnPoint) {
        if (level.isClientSide) {
            Vec3 motion = getDeltaMovement();
            Vec3 position = position();
            Vec3 returnVec = new Vec3(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().multiply(0.6, 0.6, 0.6);
            Vec3 newMotion = new Vec3(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4);
            setDeltaMovement(newMotion);
        }
    }

    private void tryReturn(@Nullable Vec3 returnPoint) {
        if (returnPoint == null) {
            // We don't know where we should fly. Just place it where we are.
            BlockMjoellnir.putInWorld(stack, level, blockPosition());
            this.remove(RemovalReason.DISCARDED);
        } else if (returnPoint.distanceToSqr(position()) < 2) {
            Player throwerEntity = getThrower();
            if (throwerEntity != null) {
                if (!BlockMjoellnir.putInInventory(throwerEntity, stack, hotbarSlot)) {
                    BlockMjoellnir.putInWorld(stack, level, new BlockPos(returnPoint));
                }
            } else {
                BlockMjoellnir.putInWorld(stack, level, new BlockPos(returnPoint));
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void startReturn() {
        if (!returning) {
            setLifeLeft(0);
            setReturning(true);
        }
    }

    private void attackEntities(LivingEntity target) {
        AABB aabb = new AABB(target.position(), target.position()).inflate(4);
        List<LivingEntity> found = level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> !entity.isSpectator() && entity.isAlive() && entity != getThrower());
        LightningBolt lightning = attackEntity(target);
        for (LivingEntity entity : found) {
            if (entity != target) {
                areaDamage(entity, lightning);
            }
        }
    }

    @Nullable
    private LightningBolt attackEntity(LivingEntity target) {
        if (!level.isClientSide) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) >= 1) {
                target.setSecondsOnFire(5);
            }
            int knockback = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
            if (knockback > 0) {
                Vec3 vector3d = this.getDeltaMovement().multiply(1, 0, 1).normalize().scale(knockback * 0.6);
                if (vector3d.lengthSqr() > 0) {
                    target.push(vector3d.x, 0.1D, vector3d.z);
                }
            }
            int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
            float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
            if (power > 0) {
                dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.getDamageBonus(power, target.getMobType()));
            }
            Player thrower = getThrower();
            if (thrower instanceof ServerPlayer) {
                ModCriteria.MJOELLNIR.trigger((ServerPlayer) thrower, getStack(), target);
            }
            target.hurt(thrower == null ? DamageSource.GENERIC : DamageSource.indirectMobAttack(this, thrower), dmg);
            LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightning.setPos(target.getX(), target.getY(), target.getZ());
            lightning.setVisualOnly(true);
            lightning.setCause(thrower instanceof ServerPlayer ? (ServerPlayer) thrower : null);
            level.addFreshEntity(lightning);
            // When the entity is struck by lightning it'll take lightning damage and be set
            // on fire. We don't want that so we disable the lightning damage for that time
            // and reset the fire to the old state afterwards
            if (level instanceof ServerLevel && !ForgeEventFactory.onEntityStruckByLightning(target, lightning)) {
                int fireTicks = target.getRemainingFireTicks();
                LivingEntity oldImmune = EventListener.lightningImmuneEntity;
                EventListener.lightningImmuneEntity = target;
                target.thunderHit((ServerLevel) level, lightning);
                EventListener.lightningImmuneEntity = oldImmune;
                target.setRemainingFireTicks(fireTicks);
            }
            return lightning;
        } else {
            return null;
        }
    }

    private void areaDamage(LivingEntity target, @Nullable LightningBolt lightning) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) >= 1) {
            target.setSecondsOnFire(2);
        }
        int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
        if (power > 0) {
            dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.getDamageBonus(power, target.getMobType()));
        }
        dmg *= MythicConfig.mjoellnir.secondary_target_multiplier;
        Player thrower = getThrower();
        target.hurt(thrower == null ? DamageSource.GENERIC : DamageSource.indirectMobAttack(this, thrower), dmg);
        if (lightning != null && level.random.nextFloat() < MythicConfig.mjoellnir.secondary_lightning_chance) {
            int fireTicks = target.getRemainingFireTicks();
            LivingEntity oldImmune = EventListener.lightningImmuneEntity;
            EventListener.lightningImmuneEntity = target;
            target.thunderHit((ServerLevel) level, lightning);
            EventListener.lightningImmuneEntity = oldImmune;
            target.setRemainingFireTicks(fireTicks);
        }
    }

    public boolean isReturning() {
        return returning;
    }

    public void setReturning(boolean returning) {
        entityData.set(RETURNING, returning);
        this.returning = returning;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        entityData.set(STACK, stack);
        this.stack = stack;
    }

    @Nullable
    public Player getThrower() {
        if (thrower == null) {
            return null;
        } else {
            return level.getPlayerByUUID(thrower);
        }
    }

    @Nullable
    public UUID getThrowerId() {
        return thrower;
    }

    public void setThrower(@Nullable Player thrower) {
        setThrowerId(thrower == null ? null : thrower.getGameProfile().getId());
    }

    public void setThrowerId(@Nullable UUID thrower) {
        this.thrower = thrower;
    }

    @Nullable
    public Vec3 getThrowPos() {
        return throwPos;
    }

    public void setThrowPos(@Nullable Vec3 throwPos) {
        this.throwPos = throwPos;
    }

    public int getHotBarSlot() {
        return hotbarSlot;
    }

    public void setHotBarSlot(int hotBarSlot) {
        this.hotbarSlot = hotBarSlot;
    }

    public int getLifeLeft() {
        return lifeLeft;
    }

    public void setLifeLeft(int lifeLeft) {
        this.lifeLeft = lifeLeft;
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Stack", stack.save(new CompoundTag()));
        if (thrower != null) {
            compound.putBoolean("thrower", true);
            compound.putLong("throwerl", thrower.getLeastSignificantBits());
            compound.putLong("throwerm", thrower.getMostSignificantBits());
        } else {
            compound.putBoolean("thrower", false);
        }
        if (throwPos != null) {
            compound.putBoolean("throwPos", true);
            compound.putDouble("throwX", throwPos.x);
            compound.putDouble("throwY", throwPos.y);
            compound.putDouble("throwZ", throwPos.z);
        } else {
            compound.putBoolean("throwPos", false);
        }
        compound.putDouble("hotbar", hotbarSlot);
        compound.putDouble("lifeTime", lifeLeft);
        compound.putBoolean("returning", returning);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setStack(ItemStack.of(compound.getCompound("Stack")));
        if (compound.getBoolean("thrower")) {
            setThrowerId(new UUID(compound.getLong("throwerm"), compound.getLong("throwerl")));
        } else {
            setThrowerId(null);
        }
        if (compound.getBoolean("throwPos")) {
            setThrowPos(new Vec3(compound.getDouble("throwX"), compound.getDouble("throwY"), compound.getDouble("throwZ")));
        } else {
            setThrowPos(null);
        }
        setHotBarSlot(compound.getInt("hotbar"));
        setLifeLeft(compound.getInt("lifeTime"));
        setReturning(compound.getBoolean("returning"));
    }
}
