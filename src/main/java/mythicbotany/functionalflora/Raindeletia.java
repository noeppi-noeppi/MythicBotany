package mythicbotany.functionalflora;

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
                multiplier = world.isThundering() ? 3 : 0.01f;
            }
            if (world.getBlockState(pos.down()).getBlock() == ModBlocks.enchantedSoil) {
                multiplier = multiplier * 5;
            } else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.vividGrass) {
                multiplier = multiplier * 2f;
            }  else if (world.getBlockState(pos.down()).getBlock() == ModBlocks.dryGrass) {
                multiplier = multiplier * 0.5f;
            }

            int manaTransfer = Math.round(multiplier * 5);
            mana = MathHelper.clamp(mana + manaTransfer, 0, maxMana);
            didWork = true;
        }
    }
}
