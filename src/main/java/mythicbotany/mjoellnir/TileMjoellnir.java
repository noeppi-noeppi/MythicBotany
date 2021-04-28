package mythicbotany.mjoellnir;

import io.github.noeppi_noeppi.libx.mod.registration.TileEntityBase;
import mythicbotany.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class TileMjoellnir extends TileEntityBase {

    private ItemStack stack;
    
    public TileMjoellnir(TileEntityType<?> teType) {
        super(teType);
        stack = new ItemStack(ModBlocks.mjoellnir);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        markDirty();
        markDispatchable();
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        if (nbt.contains("hammer", Constants.NBT.TAG_COMPOUND)) {
            stack = ItemStack.read(nbt.getCompound("hammer"));
        } else {
            stack = new ItemStack(ModBlocks.mjoellnir);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.put("hammer", stack.write(new CompoundNBT()));
        return super.write(nbt);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        if (world != null && !world.isRemote) {
            nbt.put("hammer", stack.write(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        super.handleUpdateTag(state, nbt);
        if (nbt.contains("hammer", Constants.NBT.TAG_COMPOUND)) {
            stack = ItemStack.read(nbt.getCompound("hammer"));
        } else {
            stack = new ItemStack(ModBlocks.mjoellnir);
        }
    }
}
