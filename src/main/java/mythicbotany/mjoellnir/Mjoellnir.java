package mythicbotany.mjoellnir;

import mythicbotany.EventListener;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.config.MythicConfig;
import mythicbotany.register.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

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
        this.entityData.define(RETURNING, false);
        this.entityData.define(STACK, ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void onSyncedDataUpdated(@Nonnull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (RETURNING.equals(key)) {
            this.returning = this.entityData.get(RETURNING);
        } else if (STACK.equals(key)) {
            this.stack = this.entityData.get(STACK);
        }
    }

    @Override
    public void tick() {
        super.tick();

        // Will be changed before moving.
        // But we need the original value later on
        boolean returningTickStart = this.returning;

        if (!this.returning) {
            this.checkForCollision();
        } else if (!this.level().isClientSide) {
            Vec3 returnPoint = this.getReturnPoint();
            if (returnPoint != null) {
                this.applyReturnMotion(returnPoint);
            }
            this.tryReturn(returnPoint);
        }


        Vec3 motion = this.getDeltaMovement();
        if (returningTickStart) {
            // The move method applies collisions. We don't want that
            // when returning, or we might be stuck
            Vec3 position = this.position();
            this.setPos(position.x + motion.x, position.y + motion.y, position.z + motion.z);
//            move(MoverType.SELF, motion);
        } else {
            this.move(MoverType.SELF, motion);
        }

        this.lifeLeft -= 1;

        if (this.lifeLeft <= 0) {
            this.startReturn();
        }

        Vec3 position = this.position();
        this.setPos(position.x, position.y, position.z);
    }

    private void checkForCollision() {
        // We also check on the client because it takes time for the server to update and this makes it smoother
        // however we don't trigger any logic here, just set the motion.
        Vec3 motion = this.getDeltaMovement();
        Vec3 position = this.position();
        Vec3 rayCast = position.add(motion);

        if (this.isAlive() && !this.entityData.get(RETURNING)) {
            HitResult result = ProjectileUtil.getEntityHitResult(this.level(), this, position, rayCast, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1),
                    entity -> entity != this && !entity.isSpectator() && entity.isAlive() && entity.isPickable() && entity != this.getThrower());

            if (result == null || result.getType() == HitResult.Type.MISS) {
                result = this.level().clip(new ClipContext(position, rayCast, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            }
            //noinspection ConstantConditions
            if (result != null && result.getType() != HitResult.Type.MISS) {
                if (!this.level().isClientSide) {
                    this.onHit(result);
                } else {
                    Vec3 returnPoint = this.getReturnPoint();
                    if (returnPoint != null) {
                        this.applyReturnMotionClient(returnPoint);
                    }
                }
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        if (!this.returning) {
            super.onHit(result);
            this.startReturn();
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity) {
            this.attackEntities((LivingEntity) result.getEntity());
        }
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult hit) {
        AABB aabb = new AABB(hit.getLocation(), hit.getLocation()).inflate(2);
        List<LivingEntity> living = this.level().getEntitiesOfClass(LivingEntity.class, aabb, entity -> !entity.isSpectator() && entity.isAlive() && entity != this.getThrower());
        if (!living.isEmpty()) {
            this.attackEntities(living.get(this.level().random.nextInt(living.size())));
        }
    }

    @Nullable
    @Override
    protected PortalInfo findDimensionEntryPoint(@Nonnull ServerLevel level) {
        return null;
    }
    
    @Nullable
    private Vec3 getReturnPoint() {
        Player throwerEntity = this.getThrower();
        if (throwerEntity != null) {
            return throwerEntity.position();
        } else if (this.throwPos != null) {
            return this.throwPos;
        } else {
            return null;
        }
    }

    private void applyReturnMotion(@Nonnull Vec3 returnPoint) {
        if (!this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            Vec3 position = this.position();
            Vec3 returnVec = new Vec3(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().multiply(0.6, 0.6, 0.6);
            // clamp because some mods think, it's a good idea to over enchant stuff on any type of tool they don't know about
            double loyalty = 1 + (0.07 * Mth.clamp(this.stack.getEnchantmentLevel(Enchantments.LOYALTY), 0, 3));
            Vec3 newMotion = new Vec3(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4).multiply(loyalty, loyalty, loyalty);
            this.setDeltaMovement(newMotion);
        }
    }

    private void applyReturnMotionClient(@Nonnull Vec3 returnPoint) {
        if (this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            Vec3 position = this.position();
            Vec3 returnVec = new Vec3(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().multiply(0.6, 0.6, 0.6);
            Vec3 newMotion = new Vec3(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4);
            this.setDeltaMovement(newMotion);
        }
    }

    private void tryReturn(@Nullable Vec3 returnPoint) {
        if (returnPoint == null) {
            // We don't know where we should fly. Just place it where we are.
            BlockMjoellnir.putInWorld(this.stack, this.level(), this.blockPosition());
            this.remove(RemovalReason.DISCARDED);
        } else if (returnPoint.distanceToSqr(this.position()) < 2) {
            Player throwerEntity = this.getThrower();
            if (throwerEntity != null) {
                if (!BlockMjoellnir.putInInventory(throwerEntity, this.stack, this.hotbarSlot)) {
                    BlockMjoellnir.putInWorld(this.stack, this.level(), BlockPos.containing(returnPoint));
                }
            } else {
                BlockMjoellnir.putInWorld(this.stack, this.level(), BlockPos.containing(returnPoint));
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void startReturn() {
        if (!this.returning) {
            this.setLifeLeft(0);
            this.setReturning(true);
        }
    }

    private void attackEntities(LivingEntity target) {
        AABB aabb = new AABB(target.position(), target.position()).inflate(4);
        List<LivingEntity> found = this.level().getEntitiesOfClass(LivingEntity.class, aabb, entity -> !entity.isSpectator() && entity.isAlive() && entity != this.getThrower());
        LightningBolt lightning = this.attackEntity(target);
        for (LivingEntity entity : found) {
            if (entity != target) {
                this.areaDamage(entity, lightning);
            }
        }
    }

    @Nullable
    private LightningBolt attackEntity(LivingEntity target) {
        if (!this.level().isClientSide) {
            if (this.stack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) >= 1) {
                target.setSecondsOnFire(5);
            }
            int knockback = this.stack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
            if (knockback > 0) {
                Vec3 vector3d = this.getDeltaMovement().multiply(1, 0, 1).normalize().scale(knockback * 0.6);
                if (vector3d.lengthSqr() > 0) {
                    target.push(vector3d.x, 0.1D, vector3d.z);
                }
            }
            int power = this.stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
            float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
            if (power > 0) {
                dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.getDamageBonus(power, target.getMobType(), this.stack));
            }
            Player thrower = this.getThrower();
            if (thrower instanceof ServerPlayer) {
                ModCriteria.MJOELLNIR.trigger((ServerPlayer) thrower, this.getStack(), target);
            }
            target.hurt(thrower == null ? this.level().damageSources().generic() : this.level().damageSources().mobProjectile(this, thrower), dmg);
            LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
            lightning.setPos(target.getX(), target.getY(), target.getZ());
            lightning.setVisualOnly(true);
            lightning.setCause(thrower instanceof ServerPlayer ? (ServerPlayer) thrower : null);
            this.level().addFreshEntity(lightning);
            // When the entity is struck by lightning it'll take lightning damage and be set
            // on fire. We don't want that, so we disable the lightning damage for that time
            // and reset the fire to the old state afterwards
            if (this.level() instanceof ServerLevel && !ForgeEventFactory.onEntityStruckByLightning(target, lightning)) {
                int fireTicks = target.getRemainingFireTicks();
                LivingEntity oldImmune = EventListener.lightningImmuneEntity;
                EventListener.lightningImmuneEntity = target;
                target.thunderHit((ServerLevel) this.level(), lightning);
                EventListener.lightningImmuneEntity = oldImmune;
                target.setRemainingFireTicks(fireTicks);
            }
            return lightning;
        } else {
            return null;
        }
    }

    private void areaDamage(LivingEntity target, @Nullable LightningBolt lightning) {
        if (this.stack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) >= 1) {
            target.setSecondsOnFire(2);
        }
        int power = this.stack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
        float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
        if (power > 0) {
            dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.getDamageBonus(power, target.getMobType(), this.stack));
        }
        dmg *= MythicConfig.mjoellnir.secondary_target_multiplier;
        Player thrower = this.getThrower();
        target.hurt(thrower == null ? this.level().damageSources().generic() : this.level().damageSources().mobProjectile(this, thrower), dmg);
        if (lightning != null && this.level().random.nextFloat() < MythicConfig.mjoellnir.secondary_lightning_chance) {
            int fireTicks = target.getRemainingFireTicks();
            LivingEntity oldImmune = EventListener.lightningImmuneEntity;
            EventListener.lightningImmuneEntity = target;
            target.thunderHit((ServerLevel) this.level(), lightning);
            EventListener.lightningImmuneEntity = oldImmune;
            target.setRemainingFireTicks(fireTicks);
        }
    }

    public boolean isReturning() {
        return this.returning;
    }

    public void setReturning(boolean returning) {
        this.entityData.set(RETURNING, returning);
        this.returning = returning;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.entityData.set(STACK, stack);
        this.stack = stack;
    }

    @Nullable
    public Player getThrower() {
        if (this.thrower == null) {
            return null;
        } else {
            return this.level().getPlayerByUUID(this.thrower);
        }
    }

    @Nullable
    public UUID getThrowerId() {
        return this.thrower;
    }

    public void setThrower(@Nullable Player thrower) {
        this.setThrowerId(thrower == null ? null : thrower.getGameProfile().getId());
    }

    public void setThrowerId(@Nullable UUID thrower) {
        this.thrower = thrower;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.getThrower();
    }

    @Nullable
    public Vec3 getThrowPos() {
        return this.throwPos;
    }

    public void setThrowPos(@Nullable Vec3 throwPos) {
        this.throwPos = throwPos;
    }

    public int getHotBarSlot() {
        return this.hotbarSlot;
    }

    public void setHotBarSlot(int hotBarSlot) {
        this.hotbarSlot = hotBarSlot;
    }

    public int getLifeLeft() {
        return this.lifeLeft;
    }

    public void setLifeLeft(int lifeLeft) {
        this.lifeLeft = lifeLeft;
    }

    @Override
    protected void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Stack", this.stack.save(new CompoundTag()));
        if (this.thrower != null) {
            compound.putBoolean("thrower", true);
            compound.putLong("throwerl", this.thrower.getLeastSignificantBits());
            compound.putLong("throwerm", this.thrower.getMostSignificantBits());
        } else {
            compound.putBoolean("thrower", false);
        }
        if (this.throwPos != null) {
            compound.putBoolean("throwPos", true);
            compound.putDouble("throwX", this.throwPos.x);
            compound.putDouble("throwY", this.throwPos.y);
            compound.putDouble("throwZ", this.throwPos.z);
        } else {
            compound.putBoolean("throwPos", false);
        }
        compound.putDouble("hotbar", this.hotbarSlot);
        compound.putDouble("lifeTime", this.lifeLeft);
        compound.putBoolean("returning", this.returning);
    }

    @Override
    protected void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setStack(ItemStack.of(compound.getCompound("Stack")));
        if (compound.getBoolean("thrower")) {
            this.setThrowerId(new UUID(compound.getLong("throwerm"), compound.getLong("throwerl")));
        } else {
            this.setThrowerId(null);
        }
        if (compound.getBoolean("throwPos")) {
            this.setThrowPos(new Vec3(compound.getDouble("throwX"), compound.getDouble("throwY"), compound.getDouble("throwZ")));
        } else {
            this.setThrowPos(null);
        }
        this.setHotBarSlot(compound.getInt("hotbar"));
        this.setLifeLeft(compound.getInt("lifeTime"));
        this.setReturning(compound.getBoolean("returning"));
    }
}
