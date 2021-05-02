package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import io.github.noeppi_noeppi.libx.mod.registration.ItemBase;
import mythicbotany.alftools.*;
import mythicbotany.bauble.ItemAndwariRingCursed;
import mythicbotany.bauble.ItemFireRing;
import mythicbotany.bauble.ItemIceRing;
import mythicbotany.kvasir.ItemKvasirMead;
import mythicbotany.mimir.FullGjallarHorn;
import mythicbotany.misc.ItemFadedNetherStar;
import mythicbotany.wand.ItemDreamwoodWand;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.Rarity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.material.ItemRune;

@RegisterClass
public class ModItems {

    public static final Item asgardRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item vanaheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item alfheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item midgardRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item joetunheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item muspelheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item niflheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item nidavellirRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item helheimRune = new ItemRune(new Properties().group(MythicBotany.getInstance().tab));
    public static final Item alfsteelIngot = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelNugget = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelArmorUpgrade = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item alfsteelSword = new AlfsteelSword(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelPick = new AlfsteelPick(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelAxe = new AlfsteelAxe(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelHelmet = new AlfsteelHelm(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelChestplate = new AlfsteelArmor(EquipmentSlotType.CHEST, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelLeggings = new AlfsteelArmor(EquipmentSlotType.LEGS, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item alfsteelBoots = new AlfsteelArmor(EquipmentSlotType.FEET, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item manaRingGreatest = new GreatestManaRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item auraRingGreatest = new GreatestAuraRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isImmuneToFire());
    public static final Item fadedNetherStar = new ItemFadedNetherStar();
    public static final Item dreamwoodTwigWand = new ItemDreamwoodWand(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).rarity(Rarity.RARE));
    public static final Item fireRing = new ItemFireRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1));
    public static final Item iceRing = new ItemIceRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1));
    public static final Item gjallarHornEmpty = new ItemBase(MythicBotany.getInstance(), new Properties().maxStackSize(1));
    public static final Item gjallarHornFull = new FullGjallarHorn(MythicBotany.getInstance(), new Properties().maxStackSize(1));
    public static final Item cursedAndwariRing = new ItemAndwariRingCursed(new Properties().maxStackSize(1));
    public static final Item andwariRing = new ItemBauble(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).maxDamage(32)) {};
    public static final Item fimbultyrTablet = new ItemBase(MythicBotany.getInstance(), new Properties());
    public static final Item kvasirBlood = new ItemBase(MythicBotany.getInstance(), new Properties().maxStackSize(8));
    public static final Item kvasirMead = new ItemKvasirMead(MythicBotany.getInstance(), new Properties().maxStackSize(8));
    public static final Item dreamCherry = new ItemBase(MythicBotany.getInstance(), new Item.Properties().food(new Food.Builder().hunger(10).saturation(1.2f).effect(() -> new EffectInstance(Effects.GLOWING, 20 * 5, 0), 0.3f).build()));
    public static final Item alfPixieSpawnEgg = new SpawnEggItem(ModEntities.alfPixie, 0xFFB9E2, 0xC6C6A1, new Item.Properties().group(MythicBotany.getInstance().tab));
}
