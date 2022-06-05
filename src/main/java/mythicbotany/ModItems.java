package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.libx.base.ItemBase;
import mythicbotany.alftools.*;
import mythicbotany.bauble.ItemAndwariRingCursed;
import mythicbotany.bauble.ItemFireRing;
import mythicbotany.bauble.ItemIceRing;
import mythicbotany.kvasir.ItemKvasirMead;
import mythicbotany.mimir.FullGjallarHorn;
import mythicbotany.misc.ItemFadedNetherStar;
import mythicbotany.wand.ItemDreamwoodWand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.ForgeSpawnEggItem;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.material.ItemRune;

@RegisterClass
public class ModItems {

    @SuppressWarnings("ConstantConditions")
    public static final Item asgardRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item vanaheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item alfheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item midgardRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item joetunheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item muspelheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item niflheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item nidavellirRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item helheimRune = new ItemRune(new Properties().tab(MythicBotany.getInstance().tab));
    public static final Item alfsteelIngot = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelNugget = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelSword = new AlfsteelSword(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelPick = new AlfsteelPick(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelAxe = new AlfsteelAxe(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelHelmet = new AlfsteelHelm(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelChestplate = new AlfsteelArmor(EquipmentSlot.CHEST, new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelLeggings = new AlfsteelArmor(EquipmentSlot.LEGS, new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item alfsteelBoots = new AlfsteelArmor(EquipmentSlot.FEET, new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item manaRingGreatest = new GreatestManaRing(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item auraRingGreatest = new GreatestAuraRing(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).fireResistant());
    public static final Item fadedNetherStar = new ItemFadedNetherStar();
    //public static final Item dreamwoodTwigWand = new ItemDreamwoodWand(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).rarity(Rarity.RARE));
    public static final Item fireRing = new ItemFireRing(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1));
    public static final Item iceRing = new ItemIceRing(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1));
    public static final Item gjallarHornEmpty = new ItemBase(MythicBotany.getInstance(), new Properties().stacksTo(1));
    public static final Item gjallarHornFull = new FullGjallarHorn(MythicBotany.getInstance(), new Properties().stacksTo(1));
    public static final Item cursedAndwariRing = new ItemAndwariRingCursed(new Properties().stacksTo(1));
    public static final Item andwariRing = new ItemBauble(new Properties().tab(MythicBotany.getInstance().tab).stacksTo(1).durability(32)) {};
    public static final Item fimbultyrTablet = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item kvasirBlood = new ItemBase(MythicBotany.getInstance(), new Properties().stacksTo(8));
    public static final Item kvasirMead = new ItemKvasirMead(MythicBotany.getInstance(), new Properties().stacksTo(8));
    public static final Item dreamCherry = new ItemBase(MythicBotany.getInstance(), new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1.2f).effect(() -> new MobEffectInstance(MobEffects.GLOWING, 20 * 5, 0), 0.3f).build()));
    public static final Item rawElementium = new ItemBase(MythicBotany.getInstance(), new Item.Properties());
    public static final Item alfPixieSpawnEgg = new ForgeSpawnEggItem(() -> ModEntities.alfPixie, 0xFFB9E2, 0xC6C6A1, new Item.Properties().tab(MythicBotany.getInstance().tab));
}
