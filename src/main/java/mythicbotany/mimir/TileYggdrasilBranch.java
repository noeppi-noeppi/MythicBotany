package mythicbotany.mimir;

import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import io.github.noeppi_noeppi.libx.inventory.ItemStackHandlerWrapper;
import mythicbotany.ModItems;
import mythicbotany.base.TileEntityMana;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileYggdrasilBranch extends TileEntityMana implements ITickableTileEntity {

    private final BaseItemStackHandler inventory = new BaseItemStackHandler(1, slot -> {
        this.markDirty();
        this.markDispatchable();
    }, (slot, stack) -> stack.getItem() == ModItems.gjallarHornEmpty);
    
    private final LazyOptional<IItemHandlerModifiable> itemCap = ItemStackHandlerWrapper.createLazy(() -> inventory);

    private int progress = 0;
    
    public TileYggdrasilBranch(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 10000, true, false);
    }

    @Override
    protected boolean canReceive() {
        return true;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //noinspection unchecked
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (LazyOptional<T>) itemCap : super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if (inventory.getStackInSlot(0).getItem() == ModItems.gjallarHornEmpty && inventory.getStackInSlot(0).getCount() == 1) {
            if (mana >= 20) {
                //noinspection ConstantConditions
                if (!world.isRemote) {
                    mana -= 10;
                    progress += 1;
                    if (progress >= 600) {
                        inventory.setStackInSlot(0, new ItemStack(ModItems.gjallarHornFull));
                        progress = 0;
                    }
                    markDirty();
                    markDispatchable();
                } else if (world.getGameTime() % 4 == 0) {
                    double xf = 0.5;
                    double zf = 0.35;
                    Direction dir = getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
                    if (dir.getAxis() == Direction.Axis.X) {
                        double tmp = xf;
                        xf = zf;
                        zf = tmp;
                    }
                    if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        xf = 1 - xf;
                        zf = 1 - zf;
                    }
                    world.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + xf, pos.getY() + 0.76, pos.getZ() + zf, 0, -0.2, 0);
                }
            }
        } else if (progress != 0) {
            progress = 0;
            markDirty();
            markDispatchable();
        }
    }

    @Override
    public void read(@Nonnull BlockState stateIn, @Nonnull CompoundNBT nbt) {
        super.read(stateIn, nbt);
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        progress = nbt.getInt("Progress");
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.put("Inventory", inventory.serializeNBT());
        nbt.putInt("Progress", progress);
        return super.write(nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!world.isRemote) {
            nbt.put("Inventory", inventory.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        //noinspection ConstantConditions
        if (world.isRemote) {
           inventory.deserializeNBT(nbt.getCompound("Inventory"));
        }
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }
}
