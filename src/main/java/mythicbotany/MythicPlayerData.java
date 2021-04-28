package mythicbotany;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

public class MythicPlayerData {
    
    public static CompoundNBT getData(PlayerEntity player) {
        if (player.getPersistentData().contains("MythicBotanyPlayerInfo", Constants.NBT.TAG_COMPOUND)) {
            return player.getPersistentData().getCompound("MythicBotanyPlayerInfo");
        } else {
            CompoundNBT nbt = new CompoundNBT();
            player.getPersistentData().put("MythicBotanyPlayerInfo", nbt);
            return nbt;
        }
    }
    
    public static void copy(PlayerEntity source, PlayerEntity target) {
        if (source.getPersistentData().contains("MythicBotanyPlayerInfo", Constants.NBT.TAG_COMPOUND)) {
            target.getPersistentData().put("MythicBotanyPlayerInfo", source.getPersistentData().getCompound("MythicBotanyPlayerInfo").copy());
        }
    }
}
