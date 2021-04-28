package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.NoReg;
import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import mythicbotany.loot.AlfsteelDisposeModifier;
import mythicbotany.loot.FimbultyrModifier;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

@RegisterClass
public class ModMisc {

    @NoReg public static final EnchantmentType MJOELLNIR_ENCHANTS = EnchantmentType.create(MythicBotany.getInstance().modid + "_mjoellnir", i -> i == ModBlocks.mjoellnir.asItem());
    
    public static final Enchantment hammerMobility = new Enchantment(Enchantment.Rarity.UNCOMMON, MJOELLNIR_ENCHANTS, new EquipmentSlotType[]{ EquipmentSlotType.MAINHAND }) {
        @Override
        public int getMaxLevel() {
            return 5;
        }
    };
    
    public static void register() {
        MythicBotany.getInstance().register("dispose", AlfsteelDisposeModifier.Serializer.INSTANCE);
        MythicBotany.getInstance().register("fimbultyr", FimbultyrModifier.Serializer.INSTANCE);
    }
}
