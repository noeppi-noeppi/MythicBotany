package mythicbotany;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public class MythicPlayerData {
    
    public static CompoundTag getData(Player player) {
        if (player.getPersistentData().contains("MythicBotanyPlayerInfo", Tag.TAG_COMPOUND)) {
            return player.getPersistentData().getCompound("MythicBotanyPlayerInfo");
        } else {
            CompoundTag nbt = new CompoundTag();
            player.getPersistentData().put("MythicBotanyPlayerInfo", nbt);
            return nbt;
        }
    }
    
    public static void copy(Player source, Player target) {
        if (source.getPersistentData().contains("MythicBotanyPlayerInfo", Tag.TAG_COMPOUND)) {
            target.getPersistentData().put("MythicBotanyPlayerInfo", source.getPersistentData().getCompound("MythicBotanyPlayerInfo").copy());
        }
    }
}
