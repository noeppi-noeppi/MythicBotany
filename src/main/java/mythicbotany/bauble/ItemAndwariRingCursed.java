package mythicbotany.bauble;

import mythicbotany.MythicBotany;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class ItemAndwariRingCursed extends ItemBauble {

    public ItemAndwariRingCursed(Properties props) {
        super(props.group(MythicBotany.getInstance().tab));
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        super.onWornTick(stack, entity);
        if (entity instanceof PlayerEntity) {
            ManaItemHandler.instance().requestMana(stack, (PlayerEntity) entity, 500, true);
        }
        if (entity.ticksExisted % 20 == 0) {
            entity.addPotionEffect(new EffectInstance(Effects.POISON, 30, 0));
        }
    }
}
