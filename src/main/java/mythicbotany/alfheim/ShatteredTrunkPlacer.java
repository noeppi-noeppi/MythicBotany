package mythicbotany.alfheim;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.ModBlockTags;
import mythicbotany.MythicBotany;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ShatteredTrunkPlacer extends AbstractTrunkPlacer {

    public static final TrunkPlacerType<ShatteredTrunkPlacer> SHATTERED_TRUNK = new TrunkPlacerType<>(RecordCodecBuilder.create((p_236897_0_) -> func_236915_a_(p_236897_0_).apply(p_236897_0_, ShatteredTrunkPlacer::new)));

    static {
        // Must be here in a static init block or the game will fail to load worlds
        // I don't understand why but it works.
        Registry.register(Registry.TRUNK_REPLACER, new ResourceLocation(MythicBotany.getInstance().modid, "shattered_trunk"), SHATTERED_TRUNK);
    }

    public ShatteredTrunkPlacer(int height, int max, int min) {
        super(height, max, min);
    }

    @Nonnull
    @Override
    protected TrunkPlacerType<?> func_230381_a_() {
        return SHATTERED_TRUNK;
    }

    @Nonnull
    @Override
    public List<FoliagePlacer.Foliage> func_230382_a_(@Nonnull IWorldGenerationReader world, @Nonnull Random rand, int height, @Nonnull BlockPos pos, @Nonnull Set<BlockPos> positions, @Nonnull MutableBoundingBox box, @Nonnull BaseTreeFeatureConfig config) {
        List<FoliagePlacer.Foliage> list = new ArrayList<>();
        func_236909_a_(world, pos.down());
        for (Direction dir : Direction.values()) {
            if (dir.getAxis() != Direction.Axis.Y && rand.nextInt(3) == 0) {
                BlockPos.Mutable mpos = pos.offset(Direction.UP).offset(dir).toMutable();
                for (int i = 0; i < 3; i++) {
                    mpos.move(0, -1, 0);
                    if (!placeLog(world, mpos, rand, box, config)) {
                        break;
                    }
                }
                if (world.hasBlockState(pos, AbstractBlock.AbstractBlockState::isSolid)) {
                    func_236909_a_(world, pos.down());
                }
            }
        }
        int straightTrunkSize = Math.max(3, height - 3);
        for (int i = 0; i < straightTrunkSize; i++) {
            placeLog(world, pos.up(i), rand, box, config);
        }
        int shatters = 3 + rand.nextInt(3);
        shatterLoop: for (int i = 0; i < shatters; i++) {
            int xd = rand.nextInt(3);
            int zd = rand.nextInt(3);
            int xs = rand.nextBoolean() ? 1 : -1;
            int zs = rand.nextBoolean() ? 1 : -1;
            BlockPos.Mutable mpos = pos.up(straightTrunkSize - 1).toMutable();
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
                    if (placeLog(world, mpos, rand, box, config)) {
                        list.add(new FoliagePlacer.Foliage(mpos.toImmutable(), 0, false));
                    } else {
                        continue shatterLoop;
                    }
                }
            }
        }
        return list;
    }

    private boolean placeLog(IWorldGenerationReader world, BlockPos pos, Random rand, MutableBoundingBox box, BaseTreeFeatureConfig config) {
        if (world.hasBlockState(pos, state -> {
            //noinspection deprecation
            return state.isAir() || state.isIn(ModBlockTags.ALFHEIM_LEAVES) || state.getMaterial() == Material.TALL_PLANTS || state.isIn(Blocks.WATER);
        })) {
            func_236913_a_(world, pos, config.trunkProvider.getBlockState(rand, pos), box);
            return true;
        } else {
            return false;
        }
    }
}
