package mythicbotany.data.patchouli.page;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface PageBuilder {

    boolean isFirst();
    void addPage(JsonObject page);
    String translate(String localized);
    
    void flipToEven();
    void checkAssets(ResourceLocation path);
}
