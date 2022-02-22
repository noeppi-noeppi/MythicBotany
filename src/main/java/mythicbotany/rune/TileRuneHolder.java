package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import io.github.noeppi_noeppi.libx.capability.ItemCapabilities;
import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.ModItemTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileRuneHolder extends BlockEntityBase {

    private final BaseItemStackHandler inventory = BaseItemStackHandler.builder(1)
            .contentsChanged(() -> {
                this.setChanged();
                this.setDispatchable();
            })
            .validator(stack -> ModItemTags.RITUAL_RUNES.contains(stack.getItem()), 1)
            .defaultSlotLimit(1)
            .build();
            
    private final LazyOptional<IItemHandlerModifiable> itemCap = ItemCapabilities.create(() -> this.inventory).cast();

    @Nullable
    private BlockPos target;
    private double floatProgress;
    
    public TileRuneHolder(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        target = NBTX.getPos(nbt, "TargetPos");
        floatProgress = nbt.getDouble("FloatProgress");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", inventory.serializeNBT());
        if (this.target != null) {
            NBTX.putPos(nbt, "TargetPos", target);
            nbt.putDouble("FloatProgress", floatProgress);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            nbt.put("Inventory", inventory.serializeNBT());
            if (this.target != null) {
                NBTX.putPos(nbt, "TargetPos", target);
                nbt.putDouble("FloatProgress", floatProgress);
            }
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        //noinspection ConstantConditions
        if (level.isClientSide) {
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
            this.floatProgress = Mth.clamp(floatProgress, 0, 1);
        } else {
            this.floatProgress = 0;
        }
        setChanged();
        if (sync) {
            setDispatchable();
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        AABB aabb = super.getRenderBoundingBox();
        if (target != null) {
            // If the rune is floating to a target, we need to expand the render
            // aabb to include that target or runes will sometimes not render.
            return aabb.expandTowards(target.getX() - worldPosition.getX(), 0, target.getZ() - worldPosition.getZ()).inflate(1);
        } else {
            return aabb;
        }
    }
}
