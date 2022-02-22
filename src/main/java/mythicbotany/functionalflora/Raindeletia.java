package mythicbotany.functionalflora;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.common.block.ModBlocks;

public class Raindeletia extends FunctionalFlowerBase {

    public Raindeletia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0x1E1CD8, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            float multiplier = 0;
            if (level.isRainingAt(worldPosition)) {
                multiplier = level.isThundering() ? MythicConfig.flowers.raindeletia.thunder : MythicConfig.flowers.raindeletia.rain;
            }
            if (level.getBlockState(worldPosition.below()).getBlock() == ModBlocks.enchantedSoil) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.enchanted_soil;
            } else if (level.getBlockState(worldPosition.below()).getBlock() == ModBlocks.vividGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.vivid_grass;
            }  else if (level.getBlockState(worldPosition.below()).getBlock() == ModBlocks.dryGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.dry_grass;
            }

            int manaTransfer = Math.round(multiplier * MythicConfig.flowers.raindeletia.base);
            mana = Mth.clamp(mana + manaTransfer, 0, maxMana);
            didWork = true;
        }
    }
}
