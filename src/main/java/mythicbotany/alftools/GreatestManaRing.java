package mythicbotany.alftools;

import mythicbotany.MythicCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.moddingx.libx.creativetab.CreativeTabItemProvider;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.common.item.equipment.bauble.BandOfManaItem;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class GreatestManaRing extends BandOfManaItem implements CreativeTabItemProvider {

    private static final int MAX_MANA = 4000000;

    public GreatestManaRing(Properties props) {
        super(props);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new MythicCap<>(super.initCapabilities(stack, nbt), BotaniaForgeCapabilities.MANA_ITEM, () -> new ManaItemImpl(stack) {

            @Override
            public int getMaxMana() {
                return MAX_MANA * stack.getCount();
            }
        });
    }

    @Override
    public Stream<ItemStack> makeCreativeTabStacks() {
        ItemStack full = new ItemStack(this);
        setMana(full, MAX_MANA);
        return Stream.of(new ItemStack(this), full);
    }
}
