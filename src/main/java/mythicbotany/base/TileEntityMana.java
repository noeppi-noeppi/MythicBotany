package mythicbotany.base;

import com.google.common.base.Predicates;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class TileEntityMana extends TileEntityBase implements ISparkAttachable, IManaCollector {

    public final int maxMana;
    private final boolean bursts;
    private final boolean sparks;

    protected int mana;

    public TileEntityMana(TileEntityType<?> tileEntityTypeIn, int maxMana, boolean bursts, boolean sparks) {
        super(tileEntityTypeIn);
        this.maxMana = maxMana;
        this.bursts = bursts;
        this.sparks = sparks;
    }

    protected abstract boolean canReceive();

    @Override
    public void onClientDisplayTick() {

    }

    @Override
    public float getManaYieldMultiplier(IManaBurst iManaBurst) {
        return 1;
    }

    @Override
    public int getMaxMana() {
        return maxMana;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return sparks;
    }

    @Override
    public void attachSpark(ISparkEntity iSparkEntity) {

    }

    @Override
    public int getAvailableSpaceForMana() {
        return MathHelper.clamp(maxMana - mana, 0, maxMana);
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        @SuppressWarnings("ConstantConditions")
        List<Entity> sparks = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos.up(), this.pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ISparkEntity)e;
        } else {
            return null;
        }
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return mana < maxMana && canReceive();
    }

    @Override
    public boolean isFull() {
        return mana >= maxMana;
    }

    @Override
    public void receiveMana(int i) {
        mana = MathHelper.clamp(mana + i, 0, maxMana);
        onManaChange();
        markDirty();
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return bursts;
    }

    @Override
    public int getCurrentMana() {
        return MathHelper.clamp(mana, 0, maxMana);
    }

    @Override
    public void read(@Nonnull BlockState stateIn, @Nonnull CompoundNBT nbtIn) {
        super.read(stateIn, nbtIn);
        if (nbtIn.contains("mana", Constants.NBT.TAG_INT)) {
            this.mana = MathHelper.clamp(nbtIn.getInt("mana"), 0, maxMana);
        } else {
            this.mana = 0;
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.putInt("mana", MathHelper.clamp(mana, 0, maxMana));
        return super.write(compound);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        //noinspection ConstantConditions
        if (!world.isRemote) {
            tag.putInt("mana", mana);
        }
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        //noinspection ConstantConditions
        if (world.isRemote) {
            mana = MathHelper.clamp(tag.getInt("mana"), 0, maxMana);
        }
    }

    protected void onManaChange() {

    }
}
