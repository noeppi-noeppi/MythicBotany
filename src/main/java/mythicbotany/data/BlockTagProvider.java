package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.BlockTagProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlockTags;
import mythicbotany.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagProvider extends BlockTagProviderBase {

	public BlockTagProvider(ModX mod, DataGenerator generatorIn, ExistingFileHelper fileHelper) {
		super(mod, generatorIn, fileHelper);
	}

	@Override
	protected void setup() {
		getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
		getOrCreateBuilder(ModBlockTags.ALFHEIM_LOGS).add(vazkii.botania.common.block.ModBlocks.dreamwood);
		getOrCreateBuilder(ModBlockTags.ALFHEIM_LEAVES).add(ModBlocks.dreamwoodLeaves);
		getOrCreateBuilder(ModBlockTags.BASE_STONE_ALFHEIM).add(vazkii.botania.common.block.ModBlocks.livingrock);
		getOrCreateBuilder(BlockTags.GOLD_ORES).add(ModBlocks.goldOre);
	}
}
