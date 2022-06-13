package mythicbotany.alfheim.content;

import mythicbotany.ModEntities;
import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Random;

public class AlfPixie extends PathfinderMob {

    public AlfPixie(@Nonnull Level level) {
        this(ModEntities.alfPixie, level);
    }

    public AlfPixie(EntityType<AlfPixie> type, Level level) {
        super(type, level);
        this.xpReward = 3;
        this.moveControl = new MoveHelperController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAroundGoal(this));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, EnderMan.class, 0, true, false, entity -> true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Endermite.class, 0, true, false, entity -> true));
        this.initExtraAI();
    }

    protected void initExtraAI() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    public int getMaxSpawnClusterSize() {
        return 7;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.isAlive()) {
            if (this.tickCount % 10 == 0 && this.deathTime == 0) {
                this.heal(1);
            }
        }
        if (this.level.isClientSide) {
            for (int i = 0; i < 4; i++) {
                SparkleParticleData data = SparkleParticleData.sparkle(0.1F + (float) Math.random() * 0.25f, 1, 0.25f, 0.9f, 12);
                this.level.addParticle(data, this.getX() + (Math.random() - 0.5) * 0.25,
                        this.getY() + 0.5 + (Math.random() - 0.5) * 0.25,
                        this.getZ() + (Math.random() - 0.5) * 0.25,
                        0, 0, 0);
            }
        }
    }

    public boolean onClimbable() {
        return false;
    }

    @Override
    public void travel(@Nonnull Vec3 travelVector) {
        if (this.isInWater()) {
            this.moveRelative(0.02f, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
        } else if (this.isInLava()) {
            this.moveRelative(0.02f, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
        } else {
            float groundModifier = (19 / 3f) / 0.753571f;
            this.moveRelative(this.onGround ? 0.1F * groundModifier : 0.02f, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.91f));
        }
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    public static AttributeSupplier entityAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 32)
                .build();
    }

    public static boolean canSpawnAt(EntityType<AlfPixie> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return level.getBlockState(pos).isAir();
    }

    private static class LookAroundGoal extends Goal {

        private final AlfPixie entity;

        public LookAroundGoal(AlfPixie entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            if (this.entity.getTarget() == null) {
                Vec3 motion = this.entity.getDeltaMovement();
                this.entity.setYRot(-((float) Mth.atan2(motion.x, motion.z)) * ((float) (180 / Math.PI)));
                this.entity.yBodyRot = this.entity.getYRot();
            } else {
                LivingEntity target = this.entity.getTarget();
                if (target.distanceToSqr(this.entity) < 64 * 64) {
                    double xd = target.getX() - this.entity.getX();
                    double zd = target.getZ() - this.entity.getZ();
                    this.entity.setYRot(-((float) Mth.atan2(xd, zd)) * ((float) (180 / Math.PI)));
                    this.entity.yBodyRot = this.entity.getYRot();
                }
            }
        }
    }

    private static class MoveHelperController extends MoveControl {

        private final AlfPixie entity;
        private int cooldown;

        public MoveHelperController(AlfPixie entity) {
            super(entity);
            this.entity = entity;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.cooldown-- <= 0) {
                    this.cooldown += this.entity.getRandom().nextInt(5) + 2;
                    Vec3 motion = new Vec3(this.wantedX - this.entity.getX(), this.wantedY - this.entity.getY(), this.wantedZ - this.entity.getZ());
                    double d0 = motion.length();
                    motion = motion.normalize();
                    if (this.checkNoCollision(motion, Mth.ceil(d0))) {
                        this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(motion.scale(0.1)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean checkNoCollision(Vec3 p_220673_1_, int p_220673_2_) {
            AABB aabb = this.entity.getBoundingBox();
            for (int i = 1; i < p_220673_2_; ++i) {
                aabb = aabb.move(p_220673_1_);
                if (!this.entity.level.noCollision(this.entity, aabb)) {
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl controller = this.entity.getMoveControl();
            if (!controller.hasWanted()) {
                return true;
            } else {
                double xd = controller.getWantedX() - this.entity.getX();
                double yd = controller.getWantedY() - this.entity.getY();
                double zd = controller.getWantedZ() - this.entity.getZ();
                double m = xd * xd + yd * yd + zd * zd;
                return m < 1 || m > 60 * 60;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            int aboveGround = 0;
            BlockPos.MutableBlockPos mpos = this.entity.blockPosition().below().mutable();
            while (mpos.getY() > 0 && AlfheimWorldGenUtil.passReplaceableAndDreamWood(this.entity.level.getBlockState(mpos))) {
                mpos.move(Direction.DOWN);
                aboveGround += 1;
                if (aboveGround >= 10) break;
            }
            Random random = this.entity.getRandom();
            double x = this.entity.getX() + ((random.nextDouble() * 2) - 1) * 16;
            double y = this.entity.getY() + (((random.nextDouble() * 2) - 1) * 16) - (2 * (aboveGround - 5));
            double z = this.entity.getZ() + ((random.nextDouble() * 2) - 1) * 16;
            this.entity.getMoveControl().setWantedPosition(x, y, z, 0.8);
        }
    }
}
