package mythicbotany.alfheim;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.ModBlockTags;
import mythicbotany.MythicBotany;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ShatteredTrunkPlacer extends TrunkPlacer {

    public static final TrunkPlacerType<ShatteredTrunkPlacer> SHATTERED_TRUNK = new TrunkPlacerType<>(RecordCodecBuilder.create((instance) -> trunkPlacerParts(instance).apply(instance, ShatteredTrunkPlacer::new)));

    static {
        // Must be here in a static init block or the game will fail to load worlds
        // I don't understand why but it works.
        Registry.register(Registry.TRUNK_PLACER_TYPES, new ResourceLocation(MythicBotany.getInstance().modid, "shattered_trunk"), SHATTERED_TRUNK);
    }

    public ShatteredTrunkPlacer(int height, int max, int min) {
        super(height, max, min);
    }

    @Nonnull
    @Override
    protected TrunkPlacerType<?> type() {
        return SHATTERED_TRUNK;
    }
    
    @Nonnull
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(@Nonnull LevelSimulatedRW level, @Nonnull Random rand, int height, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> positions, @Nonnull BoundingBox box, @Nonnull TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> list = new ArrayList<>();
        setDirtAt(level, pos.below());
        for (Direction dir : Direction.values()) {
            if (dir.getAxis() != Direction.Axis.Y && rand.nextInt(3) == 0) {
                BlockPos.MutableBlockPos mpos = pos.relative(Direction.UP).relative(dir).mutable();
                for (int i = 0; i < 3; i++) {
                    mpos.move(0, -1, 0);
                    if (!placeLog(level, mpos, rand, box, config)) {
                        break;
                    }
                }
                if (level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::canOcclude)) {
                    setDirtAt(level, pos.below());
                }
            }
        }
        int straightTrunkSize = Math.max(3, height - 3);
        for (int i = 0; i < straightTrunkSize; i++) {
            placeLog(level, pos.above(i), rand, box, config);
        }
        int shatters = 3 + rand.nextInt(3);
        shatterLoop: for (int i = 0; i < shatters; i++) {
            int xd = rand.nextInt(3);
            int zd = rand.nextInt(3);
            int xs = rand.nextBoolean() ? 1 : -1;
            int zs = rand.nextBoolean() ? 1 : -1;
            BlockPos.MutableBlockPos mpos = pos.above(straightTrunkSize - 1).mutable();
            int shatterSize = 3 + rand.nextInt(4);
            for (int j = 0; j < shatterSize; j++) {
                int xm = xd == 0 ? 0 : (xd / 2) + rand.nextInt((xd + 1) / 2);
                int zm = zd == 0 ? 0 : (zd / 2) + rand.nextInt((zd + 1) / 2);
                int x = 0;
                int z = 0;
                while (x < xm || z < zm) {
                    mpos.move(x < xm ? xs : 0, rand.nextInt(3) != 0 ? 1 : 0, z < zm ? zs : 0);
                    x = Math.min(xm, x + 1);
                    z = Math.min(zm, z + 1);
                    if (placeLog(level, mpos, rand, box, config)) {
                        list.add(new FoliagePlacer.FoliageAttachment(mpos.immutable(), 0, false));
                    } else {
                        continue shatterLoop;
                    }
                }
            }
        }
        return list;
    }

    private boolean placeLog(LevelSimulatedRW level, BlockPos pos, Random rand, BoundingBox box, TreeConfiguration config) {
        if (level.isStateAtPosition(pos, state -> {
            //noinspection deprecation
            return state.isAir() || state.is(ModBlockTags.ALFHEIM_LEAVES) || state.getMaterial() == Material.REPLACEABLE_PLANT || state.is(Blocks.WATER);
        })) {
            setBlock(level, pos, config.trunkProvider.getState(rand, pos), box);
            return true;
        } else {
            return false;
        }
    }
}
