package mythicbotany.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mythicbotany.MythicPlayerData;
import mythicbotany.register.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.GaiaGuardianEntity;

import javax.annotation.Nonnull;

public class FimbultyrModifier extends LootModifier {
    
    public static final Codec<FimbultyrModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions)
    ).apply(instance, FimbultyrModifier::new));
    
    public static final ResourceLocation HARD_LOOT_TABLE = new ResourceLocation("botania", "gaia_guardian_2");
    
    public FimbultyrModifier(LootItemCondition... conditions) {
        super(conditions);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity self = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        Entity dead = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (self instanceof Player && dead instanceof GaiaGuardianEntity && dead.getType() == BotaniaEntities.DOPPLEGANGER) {
            if (HARD_LOOT_TABLE.equals(((Mob) dead).getLootTable()) && MythicPlayerData.getData((Player) self).getBoolean("MimirKnowledge")) {
                generatedLoot.add(new ItemStack(ModItems.fimbultyrTablet));
            }
        }
        return generatedLoot;
    }
}
