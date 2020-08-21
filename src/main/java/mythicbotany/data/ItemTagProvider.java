/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data;

import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.BlockFloatingFunctionalFlower;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import vazkii.botania.common.item.material.ItemRune;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

public class ItemTagProvider extends ItemTagsProvider {
	public ItemTagProvider(DataGenerator generatorIn, BlockTagProvider blockTagProvider) {
		super(generatorIn, blockTagProvider);
	}

	@Nonnull
	@Override
	public String getName() {
		return "MythicBotany item tags";
	}

	@Override
	protected void registerTags() {

		//noinspection deprecation
		Registry.ITEM.stream()
				.filter(i -> MythicBotany.MODID.equals(Registry.ITEM.getKey(i).getNamespace()))
				.filter(i -> !(i instanceof BlockItem))
				.forEach(this::addDefaultItemTag);

		//noinspection deprecation
		Registry.ITEM.stream()
				.filter(i -> MythicBotany.MODID.equals(Registry.ITEM.getKey(i).getNamespace()))
				.filter(i -> i instanceof BlockItem)
				.map(i -> ((BlockItem) i).getBlock())
				.forEach(this::addDefaultBlockItemTag);

		//this.func_240522_a_(ModTags.Items.SHEARS).func_240534_a_(ModItems.elementiumShears, ModItems.manasteelShears);
	}

	public void addDefaultItemTag(Item item) {
		if (item instanceof ItemRune) {
			this.func_240522_a_(ModTags.Items.RUNES).func_240534_a_(item);
		}
	}

	public void addDefaultBlockItemTag(Block block) {
		if (block instanceof BlockFunctionalFlower<?>) {
			this.func_240522_a_(ModTags.Items.SPECIAL_FLOWERS).func_240534_a_(block.asItem());
			if (((BlockFunctionalFlower<?>) block).isGenerating) {
				this.func_240522_a_(ModTags.Items.GENERATING_SPECIAL_FLOWERS).func_240534_a_(block.asItem());
			} else {
				this.func_240522_a_(ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS).func_240534_a_(block.asItem());
			}
		} else if (block instanceof BlockFloatingFunctionalFlower<?>) {
			this.func_240522_a_(ModTags.Items.FLOATING_FLOWERS).func_240534_a_(block.asItem());
			this.func_240522_a_(ModTags.Items.SPECIAL_FLOATING_FLOWERS).func_240534_a_(block.asItem());
		}
	}
}
