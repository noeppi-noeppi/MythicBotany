package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import io.github.noeppi_noeppi.libx.mod.registration.BlockBase;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import mythicbotany.alfheim.BlockAlfheimLeaves;
import mythicbotany.alfheim.teleporter.BlockReturnPortal;
import mythicbotany.alfheim.teleporter.TileReturnPortal;
import mythicbotany.base.BlockTEManaHUD;
import mythicbotany.collector.TileManaCollector;
import mythicbotany.functionalflora.*;
import mythicbotany.functionalflora.base.BlockFunctionalFlower;
import mythicbotany.infuser.BlockManaInfuser;
import mythicbotany.infuser.TileManaInfuser;
import mythicbotany.mimir.BlockYggdrasilBranch;
import mythicbotany.mimir.TileYggdrasilBranch;
import mythicbotany.mjoellnir.BlockMjoellnir;
import mythicbotany.pylon.BlockAlfsteelPylon;
import mythicbotany.pylon.TileAlfsteelPylon;
import mythicbotany.rune.BlockMasterRuneHolder;
import mythicbotany.rune.BlockRuneHolder;
import mythicbotany.rune.TileMasterRuneHolder;
import mythicbotany.rune.TileRuneHolder;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

@RegisterClass
public class ModBlocks {

    public static final BlockTE<TileManaInfuser> manaInfuser = new BlockManaInfuser(MythicBotany.getInstance(), TileManaInfuser.class, Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockBase alfsteelBlock = new BlockBase(MythicBotany.getInstance(), Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockTE<TileAlfsteelPylon> alfsteelPylon = new BlockAlfsteelPylon(MythicBotany.getInstance(), Properties.create(Material.IRON).hardnessAndResistance(5.5f).sound(SoundType.METAL).setLightLevel((s) -> 7));
    public static final BlockTE<TileManaCollector> manaCollector = new BlockTEManaHUD<>(MythicBotany.getInstance(), TileManaCollector.class, Properties.create(Material.WOOD).hardnessAndResistance(5));
    public static final BlockFunctionalFlower<Exoblaze> exoblaze = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Exoblaze.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<WitherAconite> witherAconite = new BlockFunctionalFlower<>(MythicBotany.getInstance(), WitherAconite.class, Properties.create(Material.PLANTS), true);
    public static final BlockFunctionalFlower<Aquapanthus> aquapanthus = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Aquapanthus.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<Hellebore> hellebore = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Hellebore.class, Properties.create(Material.PLANTS), false);
    public static final BlockFunctionalFlower<Raindeletia> raindeletia = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Raindeletia.class, Properties.create(Material.PLANTS), true);
    public static final BlockTE<TileYggdrasilBranch> yggdrasilBranch = new BlockYggdrasilBranch(MythicBotany.getInstance(), Properties.create(Material.WOOD).hardnessAndResistance(4).sound(SoundType.WOOD));
    public static final BlockTE<TileRuneHolder> runeHolder = new BlockRuneHolder<>(MythicBotany.getInstance(), TileRuneHolder.class, Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL));
    public static final BlockTE<TileMasterRuneHolder> masterRuneHolder = new BlockMasterRuneHolder(MythicBotany.getInstance(), Properties.create(Material.IRON).hardnessAndResistance(3).sound(SoundType.METAL));
    public static final BlockMjoellnir mjoellnir = new BlockMjoellnir(Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops(), new Item.Properties().maxStackSize(1));
    public static final Block dreamwoodLeaves = new BlockAlfheimLeaves(MythicBotany.getInstance());
    public static final BlockBase elementiumOre = new BlockBase(MythicBotany.getInstance(), AbstractBlock.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().hardnessAndResistance(4, 4));
    public static final BlockBase dragonstoneOre = new BlockBase(MythicBotany.getInstance(),AbstractBlock.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).setRequiresTool().hardnessAndResistance(5, 5));
    public static final BlockBase goldOre = new BlockBase(MythicBotany.getInstance(), AbstractBlock.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).setRequiresTool().hardnessAndResistance(3, 3));
    public static final BlockTE<TileReturnPortal> returnPortal = new BlockReturnPortal(MythicBotany.getInstance(), AbstractBlock.Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F).sound(SoundType.GLASS).setLightLevel(state -> 4));
}
