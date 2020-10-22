package mythicbotany;

import io.github.noeppi_noeppi.libx.mod.registration.ItemBase;
import mythicbotany.alftools.*;
import mythicbotany.bauble.ItemFireRing;
import mythicbotany.bauble.ItemIceRing;
import mythicbotany.functionalflora.WitherAconite;
import mythicbotany.wand.ItemDreamwoodWand;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.Rarity;
import vazkii.botania.common.item.material.ItemRune;

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
    public static final Item alfsteelSword = new AlfsteelSword(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelPick = new AlfsteelPick(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelAxe = new AlfsteelAxe(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelHelmet = new AlfsteelHelm(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelChest = new AlfsteelArmor(EquipmentSlotType.CHEST, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelLegs = new AlfsteelArmor(EquipmentSlotType.LEGS, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item alfsteelBoots = new AlfsteelArmor(EquipmentSlotType.FEET, new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item greatestManaRing = new GreatestManaRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item greatestAuraRing = new GreatestAuraRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).isBurnable());
    public static final Item fadedNetherStar = new Item(new Properties().maxStackSize(1).maxDamage(WitherAconite.MANA_PER_STAR));
    public static final Item dreamwoodWand = new ItemDreamwoodWand(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1).rarity(Rarity.RARE));
    public static final Item fireRing = new ItemFireRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1));
    public static final Item iceRing = new ItemIceRing(new Properties().group(MythicBotany.getInstance().tab).maxStackSize(1));

    public static void register() {
        MythicBotany.getInstance().register("asgard_rune", asgardRune);
        MythicBotany.getInstance().register("vanaheim_rune", vanaheimRune);
        MythicBotany.getInstance().register("alfheim_rune", alfheimRune);
        MythicBotany.getInstance().register("midgard_rune", midgardRune);
        MythicBotany.getInstance().register("joetunheim_rune", joetunheimRune);
        MythicBotany.getInstance().register("muspelheim_rune", muspelheimRune);
        MythicBotany.getInstance().register("niflheim_rune", niflheimRune);
        MythicBotany.getInstance().register("nidavellir_rune", nidavellirRune);
        MythicBotany.getInstance().register("helheim_rune", helheimRune);
        MythicBotany.getInstance().register("alfsteel_ingot", alfsteelIngot);
        MythicBotany.getInstance().register("alfsteel_nugget", alfsteelNugget);
        MythicBotany.getInstance().register("alfsteel_armor_upgrade", alfsteelArmorUpgrade);
        MythicBotany.getInstance().register("alfsteel_sword", alfsteelSword);
        MythicBotany.getInstance().register("alfsteel_pick", alfsteelPick);
        MythicBotany.getInstance().register("alfsteel_axe", alfsteelAxe);
        MythicBotany.getInstance().register("alfsteel_helmet", alfsteelHelmet);
        MythicBotany.getInstance().register("alfsteel_chestplate", alfsteelChest);
        MythicBotany.getInstance().register("alfsteel_leggings", alfsteelLegs);
        MythicBotany.getInstance().register("alfsteel_boots", alfsteelBoots);
        MythicBotany.getInstance().register("mana_ring_greatest", greatestManaRing);
        MythicBotany.getInstance().register("aura_ring_greatest", greatestAuraRing);
        MythicBotany.getInstance().register("faded_nether_star", fadedNetherStar);
        MythicBotany.getInstance().register("dreamwood_twig_wand", dreamwoodWand);
        MythicBotany.getInstance().register("fire_ring", fireRing);
        MythicBotany.getInstance().register("ice_ring", iceRing);
    }
}
