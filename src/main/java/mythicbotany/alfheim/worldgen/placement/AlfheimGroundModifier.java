package mythicbotany.alfheim.worldgen.placement;

import com.mojang.serialization.Codec;
import mythicbotany.alfheim.worldgen.AlfheimWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class AlfheimGroundModifier extends PlacementModifier {
    
    public static final AlfheimGroundModifier INSTANCE = new AlfheimGroundModifier();
    public static final Codec<AlfheimGroundModifier> CODEC = Codec.unit(INSTANCE);
    public static final PlacementModifierType<AlfheimGroundModifier> TYPE = () -> CODEC;
    
    private AlfheimGroundModifier() {
        
    }

    @Nonnull
    @Override
    public PlacementModifierType<?> type() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Stream<BlockPos> getPositions(@Nonnull PlacementContext context, @Nonnull RandomSource random, @Nonnull BlockPos pos) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos.getX(), context.getMinBuildHeight() + context.getGenDepth(), pos.getZ());
        while (mpos.getY() >= context.getMinBuildHeight()) {
            if (!AlfheimWorldGen.passthrough(context.getLevel().getBlockState(mpos))) {
                return Stream.of(mpos.immutable().relative(Direction.UP));
            }
            mpos.move(Direction.DOWN);
        }
        return Stream.of();
    }
}
