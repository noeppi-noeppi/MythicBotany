package mythicbotany.infuser;

import com.google.common.base.Predicates;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import mythicbotany.MythicBotany;
import mythicbotany.misc.SolidifiedMana;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class TileManaInfuser extends BlockEntityBase implements ISparkAttachable, TickableBlock {

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

    public TileManaInfuser(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        //noinspection ConstantConditions
        if (level.isClientSide || !hasValidPlatform())
            return;
        if (active && recipe != null && mana > 0) {
            MythicBotany.getNetwork().spawnInfusionParticles(level, worldPosition, mana / (float) recipe.getManaUsage(), recipe.fromColor(), recipe.toColor());
        }
        List<ItemEntity> items = getItems();
        List<ItemStack> stacks = items.stream().map(ItemEntity::getItem).collect(Collectors.toList());
        if (active && recipe != null && output != null) {
            if (recipe.result(stacks).isEmpty()) {
                SolidifiedMana.dropMana(level, worldPosition, mana);
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                output = null;
                level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                setChanged();
            } else if (mana >= recipe.getManaUsage()) {
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                items.forEach(ie -> ie.remove(Entity.RemovalReason.DISCARDED));
                ItemEntity outItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, output);
                level.addFreshEntity(outItem);
                output = null;
                setChanged();
            } else {
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
            }
        } else {
            Pair<IInfuserRecipe, ItemStack> match = InfuserRecipe.getOutput(this.level, stacks);
            if (match != null) {
                if (!active) {
                    active = true;
                    recipe = match.getLeft();
                    mana = 0;
                } else {
                    recipe = match.getLeft();
                    mana = Mth.clamp(mana, 0, recipe.getManaUsage());
                }
                maxMana = recipe.getManaUsage();
                fromColor = recipe.fromColor();
                toColor = recipe.toColor();
                output = match.getRight();
                level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                items.forEach(ie -> MythicBotany.getNetwork().setItemMagnetImmune(ie));
                setChanged();
            } else if (active || recipe != null || output != null) {
                SolidifiedMana.dropMana(level, worldPosition, mana);
                active = false;
                recipe = null;
                fromColor = -1;
                toColor = -1;
                mana = 0;
                maxMana = 0;
                output = null;
                level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
                setChanged();
            }
        }
    }

    private List<ItemEntity> getItems() {
        //noinspection ConstantConditions
        return this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition, this.worldPosition.offset(1, 1, 1)));
    }

    private boolean hasValidPlatform() {
        BlockPos center = worldPosition.below();
        //noinspection ConstantConditions
        return level.getBlockState(center).getBlock() == ModBlocks.shimmerrock
                && level.getBlockState(center.north().west()).getBlock() == ModBlocks.shimmerrock
                && level.getBlockState(center.north().east()).getBlock() == ModBlocks.shimmerrock
                && level.getBlockState(center.south().west()).getBlock() == ModBlocks.shimmerrock
                && level.getBlockState(center.south().east()).getBlock() == ModBlocks.shimmerrock
                && level.getBlockState(center.north()).getBlock() == Blocks.GOLD_BLOCK
                && level.getBlockState(center.east()).getBlock() == Blocks.GOLD_BLOCK
                && level.getBlockState(center.south()).getBlock() == Blocks.GOLD_BLOCK
                && level.getBlockState(center.west()).getBlock() == Blocks.GOLD_BLOCK;
    }

    @Override
    public boolean canAttachSpark(ItemStack itemStack) {
        return true;
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
    public IManaSpark getAttachedSpark() {
        @SuppressWarnings("ConstantConditions")
        List<Entity> sparks = this.level.getEntitiesOfClass(Entity.class, new AABB(this.worldPosition.above(), this.worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(IManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (IManaSpark) e;
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
            mana = Mth.clamp(mana + i, 0, recipe.getManaUsage());
            //noinspection ConstantConditions
            level.updateNeighbourForOutputSignal(this.worldPosition, this.getBlockState().getBlock());
            setChanged();
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
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        mana = nbt.getInt("mana");
        if (nbt.contains("output")) {
            output = ItemStack.of(nbt.getCompound("output"));
        } else {
            output = null;
        }
        active = nbt.getBoolean("active");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("mana", mana);
        if (output != null) {
            nbt.put("output", output.save(new CompoundTag()));
        }
        nbt.putBoolean("active", active);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        //noinspection ConstantConditions
        if (level.isClientSide)
            return super.getUpdateTag();
        CompoundTag compound = super.getUpdateTag();
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
    public void handleUpdateTag(CompoundTag tag) {
        //noinspection ConstantConditions
        if (!level.isClientSide)
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

    public double getProgress() {
        if (maxMana <= 0) {
            return -1;
        } else {
            return mana / (double) maxMana;
        }
    }
}
