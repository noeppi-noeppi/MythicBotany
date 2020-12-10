package mythicbotany.infuser;

import io.github.noeppi_noeppi.libx.crafting.recipe.RecipeHelper;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import mythicbotany.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockManaInfuser extends BlockTE<TileManaInfuser> {

    private static final VoxelShape SHAPE = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);

    public BlockManaInfuser(ModX mod, Class<TileManaInfuser> teClass, Properties properties) {
        super(mod, teClass, properties);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull PathType type) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        TileManaInfuser te = getTile(world, pos);
        double progress = te.getProgess();
        if (progress < 0) {
            return 0;
        } else {
            return 1 + (int) (Math.round(progress * 14));
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.isEmpty() && RecipeHelper.isItemValidInput(world.getRecipeManager(), ModRecipes.INFUSER, stack)) {
                ItemStack copy = stack.copy();
                stack.shrink(1);
                player.setHeldItem(hand, stack);
                copy.setCount(1);
                ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, copy);
                entity.setMotion(0, 0, 0);
                world.addEntity(entity);
                return ActionResultType.SUCCESS;
            } else {
                return super.onBlockActivated(state, world, pos, player, hand, hit);
            }
        } else {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
    }
}
