package mythicbotany.base;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface Registerable {

    /**
     * Gets additional items that should be registered. Those may be Registerable,
     * Items, Block TileEntities, Biomes ...
     */
    default Set<Object> getAdditionalRegisters() {
        return Collections.emptySet();
    }

    /**
     * Gets additional items that should be registered. Those may be Registerable,
     * Items, Block TileEntities, Biomes ... The ones here may have a postfix.
     * That allows to register multiple thing to the same registry.
     */
    default Map<String, Object> getNamedAdditionalRegisters() {
        return Collections.emptyMap();
    }

    /**
     * Do stuff needed on the client
     */
    @OnlyIn(Dist.CLIENT)
    default void registerClient(@SuppressWarnings("unused") String id) {

    }
}
