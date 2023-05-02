package mythicbotany.functionalflora;

import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.block_entity.RadiusDescriptor;

public class Exoblaze extends FunctionalFlowerBase {

    public static final int MAX_TICK_TO_NEXT_CHECK = 10;
    public static final int MANA_PER_BREW = 50;

    private transient int tickToNextCheck = 0;

    public Exoblaze(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0xFFFF22, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            if (this.tickToNextCheck > 0) {
                this.tickToNextCheck -= 1;
                return;
            }
            this.tickToNextCheck = MAX_TICK_TO_NEXT_CHECK;
            BlockPos basePos = this.worldPosition.immutable();
            outer:
            for (int xd = -3; xd <= 3; xd++) {
                for (int yd = -1; yd <= 1; yd++) {
                    for (int zd = -3; zd <= 3; zd++) {
                        if (this.mana < MANA_PER_BREW)
                            break outer;
                        BlockEntity te = this.level.getBlockEntity(basePos.offset(xd, yd, zd));
                        if (te instanceof BrewingStandBlockEntity && ((BrewingStandBlockEntity) te).fuel < 20) {
                            this.mana -= MANA_PER_BREW;
                            ((BrewingStandBlockEntity) te).fuel += 1;
                            this.didWork = true;
                            this.setChanged();
                            te.setChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(this.worldPosition, 3);
    }
}
