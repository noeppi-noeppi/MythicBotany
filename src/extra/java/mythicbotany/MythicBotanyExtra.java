package mythicbotany;

import mythicbotany.data.recipes.BotanicAdditionsRecipeProvider;
import mythicbotany.data.recipes.FeywildRecipeProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
public class MythicBotanyExtra {
    
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MythicBotanyExtra::gatherData);
    }
    
    private static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(true, new FeywildRecipeProvider(MythicBotany.getInstance(), event.getGenerator()));
        event.getGenerator().addProvider(true, new BotanicAdditionsRecipeProvider(MythicBotany.getInstance(), event.getGenerator()));
    }
}
