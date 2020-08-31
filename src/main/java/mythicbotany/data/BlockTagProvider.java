/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package mythicbotany.data;

import mythicbotany.ModBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;

import javax.annotation.Nonnull;

public class BlockTagProvider extends BlockTagsProvider {
	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Nonnull
	@Override
	public String getName() {
		return "MythicBotany block tags";
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.alfsteelBlock);
	}
}
