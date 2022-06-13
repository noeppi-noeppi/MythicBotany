package mythicbotany.rune;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class SpecialRuneOutput {
    
    public final ResourceLocation id;

    protected SpecialRuneOutput(ResourceLocation id) {
        this.id = id;
    }
    
    public abstract void apply(Level world, BlockPos center, List<ItemStack> consumedStacks);

    public List<ItemStack> getJeiOutputItems() {
        return ImmutableList.of();
    }
}
