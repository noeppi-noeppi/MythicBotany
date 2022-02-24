package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import io.github.noeppi_noeppi.libx.base.BlockBase;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import mythicbotany.alfheim.content.BlockAlfheimLeaves;
import mythicbotany.alfheim.teleporter.BlockReturnPortal;
import mythicbotany.alfheim.teleporter.TileReturnPortal;
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
import mythicbotany.rune.BlockCentralRuneHolder;
import mythicbotany.rune.BlockRuneHolder;
import mythicbotany.rune.TileCentralRuneHolder;
import mythicbotany.rune.TileRuneHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;

@RegisterClass
public class ModBlocks {

    public static final BlockBE<TileManaInfuser> manaInfuser = new BlockManaInfuser(MythicBotany.getInstance(), TileManaInfuser.class, Properties.of(Material.METAL).strength(20));
    public static final BlockBase alfsteelBlock = new BlockBase(MythicBotany.getInstance(), Properties.of(Material.METAL).strength(20).requiresCorrectToolForDrops());
    public static final BlockBE<TileAlfsteelPylon> alfsteelPylon = new BlockAlfsteelPylon(MythicBotany.getInstance(), Properties.of(Material.METAL).strength(5.5f).sound(SoundType.METAL).lightLevel((s) -> 7));
    public static final BlockBE<TileManaCollector> manaCollector = new BlockBE<>(MythicBotany.getInstance(), TileManaCollector.class, Properties.of(Material.WOOD).strength(5));
    public static final BlockFunctionalFlower<Exoblaze> exoblaze = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Exoblaze.class, Properties.of(Material.PLANT), false);
    public static final BlockFunctionalFlower<WitherAconite> witherAconite = new BlockFunctionalFlower<>(MythicBotany.getInstance(), WitherAconite.class, Properties.of(Material.PLANT), true);
    public static final BlockFunctionalFlower<Aquapanthus> aquapanthus = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Aquapanthus.class, Properties.of(Material.PLANT), false);
    public static final BlockFunctionalFlower<Hellebore> hellebore = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Hellebore.class, Properties.of(Material.PLANT), false);
    public static final BlockFunctionalFlower<Raindeletia> raindeletia = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Raindeletia.class, Properties.of(Material.PLANT), true);
    public static final BlockFunctionalFlower<Feysythia> feysythia = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Feysythia.class, Properties.of(Material.PLANT), true);
    public static final BlockFunctionalFlower<Petrunia> petrunia = new BlockFunctionalFlower<>(MythicBotany.getInstance(), Petrunia.class, Properties.of(Material.PLANT), false);
    public static final BlockBE<TileYggdrasilBranch> yggdrasilBranch = new BlockYggdrasilBranch(MythicBotany.getInstance(), Properties.of(Material.WOOD).strength(4).sound(SoundType.WOOD));
    public static final BlockBE<TileRuneHolder> runeHolder = new BlockRuneHolder<>(MythicBotany.getInstance(), TileRuneHolder.class, Properties.of(Material.METAL).strength(3).sound(SoundType.METAL));
    public static final BlockBE<TileCentralRuneHolder> centralRuneHolder = new BlockCentralRuneHolder(MythicBotany.getInstance(), Properties.of(Material.METAL).strength(3).sound(SoundType.METAL));
    public static final BlockMjoellnir mjoellnir = new BlockMjoellnir(MythicBotany.getInstance(), Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static final Block dreamwoodLeaves = new BlockAlfheimLeaves(MythicBotany.getInstance());
    // TODO tool values
    public static final BlockBase elementiumOre = new BlockBase(MythicBotany.getInstance(), BlockBehaviour.Properties.of(Material.STONE).strength(4, 4).requiresCorrectToolForDrops());
    public static final BlockBase dragonstoneOre = new BlockBase(MythicBotany.getInstance(),BlockBehaviour.Properties.of(Material.STONE).strength(5, 5).requiresCorrectToolForDrops());
    public static final BlockBase goldOre = new BlockBase(MythicBotany.getInstance(), BlockBehaviour.Properties.of(Material.STONE).strength(3, 3).requiresCorrectToolForDrops());
    public static final BlockBase rawElementiumBlock = new BlockBase(MythicBotany.getInstance(), Properties.copy(Blocks.RAW_IRON_BLOCK).requiresCorrectToolForDrops());
    public static final BlockBE<TileReturnPortal> returnPortal = new BlockReturnPortal(MythicBotany.getInstance(), BlockBehaviour.Properties.of(Material.PORTAL).noCollission().noDrops().strength(-1.0F).sound(SoundType.GLASS).lightLevel(state -> 4));
}
