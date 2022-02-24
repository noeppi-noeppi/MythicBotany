package mythicbotany.core;

import mythicbotany.alfheim.Alfheim;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class FancySkyChecker {
    
    public static boolean isFancySky() {
        Level level = Minecraft.getInstance().level;
        return level != null && Alfheim.DIMENSION.equals(level.dimension());
    }
}
