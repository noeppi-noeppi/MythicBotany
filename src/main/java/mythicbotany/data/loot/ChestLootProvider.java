package mythicbotany.data.loot;

import mythicbotany.register.ModItems;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.ChestLootProviderBase;
import org.moddingx.libx.datagen.provider.loot.entry.LootFactory;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.List;

public class ChestLootProvider extends ChestLootProviderBase {
    
    public ChestLootProvider(DatagenContext ctx) {
        super(ctx);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void setup() {
        LootPool.Builder pool1 = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(this.combine(
                this.stack(ModItems.cursedAndwariRing)
        ).build("andwari_cave"));
        LootPool.Builder pool2 = LootPool.lootPool().setRolls(UniformGenerator.between(8, 13))
                .add(this.stack(Items.ENCHANTED_GOLDEN_APPLE).build("andwari_cave").setWeight(1))
                .add(this.stack(Items.GOLDEN_APPLE).with(this.count(1, 3)).build("andwari_cave").setWeight(4))
                .add(this.stack(Items.GOLD_INGOT).with(this.count(3, 8)).build("andwari_cave").setWeight(5))
                .add(this.stack(Items.GOLD_NUGGET).with(this.count(4, 20)).build("andwari_cave").setWeight(5));
        this.customLootTable("andwari_cave", LootTable.lootTable().withPool(pool1).withPool(pool2));
        
        this.drops("elven_house",
                this.first(
                        this.stack(BotaniaItems.blackerLotus).with(this.random(0.02f)),
                        this.stack(BotaniaItems.blackLotus)
                ).with(this.random(0.5f)),
                this.stack(BotaniaItems.elementium).with(this.count(1, 6)).with(this.random(0.7f)),
                this.first(
                        this.stack(BotaniaItems.dragonstone).with(this.count(1, 2)).with(this.random(0.5f)),
                        this.stack(BotaniaItems.pixieDust).with(this.count(1, 2))
                ).with(this.random(0.4f)),
                this.stack(ModItems.dreamCherry).with(this.count(1, 3)).with(this.random(0.4f)),
                this.stack(Items.GOLD_NUGGET).with(this.count(1, 5)),
                this.randomPetal(1, 4).with(this.random(0.8f)),
                this.randomPetal(1, 4).with(this.random(0.8f)),
                this.randomPetal(1, 4).with(this.random(0.8f))
        );
    }
    
    @SuppressWarnings("unchecked")
    private LootFactory<String> randomPetal(int min, int max) {
        List<LootFactory<String>> factories = new ArrayList<>();
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < colors.length; i++) {
            LootFactory<String> factory = this.stack(BotaniaItems.getPetal(colors[i])).with(this.count(min, max));
            if (i != colors.length - 1) {
                factory = factory.with(this.random(1f / (colors.length - 1)));
            }
            factories.add(factory);
        }
        return this.first(factories);
    }
}
