package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.BlockTagProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagProvider extends BlockTagProviderBase {

	public BlockTagProvider(ModX mod, DataGenerator generatorIn, ExistingFileHelper fileHelper) {
		super(mod, generatorIn, fileHelper);
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
	}
}
