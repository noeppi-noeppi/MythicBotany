package mythicbotany.mjoellnir;

import com.google.common.collect.ImmutableList;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.rune.SpecialRuneOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MjoellnirRuneOutput extends SpecialRuneOutput {

    public static final MjoellnirRuneOutput INSTANCE = new MjoellnirRuneOutput();
    
    private MjoellnirRuneOutput() {
        super(new ResourceLocation(MythicBotany.getInstance().modid, "mjoellnir"));
    }

    @Override
    public void apply(World world, BlockPos center, List<ItemStack> consumedStacks) {
        BlockMjoellnir.putInWorld(new ItemStack(ModBlocks.mjoellnir), world, center, false);
    }

    @Override
    public List<ItemStack> getJeiOutputItems() {
        return ImmutableList.of(new ItemStack(ModBlocks.mjoellnir));
    }
}
