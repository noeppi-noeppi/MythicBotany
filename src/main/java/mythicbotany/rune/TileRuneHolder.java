package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import io.github.noeppi_noeppi.libx.inventory.ItemStackHandlerWrapper;
import io.github.noeppi_noeppi.libx.mod.registration.TileEntityBase;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.ModItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileRuneHolder extends TileEntityBase {

    private final BaseItemStackHandler inventory;
    private final LazyOptional<IItemHandlerModifiable> itemCap;

    @Nullable
    private BlockPos target;
    private double floatProgress;
    
    public TileRuneHolder(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        inventory = new BaseItemStackHandler(1, slot -> {
            this.markDirty();
            this.markDispatchable();
        }, (slot, stack) -> ModItemTags.RITUAL_RUNES.contains(stack.getItem()));
        inventory.setDefaultSlotLimit(1);
        itemCap = ItemStackHandlerWrapper.createLazy(() -> inventory);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //noinspection unchecked
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (LazyOptional<T>) itemCap : super.getCapability(cap, side);
    }

    public BaseItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        target = NBTX.getPos(nbt, "TargetPos");
        floatProgress = nbt.getDouble("FloatProgress");
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.put("Inventory", inventory.serializeNBT());
        if (this.target != null) {
            NBTX.putPos(nbt, "TargetPos", target);
            nbt.putDouble("FloatProgress", floatProgress);
        }
        return super.write(nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!world.isRemote) {
            nbt.put("Inventory", inventory.serializeNBT());
            if (this.target != null) {
                NBTX.putPos(nbt, "TargetPos", target);
                nbt.putDouble("FloatProgress", floatProgress);
            }
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        //noinspection ConstantConditions
        if (world.isRemote) {
            inventory.deserializeNBT(nbt.getCompound("Inventory"));
            target = NBTX.getPos(nbt, "TargetPos");
            floatProgress = nbt.getDouble("FloatProgress");
        }
    }

    @Nullable
    public BlockPos getTarget() {
        return target;
    }

    public double getFloatProgress() {
        return floatProgress;
    }

    public void setTarget(@Nullable BlockPos target, double floatProgress, boolean sync) {
        this.target = target;
        if (target != null) {
            this.floatProgress = MathHelper.clamp(floatProgress, 0, 1);
        } else {
            this.floatProgress = 0;
        }
        markDirty();
        if (sync) {
            markDispatchable();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB aabb = super.getRenderBoundingBox();
        if (target != null) {
            // If the rune is floating to a target, we need to expand the render
            // aabb to include that target or runes will sometimes not render.
            return aabb.expand(target.getX() - pos.getX(), 0, target.getZ() - pos.getZ()).grow(1);
        } else {
            return aabb;
        }
    }
}
