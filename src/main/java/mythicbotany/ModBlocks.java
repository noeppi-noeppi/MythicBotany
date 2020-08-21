package mythicbotany;

import mythicbotany.base.BlockBase;
import mythicbotany.base.BlockTE;
import mythicbotany.base.BlockTEManaHUD;
import mythicbotany.collector.TileManaCollector;
import mythicbotany.functionalflora.Aquapanthus;
import mythicbotany.functionalflora.Exoblaze;
import mythicbotany.functionalflora.Hellebore;
import mythicbotany.functionalflora.WitherAconite;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.infuser.BlockManaInfuser;
import mythicbotany.infuser.TileManaInfuser;
import mythicbotany.pylon.BlockAlfsteelPylon;
import mythicbotany.pylon.TileAlfsteelPylon;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {

    public static final BlockTE<TileManaInfuser> manaInfuser = new BlockManaInfuser(TileManaInfuser.class, Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockBase alfsteelBlock = new BlockBase(Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockTE<TileAlfsteelPylon> alfsteelPylon = new BlockAlfsteelPylon(Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).setLightLevel((s) -> 7));
    public static final BlockTE<TileManaCollector> manaCollector = new BlockTEManaHUD<>(TileManaCollector.class, Properties.create(Material.WOOD).hardnessAndResistance(5.5F));
    public static final BlockFunctionalFlower<Exoblaze> exoblaze = new BlockFunctionalFlower<>(Exoblaze.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<WitherAconite> witherAconite = new BlockFunctionalFlower<>(WitherAconite.class, Properties.create(Material.PLANTS), true);
    public static final BlockFunctionalFlower<Aquapanthus> aquapanthus = new BlockFunctionalFlower<>(Aquapanthus.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<Hellebore> hellebore = new BlockFunctionalFlower<>(Hellebore.class, Properties.create(Material.PLANTS), false);

    public static void register() {
        MythicBotany.register("mana_infuser", manaInfuser);
        MythicBotany.register("alfsteel_block", alfsteelBlock);
        MythicBotany.register("alfsteel_pylon", alfsteelPylon);
        MythicBotany.register("mana_collector", manaCollector);
        MythicBotany.register("exoblaze", exoblaze);
        MythicBotany.register("wither_aconite", witherAconite);
        MythicBotany.register("aquapanthus", aquapanthus);
        MythicBotany.register("hellebore", hellebore);
    }
}
