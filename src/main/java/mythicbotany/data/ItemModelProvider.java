package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.ItemModelProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends ItemModelProviderBase {

	public ItemModelProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
		super(mod, generator, fileHelper);
	}

	@Override
	protected void setup() {
		handheld(ModItems.alfsteelSword);
		manualModel(ModItems.alfsteelPick);
		manualModel(ModItems.alfsteelAxe);
		manualModel(ModItems.fadedNetherStar);
		manualModel(ModItems.dreamwoodWand);
		manualModel(ModBlocks.alfsteelPylon.asItem());
	}

	@Override
	protected void defaultBlock(ResourceLocation id, BlockItem item) {
		if (item.getBlock() instanceof BlockFunctionalFlower<?>) {
			this.withExistingParent(id.getPath(), GENERATED).texture("layer0", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));
		} else {
			super.defaultBlock(id, item);
		}
	}
}
