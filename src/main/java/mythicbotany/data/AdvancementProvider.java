package mythicbotany.data;

import io.github.noeppi_noeppi.libx.data.provider.AdvancementProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModEntities;
import mythicbotany.ModItems;
import mythicbotany.advancement.AlfRepairTrigger;
import mythicbotany.advancement.MjoellnirTrigger;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.lib.ModTags;

public class AdvancementProvider extends AdvancementProviderBase {

    public AdvancementProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    public void setup() {
        root().display(wandIcon())
                .task(items(ModTags.Items.INGOTS_TERRASTEEL));
                
        advancement("all_runes").display(ModItems.joetunheimRune)
                .tasks(itemTasks(
                        ModTags.Items.RUNES_AIR, ModTags.Items.RUNES_AUTUMN, ModTags.Items.RUNES_EARTH,
                        ModTags.Items.RUNES_ENVY, ModTags.Items.RUNES_FIRE, ModTags.Items.RUNES_GLUTTONY,
                        ModTags.Items.RUNES_GREED, ModTags.Items.RUNES_LUST, ModTags.Items.RUNES_MANA,
                        ModTags.Items.RUNES_PRIDE, ModTags.Items.RUNES_SLOTH, ModTags.Items.RUNES_SPRING,
                        ModTags.Items.RUNES_SUMMER, ModTags.Items.RUNES_WATER, ModTags.Items.RUNES_WINTER,
                        ModTags.Items.RUNES_WRATH
                ))
                .tasks(itemTasks(
                        ModItems.asgardRune, ModItems.vanaheimRune, ModItems.alfheimRune,
                        ModItems.midgardRune, ModItems.joetunheimRune, ModItems.muspelheimRune,
                        ModItems.niflheimRune, ModItems.nidavellirRune, ModItems.helheimRune
                ));
        
        advancement("mimir").display(ModItems.gjallarHornFull)
                .task(eat(ModItems.gjallarHornFull));
        
        advancement("alfheim").parent("mimir").display(ModItems.dreamCherry)
                .task(enter(Alfheim.DIMENSION));
        
        advancement("andwari").parent("alfheim").display(ModItems.andwariRing)
                .task(items(ModItems.andwariRing));
        
        advancement("mjoellnir").parent("mimir").display(ModBlocks.mjoellnir)
                .task(items(ModBlocks.mjoellnir));
        
        advancement("kill_pixie").parent("mjoellnir").display(ModItems.alfPixieSpawnEgg)
                .task(new MjoellnirTrigger.Instance(ItemPredicate.ANY, entity(ModEntities.alfPixie)));
        
        advancement("alfsteel").display(ModItems.alfsteelIngot)
                .task(items(ModItems.alfsteelIngot));
        
        advancement("mending_repair").parent("alfsteel").display(ModBlocks.alfsteelPylon)
                .task(new AlfRepairTrigger.Instance(stack(Enchantments.MENDING)));
    }
    
    private static ItemStack wandIcon() {
        ItemStack stack = new ItemStack(ModItems.dreamwoodTwigWand);
        stack.getOrCreateTag().putInt("color1", 4);
        stack.getOrCreateTag().putInt("color2", 3);
        return stack;
    }
}
