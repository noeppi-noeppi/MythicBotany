package mythicbotany.mimir;

import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import io.github.noeppi_noeppi.libx.capability.ItemCapabilities;
import io.github.noeppi_noeppi.libx.inventory.BaseItemStackHandler;
import mythicbotany.ModItems;
import mythicbotany.base.BlockEntityMana;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileYggdrasilBranch extends BlockEntityMana implements TickableBlock {

    private final BaseItemStackHandler inventory = BaseItemStackHandler.builder(1)
            .contentsChanged(() -> {
                this.setChanged();
                this.setDispatchable();
            })
            .validator(stack -> stack.getItem() == ModItems.gjallarHornEmpty, 0)
            .build();
    
    private final LazyOptional<IItemHandlerModifiable> itemCap = ItemCapabilities.create(() -> inventory).cast();

    private int progress = 0;
    
    public TileYggdrasilBranch(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 10000, true, false);
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
                if (!level.isClientSide) {
                    mana -= 10;
                    progress += 1;
                    if (progress >= 600) {
                        inventory.setStackInSlot(0, new ItemStack(ModItems.gjallarHornFull));
                        progress = 0;
                    }
                    setChanged();
                    setDispatchable();
                } else if (level.getGameTime() % 4 == 0) {
                    double xf = 0.5;
                    double zf = 0.35;
                    Direction dir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
                    if (dir.getAxis() == Direction.Axis.X) {
                        double tmp = xf;
                        xf = zf;
                        zf = tmp;
                    }
                    if (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        xf = 1 - xf;
                        zf = 1 - zf;
                    }
                    level.addParticle(ParticleTypes.DRIPPING_WATER, worldPosition.getX() + xf, worldPosition.getY() + 0.76, worldPosition.getZ() + zf, 0, -0.2, 0);
                }
            }
        } else if (progress != 0) {
            progress = 0;
            setChanged();
            setDispatchable();
        }
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        progress = nbt.getInt("Progress");
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("Inventory", inventory.serializeNBT());
        nbt.putInt("Progress", progress);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            nbt.put("Inventory", inventory.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        //noinspection ConstantConditions
        if (level.isClientSide) {
           inventory.deserializeNBT(nbt.getCompound("Inventory"));
        }
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }
}
