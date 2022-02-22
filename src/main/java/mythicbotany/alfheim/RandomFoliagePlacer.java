package mythicbotany.alfheim;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.MythicBotany;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer.FoliageAttachment;

public class RandomFoliagePlacer extends FoliagePlacer {

    public static final FoliagePlacerType<RandomFoliagePlacer> RANDOM_FOLIAGE = new FoliagePlacerType<>(RecordCodecBuilder.create((instance) -> foliagePlacerParts(instance).apply(instance, RandomFoliagePlacer::new)));

    static {
        RANDOM_FOLIAGE.setRegistryName(new ResourceLocation(MythicBotany.getInstance().modid, "random_foliage"));
        ForgeRegistries.FOLIAGE_PLACER_TYPES.register(RANDOM_FOLIAGE);
    }

    public RandomFoliagePlacer(UniformInt spread1, UniformInt spread2) {
        super(spread1, spread2);
    }
    
    @Nonnull
    @Override
    protected FoliagePlacerType<?> type() {
        return RANDOM_FOLIAGE;
    }
    
    @Override
    protected void createFoliage(@Nonnull LevelSimulatedRW level, @Nonnull Random rand, @Nonnull TreeConfiguration config, int p_230372_4_, @Nonnull FoliageAttachment foliage, int p_230372_6_, int p_230372_7_, @Nonnull Set<BlockPos> p_230372_8_, int p_230372_9_, @Nonnull BoundingBox box) {
        int leaves = 3 + rand.nextInt(3);
        for (int i = 0; i < leaves; i++) {
            int x = rand.nextInt(5) - 2;
            int y = rand.nextInt(5) - 2;
            int z = rand.nextInt(5) - 2;
            if (placeLeave(level, foliage.foliagePos().offset(x, y, z), rand, box, config)) {
                int nx = x == 0 ? 0 : x > 0 ? x - 1 : x + 1;
                int ny = y == 0 ? 0 : y > 0 ? y - 1 : y + 1;
                int nz = z == 0 ? 0 : z > 0 ? z - 1 : z + 1;
                placeLeave(level, foliage.foliagePos().offset(nx, ny, nz), rand, box, config);
            }
        }
    }

    @Override
    public int foliageHeight(@Nonnull Random random, int height, @Nonnull TreeConfiguration config) {
        return 3;
    }

    @Override
    protected boolean shouldSkipLocation(@Nonnull Random random, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
        return p_230373_2_ == p_230373_5_ && p_230373_4_ == p_230373_5_ && (random.nextInt(2) == 0 || p_230373_3_ == 0);
    }

    private boolean placeLeave(LevelSimulatedRW level, BlockPos pos, Random rand, BoundingBox box, TreeConfiguration config) {
        if (level.isStateAtPosition(pos, state -> {
            //noinspection deprecation
            return state.isAir() || state.getMaterial() == Material.REPLACEABLE_PLANT || state.is(Blocks.WATER);
        })) {
            TreeFeature.setBlockKnownShape(level, pos, config.leavesProvider.getState(rand, pos));
            box.expand(new BoundingBox(pos, pos));
            return true;
        } else {
            return false;
        }
    }
}
