package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.capability.ItemCapabilities;
import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import mythicbotany.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
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
            .validator(stack -> stack.is(ModItemTags.RITUAL_RUNES), 1)
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
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (LazyOptional<T>) this.itemCap : super.getCapability(cap, side);
    }

    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
        this.target = NbtUtils.readBlockPos(nbt.getCompound("TargetPos"));
        this.floatProgress = nbt.getDouble("FloatProgress");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", this.inventory.serializeNBT());
        if (this.target != null) {
            nbt.put("TargetPos", NbtUtils.writeBlockPos(this.target));
            nbt.putDouble("FloatProgress", this.floatProgress);
        }
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            nbt.put("Inventory", this.inventory.serializeNBT());
            if (this.target != null) {
                nbt.put("TargetPos", NbtUtils.writeBlockPos(this.target));
                nbt.putDouble("FloatProgress", this.floatProgress);
            }
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        //noinspection ConstantConditions
        if (this.level.isClientSide) {
            this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
            this.target = NbtUtils.readBlockPos(nbt.getCompound("TargetPos"));
            this.floatProgress = nbt.getDouble("FloatProgress");
        }
    }

    @Nullable
    public BlockPos getTarget() {
        return this.target;
    }

    public double getFloatProgress() {
        return this.floatProgress;
    }

    public void setTarget(@Nullable BlockPos target, double floatProgress, boolean sync) {
        this.target = target;
        if (target != null) {
            this.floatProgress = Mth.clamp(floatProgress, 0, 1);
        } else {
            this.floatProgress = 0;
        }
        this.setChanged();
        if (sync) {
            this.setDispatchable();
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        AABB aabb = super.getRenderBoundingBox();
        if (this.target != null) {
            // If the rune is floating to a target, we need to expand the render
            // aabb to include that target or runes will sometimes not render.
            return aabb.expandTowards(this.target.getX() - this.worldPosition.getX(), 0, this.target.getZ() - this.worldPosition.getZ()).inflate(1);
        } else {
            return aabb;
        }
    }
}
