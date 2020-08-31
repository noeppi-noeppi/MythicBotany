package mythicbotany;

import mythicbotany.alftools.*;
import mythicbotany.base.ItemBase;
import mythicbotany.bauble.ItemFireRing;
import mythicbotany.bauble.ItemIceRing;
import mythicbotany.functionalflora.WitherAconite;
import mythicbotany.runic.ItemRunicSpell;
import mythicbotany.wand.ItemDreamwoodWand;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.Rarity;
import vazkii.botania.common.item.material.ItemRune;

public class ModItems {

    public static final Item asgardRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item vanaheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item alfheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item midgardRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item joetunheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item muspelheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item niflheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item nidavellirRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item helheimRune = new ItemRune(new Properties().group(MythicBotany.TAB));
    public static final Item alfsteelIngot = new ItemBase(new Properties());
    public static final Item alfsteelNugget = new ItemBase(new Properties());
    public static final Item alfsteelArmorUpgrade = new ItemBase(new Properties());
    public static final Item alfsteelSword = new AlfsteelSword(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelPick = new AlfsteelPick(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelAxe = new AlfsteelAxe(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelHelmet = new AlfsteelHelm(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelChest = new AlfsteelArmor(EquipmentSlotType.CHEST, new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelLegs = new AlfsteelArmor(EquipmentSlotType.LEGS, new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item alfsteelBoots = new AlfsteelArmor(EquipmentSlotType.FEET, new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item greatestManaRing = new GreatestManaRing(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item greatestAuraRing = new GreatestAuraRing(new Properties().group(MythicBotany.TAB).maxStackSize(1).isBurnable());
    public static final Item runicSpell = new ItemRunicSpell(new Properties());
    public static final Item fadedNetherStar = new Item(new Properties().maxStackSize(1).maxDamage(WitherAconite.MANA_PER_STAR));
    public static final Item dreamwoodWand = new ItemDreamwoodWand(new Properties().group(MythicBotany.TAB).maxStackSize(1).rarity(Rarity.RARE));
    public static final Item fireRing = new ItemFireRing(new Properties().group(MythicBotany.TAB).maxStackSize(1));
    public static final Item iceRing = new ItemIceRing(new Properties().group(MythicBotany.TAB).maxStackSize(1));

    public static void register() {
        MythicBotany.register("asgard_rune", asgardRune);
        MythicBotany.register("vanaheim_rune", vanaheimRune);
        MythicBotany.register("alfheim_rune", alfheimRune);
        MythicBotany.register("midgard_rune", midgardRune);
        MythicBotany.register("joetunheim_rune", joetunheimRune);
        MythicBotany.register("muspelheim_rune", muspelheimRune);
        MythicBotany.register("niflheim_rune", niflheimRune);
        MythicBotany.register("nidavellir_rune", nidavellirRune);
        MythicBotany.register("helheim_rune", helheimRune);
        MythicBotany.register("alfsteel_ingot", alfsteelIngot);
        MythicBotany.register("alfsteel_nugget", alfsteelNugget);
        MythicBotany.register("alfsteel_armor_upgrade", alfsteelArmorUpgrade);
        MythicBotany.register("alfsteel_sword", alfsteelSword);
        MythicBotany.register("alfsteel_pick", alfsteelPick);
        MythicBotany.register("alfsteel_axe", alfsteelAxe);
        MythicBotany.register("alfsteel_helmet", alfsteelHelmet);
        MythicBotany.register("alfsteel_chestplate", alfsteelChest);
        MythicBotany.register("alfsteel_leggings", alfsteelLegs);
        MythicBotany.register("alfsteel_boots", alfsteelBoots);
        MythicBotany.register("mana_ring_greatest", greatestManaRing);
        MythicBotany.register("aura_ring_greatest", greatestAuraRing);
        MythicBotany.register("runic_spell", runicSpell);
        MythicBotany.register("faded_nether_star", fadedNetherStar);
        MythicBotany.register("dreamwood_twig_wand", dreamwoodWand);
        MythicBotany.register("fire_ring", fireRing);
        MythicBotany.register("ice_ring", iceRing);
    }
}
