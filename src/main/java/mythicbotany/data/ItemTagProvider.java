/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

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
		//this.func_240521_a_(BlockTags.RAILS, ItemTags.RAILS);
		//this.func_240522_a_(ModTags.Items.SHEARS).func_240534_a_(ModItems.elementiumShears, ModItems.manasteelShears);
	}
}
