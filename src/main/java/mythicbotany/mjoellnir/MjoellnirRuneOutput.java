package mythicbotany.mjoellnir;

import com.google.common.collect.ImmutableList;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.rune.SpecialRuneOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class MjoellnirRuneOutput extends SpecialRuneOutput {

    public static final MjoellnirRuneOutput INSTANCE = new MjoellnirRuneOutput();
    
    private MjoellnirRuneOutput() {
        super(MythicBotany.getInstance().resource("mjoellnir"));
    }

    @Override
    public void apply(Level level, BlockPos center, List<ItemStack> consumedStacks) {
        BlockMjoellnir.putInWorld(new ItemStack(ModBlocks.mjoellnir), level, center, false);
    }

    @Override
    public List<ItemStack> getJeiOutputItems() {
        return ImmutableList.of(new ItemStack(ModBlocks.mjoellnir));
    }
}
