package mythicbotany.rune;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class SpecialRuneOutput {
    
    public final ResourceLocation id;

    protected SpecialRuneOutput(ResourceLocation id) {
        this.id = id;
    }
    
    public abstract void apply(World world, BlockPos center, List<ItemStack> consumedStacks);

    public List<ItemStack> getJeiOutputItems() {
        return ImmutableList.of();
    }
}
