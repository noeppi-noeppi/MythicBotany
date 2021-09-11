package mythicbotany.functionalflora;

import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.RadiusDescriptor;

public class Exoblaze extends FunctionalFlowerBase {

    public static final int MAX_TICK_TO_NEXT_CHECK = 10;
    public static final int MANA_PER_BREW = 50;

    private transient int tickToNextCheck = 0;

    public Exoblaze(TileEntityType<?> tileEntityType) {
        super(tileEntityType, 0xFFFF22, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!world.isRemote) {
            if (tickToNextCheck > 0) {
                tickToNextCheck -= 1;
                return;
            }
            tickToNextCheck = MAX_TICK_TO_NEXT_CHECK;
            BlockPos basePos = pos.toImmutable();
            outer:
            for (int xd = -3; xd <= 3; xd++) {
                for (int yd = -1; yd <= 1; yd++) {
                    for (int zd = -3; zd <= 3; zd++) {
                        if (mana < MANA_PER_BREW)
                            break outer;
                        TileEntity te = world.getTileEntity(basePos.add(xd, yd, zd));
                        if (te instanceof BrewingStandTileEntity && ((BrewingStandTileEntity) te).fuel < 20) {
                            mana -= MANA_PER_BREW;
                            ((BrewingStandTileEntity) te).fuel += 1;
                            didWork = true;
                            markDirty();
                            te.markDirty();
                        }
                    }
                }
            }
        }
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(pos, 3);
    }
}
