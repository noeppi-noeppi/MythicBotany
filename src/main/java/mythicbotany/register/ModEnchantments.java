package mythicbotany.register;

import mythicbotany.MythicBotany;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.moddingx.libx.annotation.registration.Reg;
import org.moddingx.libx.annotation.registration.RegisterClass;

@RegisterClass(registry = "ENCHANTMENT")
public class ModEnchantments {

    @Reg.Exclude
    public static final EnchantmentCategory MJOELLNIR_ENCHANTS = EnchantmentCategory.create(MythicBotany.getInstance().modid + "_mjoellnir", i -> i == ModBlocks.mjoellnir.asItem());
    
    public static final Enchantment hammerMobility = new Enchantment(Enchantment.Rarity.UNCOMMON, MJOELLNIR_ENCHANTS, new EquipmentSlot[]{ EquipmentSlot.MAINHAND }) {
        
        @Override
        public int getMaxLevel() {
            return 5;
        }
    };
}
