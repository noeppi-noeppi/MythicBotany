package mythicbotany;

import mythicbotany.data.recipes.BotanicAdditionsRecipeProvider;
import mythicbotany.data.recipes.FeywildRecipeProvider;
import mythicbotany.data.recipes.FeywildTagsProvider;
import org.moddingx.libx.datagen.DatagenSystem;

@SuppressWarnings("unused")
public class MythicBotanyExtra {
    
    public static void init() {
        DatagenSystem.create(MythicBotany.getInstance(), system -> {
            system.addDataProvider(BotanicAdditionsRecipeProvider::new);
            system.addDataProvider(FeywildTagsProvider::new);
            system.addDataProvider(FeywildRecipeProvider::new);
        });
    }
}
