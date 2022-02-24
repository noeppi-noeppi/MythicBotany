package mythicbotany.core;

import mythicbotany.ModItems;
import net.minecraft.world.entity.player.Player;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

public class TerraArmorChecker {
    
    public static boolean hasTerraHelmet(Player player) {
        return ((ItemTerrasteelHelm) ModItems.alfsteelHelmet).hasArmorSet(player);
    }
}
