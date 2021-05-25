package mythicbotany.mjoellnir;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

public enum MjoellnirHoldRequirement {

    NOTHING,
    EFFECT,
    HEARTS;
    
    public boolean test(PlayerEntity player) {
        switch (this) {
            case NOTHING: return true;
            case EFFECT: return player.getActivePotionEffect(Effects.ABSORPTION) != null;
            case HEARTS: return player.getAbsorptionAmount() > 0;
        }
        return false;
    }
}
