package mythicbotany.functionalflora;

import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.subtile.RadiusDescriptor;

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
        if (!level.isClientSide) {
            if (tickToNextCheck > 0) {
                tickToNextCheck -= 1;
                return;
            }
            tickToNextCheck = MAX_TICK_TO_NEXT_CHECK;
            BlockPos basePos = worldPosition.immutable();
            outer:
            for (int xd = -3; xd <= 3; xd++) {
                for (int yd = -1; yd <= 1; yd++) {
                    for (int zd = -3; zd <= 3; zd++) {
                        if (mana < MANA_PER_BREW)
                            break outer;
                        BlockEntity te = level.getBlockEntity(basePos.offset(xd, yd, zd));
                        if (te instanceof BrewingStandBlockEntity && ((BrewingStandBlockEntity) te).fuel < 20) {
                            mana -= MANA_PER_BREW;
                            ((BrewingStandBlockEntity) te).fuel += 1;
                            didWork = true;
                            setChanged();
                            te.setChanged();
                        }
                    }
                }
            }
        }
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(worldPosition, 3);
    }
}
