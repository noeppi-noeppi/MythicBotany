package mythicbotany.data.loot;

import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import vazkii.botania.common.item.BotaniaItems;

@Datagen
public class BlockLootProvider extends BlockLootProviderBase {

    public BlockLootProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setup() {
        this.drops(ModBlocks.elementiumOre, true, this.stack(ModItems.rawElementium).with(this.fortuneOres()));
        this.drops(ModBlocks.goldOre, true, this.stack(Items.RAW_GOLD).with(this.fortuneOres()));
        this.drops(ModBlocks.dragonstoneOre, true, this.stack(BotaniaItems.dragonstone).with(this.fortuneOres()));
        this.drops(ModBlocks.dreamwoodLeaves, this.first(
                this.element().with(this.or(this.silkCondition(), this.matchTool(Tags.Items.SHEARS))),
                this.combine(
                        this.stack(BotaniaItems.dreamwoodTwig).with(this.randomFortune(0.06f)).with(this.count(1, 2)),
                        this.stack(ModItems.dreamCherry).with(this.randomFortune(0.015f))
                )
        ));
    }
}
