package mythicbotany;

import io.github.noeppi_noeppi.libx.mod.registration.BlockBase;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import mythicbotany.base.BlockTEManaHUD;
import mythicbotany.collector.TileManaCollector;
import mythicbotany.functionalflora.*;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.infuser.BlockManaInfuser;
import mythicbotany.infuser.TileManaInfuser;
import mythicbotany.pylon.BlockAlfsteelPylon;
import mythicbotany.pylon.TileAlfsteelPylon;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ModBlocks {

    public static final BlockTE<TileManaInfuser> manaInfuser = new BlockManaInfuser(MythicBotany.getInstance(), TileManaInfuser.class, Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockBase alfsteelBlock = new BlockBase(MythicBotany.getInstance(), Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockTE<TileAlfsteelPylon> alfsteelPylon = new BlockAlfsteelPylon(MythicBotany.getInstance(), Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).setLightLevel((s) -> 7));
    public static final BlockTE<TileManaCollector> manaCollector = new BlockTEManaHUD<>(MythicBotany.getInstance(), TileManaCollector.class, Properties.create(Material.WOOD).hardnessAndResistance(5.5F));
    public static final BlockFunctionalFlower<Exoblaze> exoblaze = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Exoblaze.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<WitherAconite> witherAconite = new BlockFunctionalFlower<>(MythicBotany.getInstance(), WitherAconite.class, Properties.create(Material.PLANTS), true);
    public static final BlockFunctionalFlower<Aquapanthus> aquapanthus = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Aquapanthus.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<Hellebore> hellebore = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Hellebore.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<Raindeletia> raindeletia = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Raindeletia.class, Properties.create(Material.PLANTS), true);

    public static void register() {
        MythicBotany.getInstance().register("mana_infuser", manaInfuser);
        MythicBotany.getInstance().register("alfsteel_block", alfsteelBlock);
        MythicBotany.getInstance().register("alfsteel_pylon", alfsteelPylon);
        MythicBotany.getInstance().register("mana_collector", manaCollector);
        MythicBotany.getInstance().register("exoblaze", exoblaze);
        MythicBotany.getInstance().register("wither_aconite", witherAconite);
        MythicBotany.getInstance().register("aquapanthus", aquapanthus);
        MythicBotany.getInstance().register("hellebore", hellebore);
        MythicBotany.getInstance().register("raindeletia", raindeletia);
    }
}
