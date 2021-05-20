package mythicbotany.alfheim.entity;

import mythicbotany.ModEntities;
import mythicbotany.alfheim.AlfheimWorldGen;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Random;

public class AlfPixie extends CreatureEntity {

    public AlfPixie(@Nonnull World world) {
        this(ModEntities.alfPixie, world);
    }

    public AlfPixie(EntityType<AlfPixie> type, World world) {
        super(type, world);
        this.experienceValue = 3;
        this.moveController = new MoveHelperController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAroundGoal(this));
        this.goalSelector.addGoal(12, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, EndermanEntity.class, 0, true, false, entity -> true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 0, true, false, entity -> true));
        this.initExtraAI();
    }

    protected void initExtraAI() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
    }

    public int getMaxSpawnedInChunk() {
        return 7;
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.isAlive()) {
            if (this.ticksExisted % 10 == 0 && this.deathTime == 0) {
                this.heal(1);
            }
        }
        if (world.isRemote) {
            for (int i = 0; i < 4; i++) {
                SparkleParticleData data = SparkleParticleData.sparkle(0.1F + (float) Math.random() * 0.25f, 1, 0.25f, 0.9f, 12);
                world.addParticle(data, getPosX() + (Math.random() - 0.5) * 0.25,
                        getPosY() + 0.5 + (Math.random() - 0.5) * 0.25,
                        getPosZ() + (Math.random() - 0.5) * 0.25,
                        0, 0, 0);
            }
        }
    }

    public boolean isOnLadder() {
        return false;
    }

    @Override
    public void travel(@Nonnull Vector3d vec) {
        if (this.isInWater()) {
            this.moveRelative(0.02f, vec);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.8));
        } else if (this.isInLava()) {
            this.moveRelative(0.02f, vec);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.5));
        } else {
            BlockPos ground = new BlockPos(this.getPosX(), this.getPosY() - 1, this.getPosZ());
            float slipperiness = 0.91f;
            if (this.onGround) {
                slipperiness = this.world.getBlockState(ground).getSlipperiness(this.world, ground, this) * 0.91f;
            }

            float groundModifier = (19 / 3f) / (slipperiness * slipperiness * slipperiness);
            slipperiness = 0.91F;
            if (this.onGround) {
                slipperiness = this.world.getBlockState(ground).getSlipperiness(this.world, ground, this) * 0.91f;
            }

            this.moveRelative(this.onGround ? 0.1F * groundModifier : 0.02F, vec);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(slipperiness));
        }
    }

    @Override
    public boolean onLivingFall(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public static AttributeModifierMap entityAttributes() {
        return MobEntity.getDefaultAttributes()
                .createMutableAttribute(Attributes.MAX_HEALTH, 4)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3)
                .create();
    }

    public static boolean canSpawnAt(EntityType<AlfPixie> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        //noinspection deprecation
        return world.getBlockState(pos).isAir(world, pos);
    }

    private static class LookAroundGoal extends Goal {

        private final AlfPixie entity;

        public LookAroundGoal(AlfPixie entity) {
            this.entity = entity;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean shouldExecute() {
            return true;
        }

        public void tick() {
            if (this.entity.getAttackTarget() == null) {
                Vector3d motion = this.entity.getMotion();
                this.entity.rotationYaw = -((float) MathHelper.atan2(motion.x, motion.z)) * ((float) (180 / Math.PI));
                this.entity.renderYawOffset = this.entity.rotationYaw;
            } else {
                LivingEntity target = this.entity.getAttackTarget();
                if (target.getDistanceSq(this.entity) < 64 * 64) {
                    double xd = target.getPosX() - this.entity.getPosX();
                    double zd = target.getPosZ() - this.entity.getPosZ();
                    this.entity.rotationYaw = -((float) MathHelper.atan2(xd, zd)) * ((float) (180 / Math.PI));
                    this.entity.renderYawOffset = this.entity.rotationYaw;
                }
            }

        }
    }

    private static class MoveHelperController extends MovementController {

        private final AlfPixie entity;
        private int cooldown;

        public MoveHelperController(AlfPixie entity) {
            super(entity);
            this.entity = entity;
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                if (this.cooldown-- <= 0) {
                    this.cooldown += this.entity.getRNG().nextInt(5) + 2;
                    Vector3d motion = new Vector3d(this.posX - this.entity.getPosX(), this.posY - this.entity.getPosY(), this.posZ - this.entity.getPosZ());
                    double d0 = motion.length();
                    motion = motion.normalize();
                    if (this.checkNoCollision(motion, MathHelper.ceil(d0))) {
                        this.entity.setMotion(this.entity.getMotion().add(motion.scale(0.1)));
                    } else {
                        this.action = MovementController.Action.WAIT;
                    }
                }
            }
        }

        private boolean checkNoCollision(Vector3d p_220673_1_, int p_220673_2_) {
            AxisAlignedBB aabb = this.entity.getBoundingBox();
            for (int i = 1; i < p_220673_2_; ++i) {
                aabb = aabb.offset(p_220673_1_);
                if (!this.entity.world.hasNoCollisions(this.entity, aabb)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class RandomFlyGoal extends Goal {

        private final AlfPixie entity;

        public RandomFlyGoal(AlfPixie entity) {
            this.entity = entity;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            MovementController controller = this.entity.getMoveHelper();
            if (!controller.isUpdating()) {
                return true;
            } else {
                double xd = controller.getX() - this.entity.getPosX();
                double yd = controller.getY() - this.entity.getPosY();
                double zd = controller.getZ() - this.entity.getPosZ();
                double m = xd * xd + yd * yd + zd * zd;
                return m < 1 || m > 60 * 60;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            int aboveGround = 0;
            BlockPos.Mutable mpos = entity.getPosition().down().toMutable();
            while (mpos.getY() > 0 && AlfheimWorldGen.passReplaceableAndDreamwood(entity.world.getBlockState(mpos))) {
                mpos.move(Direction.DOWN);
                aboveGround += 1;
                if (aboveGround >= 10) break;
            }
            Random random = this.entity.getRNG();
            double x = this.entity.getPosX() + ((random.nextDouble() * 2) - 1) * 16;
            double y = this.entity.getPosY() + (((random.nextDouble() * 2) - 1) * 16) - (2 * (aboveGround - 5));
            double z = this.entity.getPosZ() + ((random.nextDouble() * 2) - 1) * 16;
            this.entity.getMoveHelper().setMoveTo(x, y, z, 0.8);
        }
    }
}
