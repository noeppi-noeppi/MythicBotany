package mythicbotany.misc;

import mythicbotany.MythicBotany;
import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.WitherAconite;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.base.ItemBase;

import java.util.stream.Stream;

public class ItemFadedNetherStar extends ItemBase {

    public ItemFadedNetherStar() {
        super(MythicBotany.getInstance(), new Properties().stacksTo(1).durability(WitherAconite.DEFAULT_MANA_PER_STAR));
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return MythicConfig.flowers.witherAconiteMana;
    }

    @Override
    public Stream<ItemStack> makeCreativeTabStacks() {
        return Stream.empty();
    }
}
