package mythicbotany.functionalflora;

import mythicbotany.config.MythicConfig;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.common.block.ModBlocks;

public class Raindeletia extends FunctionalFlowerBase {

    public Raindeletia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0x1E1CD8, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            float multiplier = 0;
            if (this.level.isRainingAt(this.worldPosition)) {
                multiplier = this.level.isThundering() ? MythicConfig.flowers.raindeletia.thunder : MythicConfig.flowers.raindeletia.rain;
            }
            if (this.level.getBlockState(this.worldPosition.below()).getBlock() == ModBlocks.enchantedSoil) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.enchanted_soil;
            } else if (this.level.getBlockState(this.worldPosition.below()).getBlock() == ModBlocks.vividGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.vivid_grass;
            }  else if (this.level.getBlockState(this.worldPosition.below()).getBlock() == ModBlocks.dryGrass) {
                multiplier = multiplier * MythicConfig.flowers.raindeletia.dry_grass;
            }

            int manaTransfer = Math.round(multiplier * MythicConfig.flowers.raindeletia.base);
            this.mana = Mth.clamp(this.mana + manaTransfer, 0, this.maxMana);
            this.didWork = true;
        }
    }
}
