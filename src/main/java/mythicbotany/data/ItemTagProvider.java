package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.BlockTagProviderBase;
import io.github.noeppi_noeppi.libx.data.provider.ItemTagProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlockTags;
import mythicbotany.ModBlocks;
import mythicbotany.ModItemTags;
import mythicbotany.ModItems;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vazkii.botania.common.item.material.ItemRune;
import vazkii.botania.common.lib.ModTags;

public class ItemTagProvider extends ItemTagProviderBase {

	public ItemTagProvider(ModX mod, DataGenerator generatorIn, ExistingFileHelper fileHelper, BlockTagProviderBase blockTags) {
		super(mod, generatorIn, fileHelper, blockTags);
	}

	@Override
	protected void setup() {
		// alfheim leaves should only be in the block tag, not the item tag. This is intended
		this.getOrCreateBuilder(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.auraRingGreatest);
		this.getOrCreateBuilder(ModTags.Items.TERRA_PICK_BLACKLIST).add(ModItems.alfsteelHelmet);
		this.getOrCreateBuilder(ModItemTags.RITUAL_RUNES).addTag(ModTags.Items.RUNES);
		this.getOrCreateBuilder(ModItemTags.RITUAL_RUNES).add(ModItems.fimbultyrTablet);
		this.getOrCreateBuilder(ItemTags.GOLD_ORES).add(ModBlocks.goldOre.asItem());
		this.copy(ModBlockTags.ALFHEIM_ORES, ModItemTags.ALFHEIM_ORES);
		this.getOrCreateBuilder(Tags.Items.ORES).addTag(ModItemTags.ALFHEIM_ORES);
	}
	
	@Override
	public void defaultItemTags(Item item) {
		if (item instanceof ItemRune) {
			this.getOrCreateBuilder(ModTags.Items.RUNES).add(item);
		}
	}
	
	@Override
	public void defaultBlockItemTags(Block block) {
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
