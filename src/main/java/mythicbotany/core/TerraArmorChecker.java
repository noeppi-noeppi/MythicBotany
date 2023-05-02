package mythicbotany.core;

import mythicbotany.register.ModItems;
import net.minecraft.world.entity.player.Player;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;

public class TerraArmorChecker {
    
    public static boolean hasTerraHelmet(Player player) {
        return ((TerrasteelHelmItem) ModItems.alfsteelHelmet).hasArmorSet(player);
    }
}
