package mythicbotany;

import mythicbotany.base.BlockBase;
import mythicbotany.base.BlockTE;
import mythicbotany.infuser.BlockManaInfuser;
import mythicbotany.infuser.TileManaInfuser;
import mythicbotany.pylon.BlockAlfsteelPylon;
import mythicbotany.pylon.TileAlfsteelPylon;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import vazkii.botania.common.block.BlockPylon;

public class ModBlocks {

    public static final BlockTE<TileManaInfuser> manaInfuser = new BlockManaInfuser(TileManaInfuser.class, Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockBase alfsteelBlock = new BlockBase(Properties.create(Material.IRON).hardnessAndResistance(20));
    public static final BlockTE<TileAlfsteelPylon> alfsteelPylon = new BlockAlfsteelPylon(Properties.create(Material.IRON).hardnessAndResistance(5.5F).sound(SoundType.METAL).setLightLevel((s) -> 7));

    public static void register() {
        MythicBotany.register("mana_infuser", manaInfuser);
        MythicBotany.register("alfsteel_block", alfsteelBlock);
        MythicBotany.register("alfsteel_pylon", alfsteelPylon);
    }
}
