package mythicbotany.data;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;

public class AlwaysExistentModelFile extends ModelFile {

    public AlwaysExistentModelFile(ResourceLocation rl) {
        super(rl);
    }

    @Override
    protected boolean exists() {
        return true;
    }
}
