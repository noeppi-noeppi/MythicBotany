package mythicbotany.infuser;

import com.google.common.base.Predicates;
import io.github.noeppi_noeppi.libx.mod.registration.TileEntityBase;
import mythicbotany.MythicBotany;
import mythicbotany.misc.SolidifiedMana;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TileManaInfuser extends TileEntityBase implements ISparkAttachable, ITickableTileEntity {

    private int mana;
    private boolean active;
    @Nullable
    private transient IInfuserRecipe recipe;
    @Nullable
    private ItemStack output;

    // The following are just there for the client
    private transient int maxMana;
    private transient int fromColor = -1;
    private transient int toColor = -1;

    public TileManaInfuser(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        //noinspection ConstantConditions
        if (world.isRemote || !hasValidPlatform())
            return;
        if (active && recipe != null && mana > 0) {
            MythicBotany.getNetwork().spawnInfusionParticles(world, pos, mana / (float) recipe.getManaUsage(), recipe.fromColor(), recipe.toColor());
        }
        List<ItemEntity> items = getItems();
        List<ItemStack> stacks = items.stream().map(ItemEntity::getItem).collect(Collectors.toList());
        if (active && recipe != null && output != null) {
            if (recipe.result(stacks).isEmpty()) {
                SolidifiedMana.dropMana(world, pos, mana);
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                output = null;
                world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
                markDirty();
            } else if (mana >= recipe.getManaUsage()) {
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
                items.forEach(Entity::remove);
                ItemEntity outItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);
                world.addEntity(outItem);
                output = null;
                markDirty();
            } else {
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
            }
        } else {
            Pair<IInfuserRecipe, ItemStack> match = InfuserRecipe.getOutput(this.world, stacks);
            if (match != null) {
                if (!active) {
                    active = true;
                    recipe = match.getLeft();
                    mana = 0;
                } else {
                    recipe = match.getLeft();
                    mana = MathHelper.clamp(mana, 0, recipe.getManaUsage());
                }
                maxMana = recipe.getManaUsage();
                fromColor = recipe.fromColor();
                toColor = recipe.toColor();
                output = match.getRight();
                world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
                markDirty();
            } else if (active || recipe != null || output != null) {
                SolidifiedMana.dropMana(world, pos, mana);
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                output = null;
                world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
                markDirty();
            }
        }
    }

    private List<ItemEntity> getItems() {
        //noinspection ConstantConditions
        return this.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1)));
    }

    private boolean hasValidPlatform() {
        BlockPos center = pos.down();
        //noinspection ConstantConditions
        return world.getBlockState(center).getBlock() == ModBlocks.shimmerrock
                && world.getBlockState(center.north().west()).getBlock() == ModBlocks.shimmerrock
                && world.getBlockState(center.north().east()).getBlock() == ModBlocks.shimmerrock
                && world.getBlockState(center.south().west()).getBlock() == ModBlocks.shimmerrock
                && world.getBlockState(center.south().east()).getBlock() == ModBlocks.shimmerrock
                && world.getBlockState(center.north()).getBlock() == Blocks.GOLD_BLOCK
                && world.getBlockState(center.east()).getBlock() == Blocks.GOLD_BLOCK
                && world.getBlockState(center.south()).getBlock() == Blocks.GOLD_BLOCK
                && world.getBlockState(center.west()).getBlock() == Blocks.GOLD_BLOCK;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return true;
    }

    @Override
    public void attachSpark(ISparkEntity iSparkEntity) {

    }

    @Override
    public int getAvailableSpaceForMana() {
        if (recipe != null) {
            return Math.max(0, recipe.getManaUsage() - mana);
        } else {
            return 0;
        }
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        @SuppressWarnings("ConstantConditions")
        List<Entity> sparks = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos.up(), this.pos.up().add(1, 1, 1)), Predicates.instanceOf(ISparkEntity.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (ISparkEntity) e;
        } else {
            return null;
        }
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return !active;
    }

    @Override
    public boolean isFull() {
        if (recipe == null) {
            return true;
        } else {
            return mana >= recipe.getManaUsage();
        }
    }

    @Override
    public void receiveMana(int i) {
        if (recipe != null) {
            mana = MathHelper.clamp(mana + i, 0, recipe.getManaUsage());
            //noinspection ConstantConditions
            world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
            markDirty();
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return active;
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public void read(@Nonnull BlockState stateIn, @Nonnull CompoundNBT compound) {
        super.read(stateIn, compound);
        mana = compound.getInt("mana");
        if (compound.contains("output")) {
            output = ItemStack.read(compound.getCompound("output"));
        } else {
            output = null;
        }
        active = compound.getBoolean("active");
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        compound.putInt("mana", mana);
        if (output != null) {
            compound.put("output", output.write(new CompoundNBT()));
        }
        compound.putBoolean("active", active);
        return super.write(compound);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        //noinspection ConstantConditions
        if (world.isRemote)
            return super.getUpdateTag();
        CompoundNBT compound = super.getUpdateTag();
        compound.putInt("mana", mana);
        compound.putInt("max", maxMana);
        if (recipe != null) {
            compound.putInt("fromColor", fromColor);
            compound.putInt("toColor", toColor);
        } else {
            compound.putInt("fromColor", -1);
            compound.putInt("toColor", -1);
        }
        return compound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        //noinspection ConstantConditions
        if (!world.isRemote)
            return;
        mana = tag.getInt("mana");
        maxMana = tag.getInt("maxMana");
        fromColor = tag.getInt("fromColor");
        toColor = tag.getInt("toColor");
    }

    public int getSourceColor() {
        return fromColor;
    }

    public int getTargetColor() {
        return toColor;
    }

    public double getProgess() {
        if (maxMana <= 0) {
            return -1;
        } else {
            return mana / (double) maxMana;
        }
    }
}
