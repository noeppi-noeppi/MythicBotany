package mythicbotany.alftools;

import mythicbotany.MythicCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;

import javax.annotation.Nullable;

public class GreatestManaRing extends ItemManaRing {

    private static final int MAX_MANA = 4000000;

    public GreatestManaRing(Properties props) {
        super(props);
    }

    public int getMaxMana(ItemStack stack) {
        return MAX_MANA * stack.getCount();
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new MythicCap<>(super.initCapabilities(stack, nbt), BotaniaForgeCapabilities.MANA_ITEM, () -> new ManaItem(stack) {

            @Override
            public int getMaxMana() {
                return MAX_MANA * stack.getCount();
            }
        });
    }
}
