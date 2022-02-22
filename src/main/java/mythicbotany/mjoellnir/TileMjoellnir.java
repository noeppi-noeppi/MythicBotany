package mythicbotany.mjoellnir;

import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import mythicbotany.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nonnull;

public class TileMjoellnir extends BlockEntityBase {

    private ItemStack stack;
    
    public TileMjoellnir(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        stack = new ItemStack(ModBlocks.mjoellnir);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        setChanged();
        setDispatchable();
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("hammer", Tag.TAG_COMPOUND)) {
            stack = ItemStack.of(nbt.getCompound("hammer"));
        } else {
            stack = new ItemStack(ModBlocks.mjoellnir);
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("hammer", stack.save(new CompoundTag()));
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (level != null && !level.isClientSide) {
            nbt.put("hammer", stack.save(new CompoundTag()));
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag nbt) {
        super.handleUpdateTag(nbt);
        if (nbt.contains("hammer", Tag.TAG_COMPOUND)) {
            stack = ItemStack.of(nbt.getCompound("hammer"));
        } else {
            stack = new ItemStack(ModBlocks.mjoellnir);
        }
    }
}
