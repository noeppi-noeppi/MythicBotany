package mythicbotany.data;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.BlockLootProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;

@Datagen
public class BlockLootProvider extends BlockLootProviderBase {

	public BlockLootProvider(ModX mod, DataGenerator generator) {
		super(mod, generator);
	}

	@Override
	protected void setup() {
		drops(ModBlocks.dragonstoneOre, true, stack(vazkii.botania.common.item.ModItems.dragonstone).with(fortuneOres()));
		drops(ModBlocks.dreamwoodLeaves, first(
				item().with(or(silkCondition(), matchTool(Tags.Items.SHEARS))),
				combine(
						stack(vazkii.botania.common.item.ModItems.dreamwoodTwig).with(randomFortune(0.06f)).with(count(1, 2)),
						stack(ModItems.dreamCherry).with(randomFortune(0.015f))
				)
		));
	}
}
