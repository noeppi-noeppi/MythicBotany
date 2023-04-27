package mythicbotany.register;

import org.moddingx.libx.registration.Registerable;
import org.moddingx.libx.registration.RegistryTransformer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class TrunkPlacerTransformer implements RegistryTransformer {

    public static final TrunkPlacerTransformer INSTANCE = new TrunkPlacerTransformer();

    private TrunkPlacerTransformer() {

    }

    @Nullable
    @Override
    public Object getAdditional(ResourceLocation id, Object object) {
        // If trunk placers get forge registries later, we don't need it any longer
        if (object instanceof TrunkPlacerType<?> trunkPlacerType && !(object instanceof IForgeRegistryEntry<?>)) {
            return new Registerable() {
                
                @Override
                public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
                    Registry.register(Registry.TRUNK_PLACER_TYPES, id, trunkPlacerType);
                }
            };
        } else {
            return null;
        }
    }
}
