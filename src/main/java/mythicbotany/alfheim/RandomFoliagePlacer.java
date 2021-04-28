package mythicbotany.alfheim;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import mythicbotany.MythicBotany;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

public class RandomFoliagePlacer extends FoliagePlacer {

    public static final FoliagePlacerType<RandomFoliagePlacer> RANDOM_FOLIAGE = new FoliagePlacerType<>(RecordCodecBuilder.create((p_236742_0_) -> func_242830_b(p_236742_0_).apply(p_236742_0_, RandomFoliagePlacer::new)));

    static {
        RANDOM_FOLIAGE.setRegistryName(new ResourceLocation(MythicBotany.getInstance().modid, "random_foliage"));
        ForgeRegistries.FOLIAGE_PLACER_TYPES.register(RANDOM_FOLIAGE);
    }

    public RandomFoliagePlacer(FeatureSpread spread1, FeatureSpread spread2) {
        super(spread1, spread2);
    }


    @Nonnull
    @Override
    protected FoliagePlacerType<?> func_230371_a_() {
        return RANDOM_FOLIAGE;
    }

    @Override
    protected void func_230372_a_(@Nonnull IWorldGenerationReader world, @Nonnull Random rand, @Nonnull BaseTreeFeatureConfig config, int p_230372_4_, @Nonnull Foliage foliage, int p_230372_6_, int p_230372_7_, @Nonnull Set<BlockPos> p_230372_8_, int p_230372_9_, @Nonnull MutableBoundingBox box) {
        int leaves = 3 + rand.nextInt(3);
        for (int i = 0; i < leaves; i++) {
            int x = rand.nextInt(5) - 2;
            int y = rand.nextInt(5) - 2;
            int z = rand.nextInt(5) - 2;
            if (placeLeave(world, foliage.func_236763_a_().add(x, y, z), rand, box, config)) {
                int nx = x == 0 ? 0 : x > 0 ? x - 1 : x + 1;
                int ny = y == 0 ? 0 : y > 0 ? y - 1 : y + 1;
                int nz = z == 0 ? 0 : z > 0 ? z - 1 : z + 1;
                placeLeave(world, foliage.func_236763_a_().add(nx, ny, nz), rand, box, config);
            }
        }
    }

    @Override
    public int func_230374_a_(@Nonnull Random rand, int p_230374_2_, @Nonnull BaseTreeFeatureConfig config) {
        return 3;
    }

    @Override
    protected boolean func_230373_a_(@Nonnull Random rand, int p_230373_2_, int p_230373_3_, int p_230373_4_, int p_230373_5_, boolean p_230373_6_) {
        return p_230373_2_ == p_230373_5_ && p_230373_4_ == p_230373_5_ && (rand.nextInt(2) == 0 || p_230373_3_ == 0);
    }

    private boolean placeLeave(IWorldGenerationReader world, BlockPos pos, Random rand, MutableBoundingBox box, BaseTreeFeatureConfig config) {
        if (world.hasBlockState(pos, state -> {
            //noinspection deprecation
            return state.isAir() || state.getMaterial() == Material.TALL_PLANTS || state.isIn(Blocks.WATER);
        })) {
            TreeFeature.func_236408_b_(world, pos, config.leavesProvider.getBlockState(rand, pos));
            box.expandTo(new MutableBoundingBox(pos, pos));
            return true;
        } else {
            return false;
        }
    }
}
