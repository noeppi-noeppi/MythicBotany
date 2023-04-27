package mythicbotany.data;

import org.moddingx.libx.annotation.data.Datagen;
import org.moddingx.libx.datagen.provider.AdvancementProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModEntities;
import mythicbotany.ModItems;
import mythicbotany.advancement.AlfRepairTrigger;
import mythicbotany.advancement.MjoellnirTrigger;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import vazkii.botania.common.lib.ModTags;

@Datagen
public class AdvancementProvider extends AdvancementProviderBase {

    public AdvancementProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    public void setup() {
        this.root().display(wandIcon())
                .background(this.mod.resource("textures/block/alfsteel_block.png"))
                .task(this.items(ModTags.Items.INGOTS_TERRASTEEL));

        this.advancement("all_runes").display(ModItems.joetunheimRune)
                .tasks(this.itemTasks(
                        vazkii.botania.common.item.ModItems.runeAir,
                        vazkii.botania.common.item.ModItems.runeAutumn,
                        vazkii.botania.common.item.ModItems.runeEarth,
                        vazkii.botania.common.item.ModItems.runeEnvy,
                        vazkii.botania.common.item.ModItems.runeFire,
                        vazkii.botania.common.item.ModItems.runeGluttony,
                        vazkii.botania.common.item.ModItems.runeGreed,
                        vazkii.botania.common.item.ModItems.runeLust,
                        vazkii.botania.common.item.ModItems.runeMana,
                        vazkii.botania.common.item.ModItems.runePride,
                        vazkii.botania.common.item.ModItems.runeSloth,
                        vazkii.botania.common.item.ModItems.runeSpring,
                        vazkii.botania.common.item.ModItems.runeSummer,
                        vazkii.botania.common.item.ModItems.runeWater,
                        vazkii.botania.common.item.ModItems.runeWinter,
                        vazkii.botania.common.item.ModItems.runeWrath,
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
        ItemStack stack = new ItemStack(vazkii.botania.common.item.ModItems.dreamwoodWand);
        stack.getOrCreateTag().putInt("color1", 4);
        stack.getOrCreateTag().putInt("color2", 3);
        return stack;
    }
}
