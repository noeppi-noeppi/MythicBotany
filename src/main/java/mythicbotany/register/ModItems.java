package mythicbotany.register;

import mythicbotany.MythicBotany;
import mythicbotany.alftools.*;
import mythicbotany.bauble.ItemAndwariRingCursed;
import mythicbotany.bauble.ItemFireRing;
import mythicbotany.bauble.ItemIceRing;
import mythicbotany.kvasir.ItemKvasirMead;
import mythicbotany.mimir.FullGjallarHorn;
import mythicbotany.misc.ItemFadedNetherStar;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.base.ItemBase;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.item.material.RuneItem;

@RegisterClass(registry = "ITEM")
public class ModItems {

    @SuppressWarnings("ConstantConditions")
    public static final Item asgardRune = new RuneItem(new Properties());
    public static final Item vanaheimRune = new RuneItem(new Properties());
    public static final Item alfheimRune = new RuneItem(new Properties());
    public static final Item midgardRune = new RuneItem(new Properties());
    public static final Item joetunheimRune = new RuneItem(new Properties());
    public static final Item muspelheimRune = new RuneItem(new Properties());
    public static final Item niflheimRune = new RuneItem(new Properties());
    public static final Item nidavellirRune = new RuneItem(new Properties());
    public static final Item helheimRune = new RuneItem(new Properties());
    public static final Item alfsteelIngot = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelNugget = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelTemplate = new AlfsteelTemplateItem();
    public static final Item alfsteelSword = new AlfsteelSword(new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelPick = new AlfsteelPick(new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelAxe = new AlfsteelAxe(new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelHelmet = new AlfsteelHelm(new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelChestplate = new AlfsteelArmor(ArmorItem.Type.CHESTPLATE, new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelLeggings = new AlfsteelArmor(ArmorItem.Type.LEGGINGS, new Properties().stacksTo(1).fireResistant());
    public static final Item alfsteelBoots = new AlfsteelArmor(ArmorItem.Type.BOOTS, new Properties().stacksTo(1).fireResistant());
    public static final Item manaRingGreatest = new GreatestManaRing(new Properties().stacksTo(1).fireResistant());
    public static final Item auraRingGreatest = new GreatestAuraRing(new Properties().stacksTo(1).fireResistant());
    public static final Item fadedNetherStar = new ItemFadedNetherStar();
    public static final Item fireRing = new ItemFireRing(new Properties().stacksTo(1));
    public static final Item iceRing = new ItemIceRing(new Properties().stacksTo(1));
    public static final Item gjallarHornEmpty = new ItemBase(MythicBotany.getInstance(), new Properties().stacksTo(1));
    public static final Item gjallarHornFull = new FullGjallarHorn(MythicBotany.getInstance(), new Properties().stacksTo(1));
    public static final Item cursedAndwariRing = new ItemAndwariRingCursed(new Properties().stacksTo(1));
    public static final Item andwariRing = new BaubleItem(new Properties().stacksTo(1).durability(32)) {};
    public static final Item fimbultyrTablet = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item kvasirBlood = new ItemBase(MythicBotany.getInstance(), new Properties().stacksTo(8));
    public static final Item kvasirMead = new ItemKvasirMead(MythicBotany.getInstance(), new Properties().stacksTo(8));
    public static final Item dreamCherry = new ItemBase(MythicBotany.getInstance(), new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1.2f).effect(() -> new MobEffectInstance(MobEffects.GLOWING, 20 * 5, 0), 0.3f).build()));
    public static final Item rawElementium = new ItemBase(MythicBotany.getInstance(), new Item.Properties());
    public static final Item alfPixieSpawnEgg = new ForgeSpawnEggItem(() -> ModEntities.alfPixie, 0xFFB9E2, 0xC6C6A1, new Item.Properties());
}
