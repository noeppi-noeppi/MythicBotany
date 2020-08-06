package mythicbotany.base;

import java.util.Collections;
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
     * Do stuff needed on the client
     */
    default void registerClient(@SuppressWarnings("unused") String id) {

    }
}
