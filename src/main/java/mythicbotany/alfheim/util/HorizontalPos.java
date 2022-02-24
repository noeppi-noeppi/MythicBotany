package mythicbotany.alfheim.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.function.Predicate;

public record HorizontalPos(int x, int z) {
    
    public BlockPos atY(int y) {
        return new BlockPos(this.x(), y, this.z());
    }
    
    public BlockPos trace(int fromY, int toY, Predicate<BlockPos> match) {
        if (fromY == toY) return this.atY(fromY);
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(this.x(), fromY, this.z());
        while (toY > fromY ? mpos.getY() <= toY : mpos.getY() >= toY) {
            if (match.test(mpos)) {
                return mpos.immutable().relative(toY > fromY ? Direction.DOWN : Direction.UP);
            }
            mpos.move(toY > fromY ? Direction.UP : Direction.DOWN);
        }
        return this.atY(toY);
    }
    
    public HorizontalPos offset(int xd, int zd) {
        if (xd == 0 && zd == 0) return this;
        return new HorizontalPos(this.x() + xd, this.z() + zd);
    }
    
    public HorizontalPos offset(Direction direction) {
        return this.offset(direction.getStepX(), direction.getStepZ());
    }
    
    public static HorizontalPos from(BlockPos pos) {
        return new HorizontalPos(pos.getX(), pos.getZ());
    }
}
