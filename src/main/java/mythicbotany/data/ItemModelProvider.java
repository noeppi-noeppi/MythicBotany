package mythicbotany.data;

import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.ItemModelProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.ExistingFileHelper;

@Datagen
public class ItemModelProvider extends ItemModelProviderBase {

	public ItemModelProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
		super(mod, generator, fileHelper);
	}

	@Override
	protected void setup() {
        this.handheld(ModItems.alfsteelSword);
        this.manualModel(ModItems.alfsteelPick);
        this.manualModel(ModItems.alfsteelAxe);
        this.manualModel(ModItems.fadedNetherStar);
        this.manualModel(ModBlocks.alfsteelPylon.asItem());
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
