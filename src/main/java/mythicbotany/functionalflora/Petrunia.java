package mythicbotany.functionalflora;

import io.github.noeppi_noeppi.libx.util.NBTX;
import mythicbotany.ModBlocks;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import mythicbotany.rune.TileMasterRuneHolder;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Petrunia extends FunctionalFlowerBase {
    
    @Nullable
    private BlockPos currentPos = null;
    private int storedMana = 0;
    
    public Petrunia(TileEntityType<?> tileEntityType) {
        super(tileEntityType, 0xB71A1A, false);
    }
    
    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.world.isRemote) {
            if (currentPos == null) {
                find: for (int x = -3; x <= 3; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -3; z <= 3; z++) {
                            BlockPos pos = this.pos.add(x, y, z);
                            if (this.world.getBlockState(pos).getBlock() == ModBlocks.masterRuneHolder) {
                                this.currentPos = pos.toImmutable();
                                break find;
                            }
                        }
                    }
                }
            }
            if (this.currentPos != null) {
                BlockState state = this.world.getBlockState(this.currentPos);
                TileEntity te = this.world.getTileEntity(this.currentPos);
                if (state.getBlock() != ModBlocks.masterRuneHolder || !(te instanceof TileMasterRuneHolder)) {
                    this.currentPos = null;
                } else {
                    TileMasterRuneHolder tile = (TileMasterRuneHolder) te;
                    tile.tryStartRitual(
                            msg -> {},
                            m -> {
                                if (this.storedMana >= m) {
                                    return true;
                                } else {
                                    int transfer = Math.min(Math.max(0, m - this.storedMana), this.mana);
                                    this.storedMana += transfer;
                                    this.mana -= transfer;
                                    return this.storedMana >= m;
                                }
                            },
                            m -> {
                                this.storedMana = Math.max(0, this.storedMana - m);
                                this.markDirty();
                            }
                    );
                }
            }
        }
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.storedMana = nbt.getInt("StoredMana");
        this.currentPos = NBTX.getPos(nbt, "CurrentRuneTargetPos");
    }

    @Nonnull
    @Override
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.putInt("StoredMana", this.storedMana);
        if (this.currentPos != null) NBTX.putPos(nbt, "CurrentRuneTargetPos", this.currentPos);
        return super.write(nbt);
    }
}
