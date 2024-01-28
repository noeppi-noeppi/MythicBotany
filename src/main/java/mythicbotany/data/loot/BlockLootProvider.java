package mythicbotany.data.loot;

import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.BlockLootProviderBase;
import vazkii.botania.common.item.BotaniaItems;

public class BlockLootProvider extends BlockLootProviderBase {

    public BlockLootProvider(DatagenContext ctx) {
        super(ctx);
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
