package mythicbotany.mjoellnir;

import io.github.noeppi_noeppi.libx.util.BoundingBoxUtils;
import mythicbotany.EventListener;
import mythicbotany.ModBlocks;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.config.MythicConfig;
import net.minecraft.block.PortalInfo;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityMjoellnir extends ProjectileEntity {

    private static final DataParameter<Boolean> RETURNING = EntityDataManager.createKey(EntityMjoellnir.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityMjoellnir.class, DataSerializers.ITEMSTACK);

    private boolean returning = false;
    private ItemStack stack = ItemStack.EMPTY;
    private UUID thrower = null;
    private Vector3d throwPos = null;
    // 0-8: hotbar, 9: offhand
    private int hotbarSlot = 0;
    private int lifeLeft = 40;

    public EntityMjoellnir(World world) {
        this(ModBlocks.mjoellnir.getEntityType(), world);
    }

    public EntityMjoellnir(EntityType<? extends ProjectileEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void registerData() {
        dataManager.register(RETURNING, false);
        dataManager.register(STACK, ItemStack.EMPTY);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void notifyDataManagerChange(@Nonnull DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (RETURNING.equals(key)) {
            returning = dataManager.get(RETURNING);
        } else if (STACK.equals(key)) {
            stack = dataManager.get(STACK);
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
        } else if (!world.isRemote) {
            Vector3d returnPoint = getReturnPoint();
            if (returnPoint != null) {
                applyReturnMotion(returnPoint);
            }
            tryReturn(returnPoint);
        }


        Vector3d motion = this.getMotion();
        if (returningTickStart) {
            // The move method applies collisions. We don't want that
            // when returning or we might be stuck
            Vector3d position = this.getPositionVec();
            setPosition(position.x + motion.x, position.y + motion.y, position.z + motion.z);
//            move(MoverType.SELF, motion);
        } else {
            move(MoverType.SELF, motion);
        }

        lifeLeft -= 1;

        if (lifeLeft <= 0) {
            startReturn();
        }

        Vector3d position = getPositionVec();
        this.setPosition(position.x, position.y, position.z);
    }

    private void checkForCollision() {
        // We also check on the client because it takes time for the server to update and this makes it smoother
        // however we don't trigger any logic here, just set the motion.
        Vector3d motion = getMotion();
        Vector3d position = getPositionVec();
        Vector3d rayCast = position.add(motion);

        if (isAlive() && !dataManager.get(RETURNING)) {
            RayTraceResult result = ProjectileHelper.rayTraceEntities(world, this, position, rayCast, getBoundingBox().expand(getMotion()).grow(1),
                    entity -> entity != this && !entity.isSpectator() && entity.isAlive() && entity.canBeCollidedWith() && entity != getThrower());

            if (result == null || result.getType() == RayTraceResult.Type.MISS) {
                result = world.rayTraceBlocks(new RayTraceContext(position, rayCast, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            }
            //noinspection ConstantConditions
            if (result != null && result.getType() != RayTraceResult.Type.MISS) {
                if (!world.isRemote) {
                    onImpact(result);
                } else {
                    Vector3d returnPoint = getReturnPoint();
                    if (returnPoint != null) {
                        applyReturnMotionClient(returnPoint);
                    }
                }
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult hit) {
        if (!returning) {
            super.onImpact(hit);
            startReturn();
        }
    }

    @Override
    protected void onEntityHit(@Nonnull EntityRayTraceResult hit) {
        if (hit.getEntity() instanceof LivingEntity) {
            attackEntities((LivingEntity) hit.getEntity());
        }
    }

    @Override
    protected void onBlockHit(@Nonnull BlockRayTraceResult hit) {
        List<LivingEntity> living = world.getEntitiesWithinAABB(LivingEntity.class, BoundingBoxUtils.expand(hit.getHitVec(), 2),
                entity -> !entity.isSpectator() && entity.isAlive() && entity != getThrower());
        if (!living.isEmpty()) {
            attackEntities(living.get(world.rand.nextInt(living.size())));
        }
    }

    @Nullable
    @Override
    protected PortalInfo getPortalInfo(@Nonnull ServerWorld world) {
        return null;
    }
    
    @Nullable
    private Vector3d getReturnPoint() {
        PlayerEntity throwerEntity = getThrower();
        if (throwerEntity != null) {
            return throwerEntity.getPositionVec();
        } else if (throwPos != null) {
            return throwPos;
        } else {
            return null;
        }
    }

    private void applyReturnMotion(@Nonnull Vector3d returnPoint) {
        if (!world.isRemote) {
            Vector3d motion = getMotion();
            Vector3d position = getPositionVec();
            Vector3d returnVec = new Vector3d(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().mul(0.6, 0.6, 0.6);
            // clamp because some mods think, it's a good idea to over enchant stuff on any type of tool they don't know about
            double loyalty = 1 + (0.07 * MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.LOYALTY, stack), 0, 3));
            Vector3d newMotion = new Vector3d(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4).mul(loyalty, loyalty, loyalty);
            setMotion(newMotion);
        }
    }

    private void applyReturnMotionClient(@Nonnull Vector3d returnPoint) {
        if (world.isRemote) {
            Vector3d motion = getMotion();
            Vector3d position = getPositionVec();
            Vector3d returnVec = new Vector3d(returnPoint.x - position.x, returnPoint.y - position.y, returnPoint.z - position.z).normalize().mul(0.6, 0.6, 0.6);
            Vector3d newMotion = new Vector3d(((3 * motion.x) + returnVec.x) / 4, ((3 * motion.y) + returnVec.y) / 4, ((3 * motion.z) + returnVec.z) / 4);
            setMotion(newMotion);
        }
    }

    private void tryReturn(@Nullable Vector3d returnPoint) {
        if (returnPoint == null) {
            // We don't know where we should fly. Just place it where we are.
            BlockMjoellnir.putInWorld(stack, world, getPosition());
            this.remove();
        } else if (returnPoint.squareDistanceTo(getPositionVec()) < 2) {
            PlayerEntity throwerEntity = getThrower();
            if (throwerEntity != null) {
                if (!BlockMjoellnir.putInInventory(throwerEntity, stack, hotbarSlot)) {
                    BlockMjoellnir.putInWorld(stack, world, new BlockPos(returnPoint));
                }
            } else {
                BlockMjoellnir.putInWorld(stack, world, new BlockPos(returnPoint));
            }
            this.remove();
        }
    }

    private void startReturn() {
        if (!returning) {
            setLifeLeft(0);
            setReturning(true);
        }
    }

    private void attackEntities(LivingEntity target) {
        List<LivingEntity> found = world.getEntitiesWithinAABB(LivingEntity.class, BoundingBoxUtils.expand(target.getPositionVec(), 4),
                entity -> !entity.isSpectator() && entity.isAlive() && entity != getThrower());
        LightningBoltEntity lightning = attackEntity(target);
        for (LivingEntity entity : found) {
            if (entity != target) {
                areaDamage(entity, lightning);
            }
        }
    }

    @Nullable
    private LightningBoltEntity attackEntity(LivingEntity target) {
        if (!world.isRemote) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) >= 1) {
                target.setFire(5);
            }
            int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
            if (knockback > 0) {
                Vector3d vector3d = this.getMotion().mul(1, 0, 1).normalize().scale(knockback * 0.6);
                if (vector3d.lengthSquared() > 0) {
                    target.addVelocity(vector3d.x, 0.1D, vector3d.z);
                }
            }
            int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
            float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
            if (power > 0) {
                dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.calcDamageByCreature(power, target.getCreatureAttribute()));
            }
            PlayerEntity thrower = getThrower();
            if (thrower instanceof ServerPlayerEntity) {
                ModCriteria.MJOELLNIR.trigger((ServerPlayerEntity) thrower, getStack(), target);
            }
            target.attackEntityFrom(thrower == null ? DamageSource.GENERIC : DamageSource.causeIndirectDamage(this, thrower), dmg);
            LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
            lightning.setEffectOnly(true);
            lightning.setCaster(thrower instanceof ServerPlayerEntity ? (ServerPlayerEntity) thrower : null);
            world.addEntity(lightning);
            // When the entity is struck by lightning it'll take lightning damage and be set
            // on fire. We don't want that so we disable the lightning damage for that time
            // and reset the fire to the old state afterwards
            if (world instanceof ServerWorld && !ForgeEventFactory.onEntityStruckByLightning(target, lightning)) {
                int fireTicks = target.getFireTimer();
                LivingEntity oldImmune = EventListener.lightningImmuneEntity;
                EventListener.lightningImmuneEntity = target;
                target.causeLightningStrike((ServerWorld) world, lightning);
                EventListener.lightningImmuneEntity = oldImmune;
                target.forceFireTicks(fireTicks);
            }
            return lightning;
        } else {
            return null;
        }
    }

    private void areaDamage(LivingEntity target, @Nullable LightningBoltEntity lightning) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) >= 1) {
            target.setFire(2);
        }
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
        float dmg = MythicConfig.mjoellnir.base_damage_ranged + 1;
        if (power > 0) {
            dmg += (MythicConfig.mjoellnir.enchantment_multiplier * Enchantments.SHARPNESS.calcDamageByCreature(power, target.getCreatureAttribute()));
        }
        dmg *= MythicConfig.mjoellnir.secondary_target_multiplier;
        PlayerEntity thrower = getThrower();
        target.attackEntityFrom(thrower == null ? DamageSource.GENERIC : DamageSource.causeIndirectDamage(this, thrower), dmg);
        if (lightning != null && world.rand.nextFloat() < MythicConfig.mjoellnir.secondary_lightning_chance) {
            int fireTicks = target.getFireTimer();
            LivingEntity oldImmune = EventListener.lightningImmuneEntity;
            EventListener.lightningImmuneEntity = target;
            target.causeLightningStrike((ServerWorld) world, lightning);
            EventListener.lightningImmuneEntity = oldImmune;
            target.forceFireTicks(fireTicks);
        }
    }

    public boolean isReturning() {
        return returning;
    }

    public void setReturning(boolean returning) {
        dataManager.set(RETURNING, returning);
        this.returning = returning;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        dataManager.set(STACK, stack);
        this.stack = stack;
    }

    @Nullable
    public PlayerEntity getThrower() {
        if (world == null || thrower == null) {
            return null;
        } else {
            return world.getPlayerByUuid(thrower);
        }
    }

    @Nullable
    public UUID getThrowerId() {
        return thrower;
    }

    public void setThrower(@Nullable PlayerEntity thrower) {
        setThrowerId(thrower == null ? null : thrower.getGameProfile().getId());
    }

    public void setThrowerId(@Nullable UUID thrower) {
        this.thrower = thrower;
    }

    @Nullable
    public Vector3d getThrowPos() {
        return throwPos;
    }

    public void setThrowPos(@Nullable Vector3d throwPos) {
        this.throwPos = throwPos;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public void setHotbarSlot(int hotbarSlot) {
        this.hotbarSlot = hotbarSlot;
    }

    public int getLifeLeft() {
        return lifeLeft;
    }

    public void setLifeLeft(int lifeLeft) {
        this.lifeLeft = lifeLeft;
    }

    @Override
    protected void writeAdditional(@Nonnull CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.put("Stack", stack.write(new CompoundNBT()));
        if (thrower != null) {
            nbt.putBoolean("thrower", true);
            nbt.putLong("throwerl", thrower.getLeastSignificantBits());
            nbt.putLong("throwerm", thrower.getMostSignificantBits());
        } else {
            nbt.putBoolean("thrower", false);
        }
        if (throwPos != null) {
            nbt.putBoolean("throwPos", true);
            nbt.putDouble("throwX", throwPos.x);
            nbt.putDouble("throwY", throwPos.y);
            nbt.putDouble("throwZ", throwPos.z);
        } else {
            nbt.putBoolean("throwPos", false);
        }
        nbt.putDouble("hotbar", hotbarSlot);
        nbt.putDouble("lifeTime", lifeLeft);
        nbt.putBoolean("returning", returning);
    }

    @Override
    protected void readAdditional(@Nonnull CompoundNBT nbt) {
        super.readAdditional(nbt);
        setStack(ItemStack.read(nbt.getCompound("Stack")));
        if (nbt.getBoolean("thrower")) {
            setThrowerId(new UUID(nbt.getLong("throwerm"), nbt.getLong("throwerl")));
        } else {
            setThrowerId(null);
        }
        if (nbt.getBoolean("throwPos")) {
            setThrowPos(new Vector3d(nbt.getDouble("throwX"), nbt.getDouble("throwY"), nbt.getDouble("throwZ")));
        } else {
            setThrowPos(null);
        }
        setHotbarSlot(nbt.getInt("hotbar"));
        setLifeLeft(nbt.getInt("lifeTime"));
        setReturning(nbt.getBoolean("returning"));
    }
}
