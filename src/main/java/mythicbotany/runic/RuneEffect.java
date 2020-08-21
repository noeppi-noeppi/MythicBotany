package mythicbotany.runic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Set;

public interface RuneEffect {

    void perform(World world, PlayerEntity player, ItemStack stack, Affinities affinities, Set<Item> runes);
}
