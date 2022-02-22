package mythicbotany.alfheim.structure;

import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.MythicBotany;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import java.util.function.Consumer;

public class AndwariCave extends BaseStructure implements Registerable {

    private static final ResourceLocation TEMPLATE = new ResourceLocation(MythicBotany.getInstance().modid, "andwari_cave");
    
    public AndwariCave() {
        super("andwari_cave");
    }
    
    @Override
    public void registerCommon(ResourceLocation id, Consumer<Runnable> defer) {
        defer.accept(() -> StructureFeature.STRUCTURES_REGISTRY.put(id.toString(), this));
    }
}
