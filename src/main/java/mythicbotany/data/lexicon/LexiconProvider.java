package mythicbotany.data.lexicon;

import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import mythicbotany.ModEntities;
import mythicbotany.ModItems;
import mythicbotany.data.patchouli.PatchouliProviderBase;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

@Datagen
public class LexiconProvider extends PatchouliProviderBase {

    public LexiconProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper, "lexicon");
    }

    @Override
    protected void setup() {
        this.category("mythic_botany")
                .name("MythicBotany")
                .description("Dive deeper into the world of magic")
                .icon(ModItems.alfsteelSword)
                .sort(7);
        
        this.entry("runes")
                .name("Runes of the Nine Worlds")
                .icon(ModItems.niflheimRune)
                .text("You can create runes that represent the $(thing)Nine World$(0) in the $(item)Runic Altar$(0).")
                .text("They're tier 4 runes and require the Runes of the $(thing)Deadly sins$(0).")
                .text("Those new runes unleash great power but can be dangerous if not used correctly.")
                .text("Handle them with caution.")
                .flip()
                .rune("midgard_rune_runic_altar").caption("Midgard is the world of the humans.")
                .rune("alfheim_rune_runic_altar").caption("Alfheim is the world of the elves.")
                .rune("nidavellir_rune_runic_altar").caption("Nidavellir is the world of the dwarves.")
                .rune("joetunheim_rune_runic_altar").caption("Jötunheim is the world of the giants.")
                .rune("muspelheim_rune_runic_altar").caption("Muspelheim is the world of the fire giants.")
                .rune("niflheim_rune_runic_altar").caption("Niflheim is the world of the frost giants.")
                .rune("asgard_rune_runic_altar").caption("Asgard is the world of the Aesir.")
                .rune("vanaheim_rune_runic_altar").caption("Vanaheim is the world of the Vanir.")
                .rune("helheim_rune_runic_altar").caption("Helheim is the world of the dead.");
        
        this.entry("infuser")
                .name("The Mana Infuser")
                .icon(ModItems.alfsteelIngot)
                .text("The mana infuser is an upgraded version of the Terrestrial Agglomeration Plate that is also capable of creating other mana-alloys.")
                .text("Like the terrestrial agglomeration plate this requires a platform below.")
                .text("$(thing)However, this requires a dominant spark augment to pull mana from sparks.$()")
                .crafting("mana_infuser")
                .caption("Upgrading the terrestrial agglomeration plate to a mana infuser.")
                .multiblock("Mana Infuser", """
                        {
                          "mapping": {
                            "I": "mythicbotany:mana_infuser",
                            "G": "minecraft:gold_block",
                            "S": "botania:shimmerrock",
                            "0": "botania:shimmerrock"
                          },
                          "pattern": [
                            [ "   ", " I ", "   " ],
                            [ "SGS", "G0G", "SGS"]
                          ],
                          "symmetrical": true
                        }
                        """)
                .caption("The platform required for the mana infuser to work.")
                .item(ModItems.alfsteelIngot)
                .text("Alfsteel is created like terrasteel but with resources from Alfheim.")
                .text("It requires much more mana but can be used to create various powerful things.")
                .infuser("botania", "mythicbotany_infusion/terrasteel_ingot")
                .infuser("mythicbotany_infusion/alfsteel_ingot");
        
        this.entry("tools")
                .name("Alfsteel Tools")
                .icon(ModItems.alfsteelAxe)
                .text("For a long time now you thought diamond was the best you could reach without the power of a botanist.")
                .text("Then came netherite.")
                .text("It's the same with terrasteel and alfsteel.")
                .text("So just like diamond you can upgrade terrasteel tools and armor to their alfsteel counterparts via smithing.")
                .text("This improvement however, comes with higher mana costs to use those tools.")
                .item(ModItems.alfsteelSword).text("This sword does much more damage and you can swing it much faster.")
                .item(ModItems.alfsteelAxe).text("Despite being able to chop down whole trees in just seconds this axe can pull nearby items towards you when pressing shift and right-click.")
                .item(ModItems.alfsteelPick).text("This pickaxe is able to area-mine even more blocks at once. It mines multiple blocks in the direction you're looking as well. (This only works horizontally to prevent fall damage when mining down)")
                .item(ModItems.alfsteelHelmet).text("This helmet can get all ancient wills like the terrasteel but it also makes you reach further with your arms.")
                .item(ModItems.alfsteelChestplate).text("The knockback resistance of this chestplate is as high as the knockback resistance of a full terrasteel armor set.")
                .item(ModItems.alfsteelLeggings).text("This leggings drastically improves your walking speed.")
                .item(ModItems.alfsteelBoots).text("Those shoes make you jump much higher into the sky.");
        
        this.entry("manaband")
                .name("Alfsteel Mana Bands")
                .icon(ModItems.manaRingGreatest)
                .item(ModItems.manaRingGreatest).text("By upgrading the Greater Band of Mana with some alfsteel in the smithing table, you get a new band of mana that is capable of holding about twice as much mana as before.")
                .item(ModItems.auraRingGreatest).text("Just like the Greater Band of Mana you can also upgrade the Greater Band of Aura to produce mana at a much faster rate.");
        
        this.entry("pylons")
                .name("Alfsteel Pylons")
                .icon(ModBlocks.alfsteelPylon)
                .text("Those pylons are required to craft the Gaia Pylon.")
                .text("They can also receive mana from spreaders and repair alfsteel tools and tools enchanted with mending that are thrown on top of it.")
                .text("When repairing alfsteel tools you pay less mana than when using a mana tablet in your inventory.")
                .crafting("alfsteel_pylon")
                .crafting("gaia_pylon");
        
        this.entry("rings")
                .name("Elemental Rings")
                .icon(ModItems.fireRing)
                .crafting("fire_ring").caption("This ring makes you immune to all kind of fire damage and makes you ignite your enemy.")
                .flip()
                .crafting("ice_ring").caption("Applies a very strong slowness effect to your enemy for a short time while making you immune to cramming and in-wall damage.");
        
        this.entry("generating")
                .name("Generating Flora")
                .icon(ModBlocks.witherAconite)
                .petal("wither_aconite_petal_apothecary")
                .text("The wither aconite uses up nether stars dropped on it to create mana from them.")
                .text("If the wither aconite can't use up the nether star after 5 minutes, it will despawn like any other item.")
                .petal("raindeletia_petal_apothecary")
                .text("The raindeletia produces mana when it's raining and even more mana when it's thundering.")
                .text("Planting it on vivid grass or enchanted soil makes it generate much more.")
                .text("On regular soil, it even is too weak to generate mana during normal rain.")
                .crafting("mana_collector")
                .text("At some point in time your flowers will create mana that fast that spreaders are not enough to handle it.")
                .text("With the mana collector, you can put mana generated from flowers into the spark network directly.")
                .text("The mana collector acts as a little mana storage that can receive mana from flowers and interact with sparks.")
                .text("To extract the mana from the collector, you'll most likely need a recessive spark augment.");
        
        this.entry("functional")
                .name("Functional Flora")
                .icon(ModBlocks.hellebore)
                .petal("aquapanthus_petal_apothecary")
                .text("The Aquapanthus uses a very small amount of mana to fill nearby cauldrons and petal apothecaries with water.")
                .text("It can also fill some other fluid containers that are mostly unknown to the average botanist.")
                .petal("exoblaze_petal_apothecary")
                .text("The Exoblaze uses mana to fill the fuel of nearby brewing stands.")
                .text("Never put blaze powder in again when brewing your potions.")
                .text("Still a pretty primitive flower as powerful botanists have better devices that brewing stands...")
                .petal("hellebore_petal_apothecary")
                .text("Piglins and Hoglins will transform int ozombies in the overworld.")
                .text("While the exact reason is not yet known, botanists have foudn a way to use mana to keep these creatures safe in the overworld.")
                .text("Any Piglins or Hoglins n the range of the flower won't zombify when outside of the nether, as lon gas the flower has sufficient mana.")
                .petal("petrunia_petal_apothecary")
                .text("The Petrunia can activate rune rituals if provided with enough mana and placed near the central rune holder.");
        
        this.entry("mimir")
                .name("The well of Mimir")
                .icon(ModItems.gjallarHornFull)
                .text("You've already come in contact with the sacred tree Yggdrasil: The central block of the portal to Alfheim was a piece linked to it.")
                .text("By rearranging the recipe a bit, you think you have found a way to get an Branch of Yggdrasil that is capable of providing water drained from the roots of Yggdrasil if given a bit of mana.")
                .text("You know that Yggdrasil has its roots in the well of Mimir.")
                .text("And by the legend it's told that Odin was able to perform powerful rune magic after drinking from said well.")
                .text("You think you can achieve this too.")
                .crafting("yggdrasil_branch")
                .caption("However catching the water will need a special type of container.")
                .mana("gjallar_horn_empty_mana_infusion")
                .caption("Creating an empty Gjallarhorn")
                .item(ModItems.gjallarHornFull)
                .text("With a gjallarhorn, you are able to catch the water. For this, you need to place it in the branch.")
                .text("With a bit of mana, the gjallarhorn will fill up.")
                .text("Drinking from the full Gjallarhorn will provide you the knowledge to perform Rune Rituals.");
        
        this.entry("rune_rituals")
                .name("Rune Ritauls")
                .icon(ModItems.fimbultyrTablet)
                .text("Runes are way more powerful than most people think.")
                .text("However, to understand their full power, you must have drunk from the Well of Mimir.")
                .text("A rune ritual will require a shape of runes on the floor.")
                .text("To put runes on the floor, you need rune holders.")
                .text("A special type of rune holder is required for the central rune.")
                .text("You'll get back all the runes except the central one.")
                .text("The shape can be rotated and flipped but it must be all on the same Y-level.")
                .text("A ritual may also require specific ingredients that need to be thrown near the central rune holder.")
                .text("Some rituals also require mana.")
                .text("You can provide this through mana tablets and mana bands in your inventory.")
                .text("To start a ritual, right click the central rune holder with a Wand of the Forest.")
                .text("If a ritual gets interrupted it'll be reset and you'll get back everything except the mana you paid.")
                .crafting("rune_holder")
                .crafting("central_rune_holder")
                .flip()
                .item(ModItems.fimbultyrTablet)
                .text("The Rune tablet of Fimbultyr will be dropped when the Guardian of Gaia II is slain by a player who has acquired the Knowledge of Mimir.")
                .text("It is used as central rune in the most powerful rune rituals.");
        
        this.entry("mjoellnir")
                .name("Mjoellnir")
                .icon(ModBlocks.mjoellnir)
                .text("Mjöllnir is the hammer of the God Thor.")
                .text("He throws it against the giants and other enemies to deal large amounts of damage.")
                .text("You think you can create a replica of Mjöllnir with a Rune Ritual and use it as a weapon.")
                .text("You can attack with Mjöllnir just like with any other weapon.")
                .text("However, the attack cooldown is very high.")
                .text("Using the Hammer Mobility Enchantment, the cooldown can be lowered.")
                .text("You can also throw Mjöllnir with a right click.")
                .text("If it hits an entity it causes a lightning strike and deals a lot of damage.")
                .text("However, the hammer is very heavy.")
                .text("As the gods get their powers from the golden apples of Idun, you'll need the absorption effect to hold mjöllnir.")
                .text("When the absorption effect runs out, the hammer will get placed in the world at your position.")
                .text("Having the ring of thor equipped will also allow you to hold mjöllnir.")
                .text("To manually place the hammer in the world, use a shift right click.")
                .text("To pick it up again do a right click on the placed hammer.")
                .text("Mjöllnir can be enchanted with most sword enchantments.")
                .text("They only affect the melee damage.")
                .text("It can also be enchanted with most bow enchantments.")
                .text("They only affect ranged attacks.")
                .text("It can also be anchanted with Loyalty, which makes it return faster after throwing it.")
                .ritual(ModBlocks.mjoellnir, "mythicbotany_rune_rituals/mjoellnir")
                .caption("Creating a replica of the legendary weapon Mjöllnir.");
        
        this.category("alfheim")
                .name("Alfheim")
                .description("Everything about the Alfheim dimension")
                .icon(ModItems.dreamCherry)
                .sort(8);
        
        this.entry("alfheim_resources")
                .name("Resources of Alfheim")
                .icon(ModBlocks.elementiumOre)
                .item(ModItems.dreamCherry)
                .text("Dream cherries are dropped by Dreamwood Leaves.")
                .text("Those leaves generate on Dreamwood trees all throughout Alfheim.")
                .text("Eating a dream cherry will give you a big saturation boost, which means it takes much longer before your hunger goes down again.")
                .trade("dreamwood_leaves_elven_trade")
                .caption("Trading for Dreamwood Leaves.")
                .item(ModBlocks.elementiumOre)
                .text("Elementium Ore can be found underground roughly on the same levels as iron in the overworld.")
                .text("In Alfheim, where the magic is much more present than in the overworld, the raw materials are naturally generated.")
                .text("There's no need to infuse them with mana.")
                .item(ModBlocks.dragonstoneOre)
                .text("Dragonstone Ore is also found in Alfheim but way lower than Elementium.")
                .text("Just like diamonds there's no need to smelt the ore, it'll drop Dragonstone directly.")
                .item(ModBlocks.goldOre)
                .text("Gold, as the material of the gods, can also be found in Alfheim.")
                .text("In the Golden Fields biome you can find way much more gold than usual.")
                .entity(ModEntities.alfPixie)
                .caption("Pixies fly in the air of Alfheim. They'll drop Pixie Dust on death.");
        
        this.entry("alfheim_landscape")
                .name("Alfheim Landscape")
                .icon(ModItems.alfPixieSpawnEgg)
                .text("In Alfheim you can find some dreamwood trees everywhere.")
                .text("Also abandoned petal apothecaries are found throughout the world.")
                .text("They sometimes even contain a few petals.")
                .text("Alfheim is rich of animals but when it gets dark you'll need to face hordes of witches and illusioners.")
                .image("Mana Crystals", "textures/image/alfheim_hills.png")
                .caption("On the hills in Alfheim mana crystals may spawn.")
                .caption("They're made out of bifrost blocks and contain a bit of mana.")
                .image("Dreamwood Forest", "textures/image/dreamwood_forest.png")
                .caption("In a dreamwood forest dreamwood trees are found more densely.")
                .image("Golden Fields", "textures/image/golden_fields.png")
                .caption("In the golden fields biome you can find much more gold ore and occasionally an Andwari Cave.");
        
        this.entry("andwari")
                .name("Ring of Andwari")
                .icon(ModItems.andwariRing)
                .text("Andwari was a dwarf that lived in a cave in Alfheim.")
                .text("He owned a big gold treasure and a ring to multiply his treasure.")
                .text("When Loki wanted to steal that treasure, Andwari put a curse on his ring to prevent it from being used.")
                .text("However, you think you have found a way to temporarily remove the curse and make the ring work again.")
                .text("If cou could only find the ring...")
                .image("Andwari Cave", "textures/image/andwari_entrance.png", "textures/image/andwari_cave.png")
                .caption("Andwari caves generate only in the Golden Fields biome.")
                .ritual(ModItems.andwariRing, "mythicbotany_rune_rituals/andwari_ring")
                .caption("After removing the curse you can use it for a time.")
                .caption("Just place a gold block and get a reward.")
                .caption("However, this requires some mana.")
                .caption("The curse will also come back slowly.")
                .caption("Make sure it never comes back completely or it will consume huge amounts of mana and poison you.");
        
        this.entry("kvasir")
                .name("The Mead of Kvasir")
                .icon(ModItems.kvasirMead)
                .text("Kvasir was a dwarf formed by the gods after the Aesir made peace with the Vanir.")
                .text("He wandered through the nine worlds and was highly regarded everywhere.")
                .text("However, the two dwarves Fjalar and Galar wanted to steal Kvasirs' talents.")
                .text("So they killed him and mixed his blood with honey to form a mead.")
                .text("Drinking this mead will allow you to travel the worlds like Kvasir.")
                .crafting("kvasir_mead")
                .caption("After drinking the mead you'll be able to step in the Portal to Alfheim and travel to Alfheim yourself.")
                .caption("If you accidentally break your portal in Alfheim you can fix the frame and throw a dragonstone or some pixie dust in.")
                .caption("When it despawns, the portal will reopen.")
                .ritual(ModItems.kvasirBlood, "mythicbotany_rune_rituals/kvasir_blood")
                .caption("By using a wandering trader as a symbol for Kvasir you should be able to get blood to form the Mead of Kvasir.");
    }
}
