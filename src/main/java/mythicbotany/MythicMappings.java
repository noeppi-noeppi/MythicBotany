package mythicbotany;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class MythicMappings {

    private static final ResourceLocation OLD_DREAMWOOD_WAND_ID = MythicBotany.getInstance().resource("dreamwood_twig_wand");
    private static final ResourceLocation NEW_DREAMWOOD_WAND_ID = new ResourceLocation("botania", "dreamwood_wand");
    
    public static void remapItems(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings(MythicBotany.getInstance().modid)) {
            if (OLD_DREAMWOOD_WAND_ID.equals(mapping.key)) {
                Item item = ForgeRegistries.ITEMS.getValue(NEW_DREAMWOOD_WAND_ID);
                if (item != null) {
                    mapping.remap(item);
                } else {
                    mapping.warn();
                }
            }
        }
    }
}
