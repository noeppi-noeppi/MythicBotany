package mythicbotany.alfheim.structure.piece;

import mythicbotany.MythicBotany;
import net.minecraft.core.Registry;

public class ModStructurePieces {
    
    public static void setup() {
        Registry.register(Registry.STRUCTURE_POOL_ELEMENT, MythicBotany.getInstance().resource("andwari_cave"), AndwariCavePiece.TYPE);
    }
}
