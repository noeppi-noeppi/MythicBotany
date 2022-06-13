package mythicbotany.loot;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.MythicPlayerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;
import java.util.List;

public class FimbultyrModifier extends LootModifier {

    public static final ResourceLocation HARD_LOOT_TABLE = new ResourceLocation("botania", "gaia_guardian_2");
    
    private FimbultyrModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity self = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        Entity dead = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (self instanceof Player && dead instanceof EntityDoppleganger && dead.getType() == ModEntities.DOPPLEGANGER) {
            if (HARD_LOOT_TABLE.equals(((Mob) dead).getLootTable()) && MythicPlayerData.getData((Player) self).getBoolean("MimirKnowledge")) {
                generatedLoot.add(new ItemStack(ModItems.fimbultyrTablet));
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FimbultyrModifier> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        public FimbultyrModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            return new FimbultyrModifier(conditions);
        }

        public JsonObject write(FimbultyrModifier modifier) {
            return this.makeConditions(modifier.conditions);
        }
    }
}
