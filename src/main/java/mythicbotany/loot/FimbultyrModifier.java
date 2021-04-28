package mythicbotany.loot;

import com.google.gson.JsonObject;
import mythicbotany.ModItems;
import mythicbotany.MythicPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;
import java.util.List;

public class FimbultyrModifier extends LootModifier {

    public static final ResourceLocation HARD_LOOT_TABLE = new ResourceLocation("botania", "gaia_guardian_2");
    
    private FimbultyrModifier(ILootCondition[] conditions) {
        super(conditions);
    }

    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity self = context.get(LootParameters.KILLER_ENTITY);
        Entity dead = context.get(LootParameters.THIS_ENTITY);
        if (self instanceof PlayerEntity && dead instanceof EntityDoppleganger && dead.getType() == ModEntities.DOPPLEGANGER) {
            if (HARD_LOOT_TABLE.equals(((MobEntity) dead).getLootTableResourceLocation()) && MythicPlayerData.getData((PlayerEntity) self).getBoolean("MimirKnowledge")) {
                generatedLoot.add(new ItemStack(ModItems.fimbultyrTablet));
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FimbultyrModifier> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {

        }

        public FimbultyrModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new FimbultyrModifier(conditions);
        }

        public JsonObject write(FimbultyrModifier modifier) {
            return this.makeConditions(modifier.conditions);
        }
    }
}
