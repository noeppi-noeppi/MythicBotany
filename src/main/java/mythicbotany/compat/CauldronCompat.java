package mythicbotany.compat;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class CauldronCompat {
    
    public static boolean canFill(BlockState state, @Nullable TileEntity tile) {
        if (ModList.get().isLoaded("inspirations")) {
            try {
                if (tile != null) {
                    Class<?> cls = Class.forName("knightminer.inspirations.recipes.tileentity.CauldronTileEntity");
                    if (cls.isAssignableFrom(tile.getClass())) {
                        return (boolean) (Boolean) cls.getMethod("canMimicVanilla").invoke(tile);
                    }
                }
            } catch (Exception | NoClassDefFoundError e) {
                return false;
            }
        }
        return !state.hasTileEntity();
    }
}
