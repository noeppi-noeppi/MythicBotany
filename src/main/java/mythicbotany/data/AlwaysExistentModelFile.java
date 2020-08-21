package mythicbotany.data;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;

/**
 * Just because sometimes the generator don't seems to find a model. An @{}code AlwaysExistentModelFile} will
 * always return true in it's {@code exists} method.
 */
public class AlwaysExistentModelFile extends ModelFile {

    public AlwaysExistentModelFile(ResourceLocation rl) {
        super(rl);
    }

    @Override
    protected boolean exists() {
        return true;
    }
}
