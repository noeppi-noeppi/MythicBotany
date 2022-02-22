package mythicbotany.register;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import io.github.noeppi_noeppi.libx.mod.registration.RegistryTransformer;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FeatureTransformer implements RegistryTransformer {

    public static final FeatureTransformer INSTANCE = new FeatureTransformer();

    private FeatureTransformer() {

    }

    @Nullable
    @Override
    public Object getAdditional(ResourceLocation id, Object object) {
        if (object instanceof ConfiguredFeature<?, ?> feature) {
            return new Registerable() {
                
                @Override
                public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
                    Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature);
                }
            };
            
        } else if (object instanceof PlacedFeature placement) {
            return new Registerable() {

                @Override
                public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
                    Registry.register(BuiltinRegistries.PLACED_FEATURE, id, placement);
                }
            };

        } else if (object instanceof ConfiguredStructureFeature<?, ?> structure) {
            return new Registerable() {

                @Override
                public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
                    Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, structure);
                }
            };

        } else {
            return null;
        }
    }
}
