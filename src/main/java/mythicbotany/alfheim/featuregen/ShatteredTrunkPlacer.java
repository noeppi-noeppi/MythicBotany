package mythicbotany.alfheim.featuregen;

import com.mojang.serialization.Codec;
import org.moddingx.libx.annotation.api.Codecs;
import org.moddingx.libx.annotation.codec.PrimaryConstructor;
import org.moddingx.libx.annotation.registration.Reg.Exclude;
import org.moddingx.libx.annotation.registration.Reg.Name;
import org.moddingx.libx.annotation.registration.RegisterClass;
import mythicbotany.ModBlockTags;
import mythicbotany.MythicBotany;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

@RegisterClass
public class ShatteredTrunkPlacer extends TrunkPlacer {

    @Exclude public static final Codec<ShatteredTrunkPlacer> CODEC = Codecs.get(MythicBotany.class, ShatteredTrunkPlacer.class);
    @Name("shattered_trunk") public static final TrunkPlacerType<ShatteredTrunkPlacer> TYPE = new TrunkPlacerType<>(CODEC);

    @PrimaryConstructor
    public ShatteredTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Nonnull
    @Override
    protected TrunkPlacerType<?> type() {
        return TYPE;
    }

    @Nonnull
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(@Nonnull LevelSimulatedReader level, @Nonnull BiConsumer<BlockPos, BlockState> blockSetter, @Nonnull Random random, int freeTreeHeight, BlockPos pos, @Nonnull TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> list = new ArrayList<>();
        setDirtAt(level, blockSetter, random, pos.below(), config);
        for (Direction dir : Direction.values()) {
            if (dir.getAxis() != Direction.Axis.Y && random.nextInt(3) == 0) {
                BlockPos.MutableBlockPos mpos = pos.relative(Direction.UP).relative(dir).mutable();
                for (int i = 0; i < 3; i++) {
                    mpos.move(0, -1, 0);
                    if (!this.placeLog(level, blockSetter, mpos, random, config)) {
                        break;
                    }
                }
                if (level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::canOcclude)) {
                    setDirtAt(level, blockSetter, random, pos.below(), config);
                }
            }
        }
        int straightTrunkSize = Math.max(3, freeTreeHeight - 3);
        for (int i = 0; i < straightTrunkSize; i++) {
            this.placeLog(level, blockSetter, pos.above(i), random, config);
        }
        int shatters = 3 + random.nextInt(3);
        shatterLoop: for (int i = 0; i < shatters; i++) {
            int xd = random.nextInt(3);
            int zd = random.nextInt(3);
            int xs = random.nextBoolean() ? 1 : -1;
            int zs = random.nextBoolean() ? 1 : -1;
            BlockPos.MutableBlockPos mpos = pos.above(straightTrunkSize - 1).mutable();
            int shatterSize = 3 + random.nextInt(4);
            for (int j = 0; j < shatterSize; j++) {
                int xm = xd == 0 ? 0 : (xd / 2) + random.nextInt((xd + 1) / 2);
                int zm = zd == 0 ? 0 : (zd / 2) + random.nextInt((zd + 1) / 2);
                int x = 0;
                int z = 0;
                while (x < xm || z < zm) {
                    mpos.move(x < xm ? xs : 0, random.nextInt(3) != 0 ? 1 : 0, z < zm ? zs : 0);
                    x = Math.min(xm, x + 1);
                    z = Math.min(zm, z + 1);
                    if (this.placeLog(level, blockSetter, mpos, random, config)) {
                        list.add(new FoliagePlacer.FoliageAttachment(mpos.immutable(), 0, false));
                    } else {
                        continue shatterLoop;
                    }
                }
            }
        }
        return list;
    }

    private boolean placeLog(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, BlockPos pos, Random rand, TreeConfiguration config) {
        if (level.isStateAtPosition(pos, state -> state.isAir() || state.is(ModBlockTags.ALFHEIM_LEAVES) || state.getMaterial() == Material.REPLACEABLE_PLANT || state.is(Blocks.WATER))) {
            blockSetter.accept(pos, config.trunkProvider.getState(rand, pos));
            return true;
        } else {
            return false;
        }
    }
    
    public int getBaseHeight() {
        return this.baseHeight;
    }
    
    public int getHeightRandA() {
        return this.heightRandA;
    }
    
    public int getHeightRandB() {
        return this.heightRandB;
    }
}
