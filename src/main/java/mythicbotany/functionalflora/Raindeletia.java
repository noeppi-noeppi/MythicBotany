package mythicbotany.functionalflora;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.common.block.ModBlocks;

public class Raindeletia extends FunctionalFlowerBase {

    public Raindeletia(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 0x1E1CD8, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!world.isRemote) {
            float multiplier = 0;
            if (world.isRainingAt(pos)) {
                multiplier = world.isThundering() ? MythicConfig.flowers.raindeletia.thunder : MythicConfig.flowers.raindeletia.rain;
            }
            if (world.getBlockState(pos.down()).getBlock() == ModBlocks.enchantedSoil) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.enchanted_soil;
            } else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.vividGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.vivid_grass;
            }  else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.dryGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.dry_grass;
            }

            int manaTransfer = Math.round(multiplier * MythicConfig.flowers.raindeletia.base);
            mana = MathHelper.clamp(mana + manaTransfer, 0, maxMana);
            didWork = true;
        }
    }
}
