package mythicbotany.alfheim.featuregen;

import com.mojang.serialization.Codec;
import org.moddingx.libx.annotation.api.Codecs;
import org.moddingx.libx.annotation.codec.PrimaryConstructor;
import org.moddingx.libx.annotation.registration.Reg.Exclude;
import org.moddingx.libx.annotation.registration.Reg.Name;
import org.moddingx.libx.annotation.registration.RegisterClass;
import mythicbotany.MythicBotany;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.BiConsumer;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

@RegisterClass
public class RandomFoliagePlacer extends FoliagePlacer {

    @Exclude public static final Codec<RandomFoliagePlacer> CODEC = Codecs.get(MythicBotany.class, RandomFoliagePlacer.class);
    @Name("random_foliage") public static final FoliagePlacerType<RandomFoliagePlacer> TYPE = new FoliagePlacerType<>(CODEC);

    @PrimaryConstructor
    public RandomFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }
    
    @Nonnull
    @Override
    protected FoliagePlacerType<?> type() {
        return TYPE;
    }

    @Override
    protected void createFoliage(@Nonnull LevelSimulatedReader level, @Nonnull BiConsumer<BlockPos, BlockState> blockSetter, Random random, @Nonnull TreeConfiguration config, int maxFreeTreeHeight, @Nonnull FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int leaves = 3 + random.nextInt(3);
        for (int i = 0; i < leaves; i++) {
            int x = random.nextInt(5) - 2;
            int y = random.nextInt(5) - 2;
            int z = random.nextInt(5) - 2;
            if (this.placeLeave(level, blockSetter, attachment.pos().offset(x, y, z), random, config)) {
                int nx = x == 0 ? 0 : x > 0 ? x - 1 : x + 1;
                int ny = y == 0 ? 0 : y > 0 ? y - 1 : y + 1;
                int nz = z == 0 ? 0 : z > 0 ? z - 1 : z + 1;
                this.placeLeave(level, blockSetter, attachment.pos().offset(nx, ny, nz), random, config);
            }
        }
    }

    @Override
    public int foliageHeight(@Nonnull Random random, int height, @Nonnull TreeConfiguration config) {
        return 3;
    }

    @Override
    protected boolean shouldSkipLocation(@Nonnull Random random, int localX, int localY, int localZ, int range, boolean large) {
        return localX == range && localZ == range && (random.nextInt(2) == 0 || localY == 0);
    }

    private boolean placeLeave(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, BlockPos pos, Random rand, TreeConfiguration config) {
        if (level.isStateAtPosition(pos, state -> state.isAir() || state.getMaterial() == Material.REPLACEABLE_PLANT || state.is(Blocks.WATER))) {
            blockSetter.accept(pos, config.foliageProvider.getState(rand, pos));
            return true;
        } else {
            return false;
        }
    }
    
    public IntProvider getRadius() {
        return this.radius;
    }
    
    public IntProvider getOffset() {
        return this.offset;
    }
}
