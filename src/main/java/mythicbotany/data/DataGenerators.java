package mythicbotany.data;

import mythicbotany.MythicBotany;
import mythicbotany.data.recipes.*;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataGenerators {
    public static void gatherData(GatherDataEvent evt) {
		if (evt.includeServer()) {
			evt.getGenerator().addProvider(new BlockLootProvider(MythicBotany.getInstance(), evt.getGenerator()));
			BlockTagProvider blockTagProvider = new BlockTagProvider(MythicBotany.getInstance(), evt.getGenerator(), evt.getExistingFileHelper());
			evt.getGenerator().addProvider(blockTagProvider);
			evt.getGenerator().addProvider(new ItemTagProvider(MythicBotany.getInstance(), evt.getGenerator(), evt.getExistingFileHelper(), blockTagProvider));
			evt.getGenerator().addProvider(new RecipeProvider(MythicBotany.getInstance(), evt.getGenerator()));
			evt.getGenerator().addProvider(new SmeltingProvider(MythicBotany.getInstance(), evt.getGenerator()));
			evt.getGenerator().addProvider(new BlockStateProvider(MythicBotany.getInstance(), evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().addProvider(new ItemModelProvider(MythicBotany.getInstance(), evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().addProvider(new ElvenTradeProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ManaInfusionProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new PetalProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new RuneProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new SmithingProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new InfuserProvider(MythicBotany.getInstance(), evt.getGenerator()));
			evt.getGenerator().addProvider(new RitualProvider(MythicBotany.getInstance(), evt.getGenerator()));
		}
	}
}
