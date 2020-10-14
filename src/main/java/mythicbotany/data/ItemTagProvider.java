package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.BlockTagProviderBase;
import io.github.noeppi_noeppi.libx.data.provider.ItemTagProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.item.material.ItemRune;
import vazkii.botania.common.lib.ModTags;

public class ItemTagProvider extends ItemTagProviderBase {

	public ItemTagProvider(ModX mod, DataGenerator generatorIn, ExistingFileHelper fileHelper, BlockTagProviderBase blockTags) {
		super(mod, generatorIn, fileHelper, blockTags);
	}

	@Override
	protected void registerTags() {

		//noinspection deprecation
		ForgeRegistries.ITEMS.getValues().stream()
				.filter(i -> MythicBotany.getInstance().modid.equals(Registry.ITEM.getKey(i).getNamespace()))
				.filter(i -> !(i instanceof BlockItem))
				.forEach(this::addDefaultItemTag);

		//noinspection deprecation
		ForgeRegistries.ITEMS.getValues().stream()
				.filter(i -> MythicBotany.getInstance().modid.equals(Registry.ITEM.getKey(i).getNamespace()))
				.filter(i -> i instanceof BlockItem)
				.map(i -> ((BlockItem) i).getBlock())
				.forEach(this::addDefaultBlockItemTag);
	}

	public void addDefaultItemTag(Item item) {
		if (item instanceof ItemRune) {
			this.getOrCreateBuilder(ModTags.Items.RUNES).add(item);
		}
	}

	public void addDefaultBlockItemTag(Block block) {
		if (block instanceof BlockFunctionalFlower<?>) {
			this.getOrCreateBuilder(ModTags.Items.SPECIAL_FLOWERS).add(block.asItem());
			if (((BlockFunctionalFlower<?>) block).isGenerating) {
				this.getOrCreateBuilder(ModTags.Items.GENERATING_SPECIAL_FLOWERS).add(block.asItem());
			} else {
				this.getOrCreateBuilder(ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS).add(block.asItem());
			}
		} else if (block instanceof BlockFloatingFunctionalFlower<?>) {
			this.getOrCreateBuilder(ModTags.Items.FLOATING_FLOWERS).add(block.asItem());
			this.getOrCreateBuilder(ModTags.Items.SPECIAL_FLOATING_FLOWERS).add(block.asItem());
		}
	}
}
