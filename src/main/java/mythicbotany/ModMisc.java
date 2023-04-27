package mythicbotany;

import org.moddingx.libx.annotation.registration.Reg.Exclude;
import org.moddingx.libx.annotation.registration.RegisterClass;
import mythicbotany.loot.AlfsteelDisposeModifier;
import mythicbotany.loot.FimbultyrModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@RegisterClass
public class ModMisc {

    @Exclude public static final EnchantmentCategory MJOELLNIR_ENCHANTS = EnchantmentCategory.create(MythicBotany.getInstance().modid + "_mjoellnir", i -> i == ModBlocks.mjoellnir.asItem());
    
    public static final Enchantment hammerMobility = new Enchantment(Enchantment.Rarity.UNCOMMON, MJOELLNIR_ENCHANTS, new EquipmentSlot[]{ EquipmentSlot.MAINHAND }) {
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
