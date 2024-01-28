package mythicbotany.data.loot;

import mythicbotany.register.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.loot.ChestLootProviderBase;

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
    }
}
