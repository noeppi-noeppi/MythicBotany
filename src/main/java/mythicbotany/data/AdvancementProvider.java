package mythicbotany.data;

import mythicbotany.advancement.AlfRepairTrigger;
import mythicbotany.advancement.MjoellnirTrigger;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModEntities;
import mythicbotany.register.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.AdvancementProviderBase;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

public class AdvancementProvider extends AdvancementProviderBase {

    public AdvancementProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    public void setup() {
        this.root().display(wandIcon())
                .background(this.mod.resource("textures/block/alfsteel_block.png"))
                .task(this.items(BotaniaTags.Items.INGOTS_TERRASTEEL));

        this.advancement("all_runes").display(ModItems.joetunheimRune)
                .tasks(this.itemTasks(
                        BotaniaItems.runeAir,
                        BotaniaItems.runeAutumn,
                        BotaniaItems.runeEarth,
                        BotaniaItems.runeEnvy,
                        BotaniaItems.runeFire,
                        BotaniaItems.runeGluttony,
                        BotaniaItems.runeGreed,
                        BotaniaItems.runeLust,
                        BotaniaItems.runeMana,
                        BotaniaItems.runePride,
                        BotaniaItems.runeSloth,
                        BotaniaItems.runeSpring,
                        BotaniaItems.runeSummer,
                        BotaniaItems.runeWater,
                        BotaniaItems.runeWinter,
                        BotaniaItems.runeWrath,
                        ModItems.asgardRune, ModItems.vanaheimRune, ModItems.alfheimRune,
                        ModItems.midgardRune, ModItems.joetunheimRune, ModItems.muspelheimRune,
                        ModItems.niflheimRune, ModItems.nidavellirRune, ModItems.helheimRune
                ));

        this.advancement("mimir").display(ModItems.gjallarHornFull)
                .task(this.eat(ModItems.gjallarHornFull));

        this.advancement("alfheim").parent("mimir").display(ModItems.dreamCherry)
                .task(this.enter(Alfheim.DIMENSION));

        this.advancement("andwari").parent("alfheim").display(ModItems.andwariRing)
                .task(this.items(ModItems.andwariRing));

        this.advancement("mjoellnir").parent("mimir").display(ModBlocks.mjoellnir)
                .task(this.items(ModBlocks.mjoellnir));

        this.advancement("kill_pixie").parent("mjoellnir").display(ModItems.alfPixieSpawnEgg)
                .task(new MjoellnirTrigger.Instance(ItemPredicate.ANY, this.entity(ModEntities.alfPixie)));

        this.advancement("alfsteel").display(ModItems.alfsteelIngot)
                .task(this.items(ModItems.alfsteelIngot));

        this.advancement("mending_repair").parent("alfsteel").display(ModBlocks.alfsteelPylon)
                .task(new AlfRepairTrigger.Instance(this.stack(Enchantments.MENDING)));
    }
    
    private static ItemStack wandIcon() {
        ItemStack stack = new ItemStack(BotaniaItems.dreamwoodWand);
        stack.getOrCreateTag().putInt("color1", 4);
        stack.getOrCreateTag().putInt("color2", 3);
        return stack;
    }
}
