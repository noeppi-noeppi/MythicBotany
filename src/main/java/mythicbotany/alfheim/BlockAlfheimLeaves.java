package mythicbotany.alfheim;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModBlockTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class BlockAlfheimLeaves extends LeavesBlock implements Registerable {

    protected final ModX mod;
    private final Item item;

    public BlockAlfheimLeaves(ModX mod) {
        this(mod, new net.minecraft.item.Item.Properties());
    }

    public BlockAlfheimLeaves(ModX mod, net.minecraft.item.Item.Properties itemProperties) {
        super(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().harvestTool(ToolType.HOE).sound(SoundType.PLANT).notSolid().setAllowsSpawn((a, b, c, d) -> false).setSuffocates((a, b, c) -> false).setBlocksVision((a, b, c) -> false));
        this.mod = mod;
        if (mod.tab != null) {
            itemProperties.group(mod.tab);
        }
        this.item = new BlockItem(this, itemProperties);
    }

    public Set<Object> getAdditionalRegisters() {
        return ImmutableSet.of(this.item);
    }

    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    }

    @Nonnull
    @Override
    public BlockState updatePostPlacement(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        int distance = getDistance(facingState) + 1;
        if (distance != 1 || state.get(DISTANCE) != distance) {
            world.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
        }
        return state;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        world.setBlockState(pos, updateDistance(state, world, pos), 3);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return updateDistance(this.getDefaultState().with(PERSISTENT, true), context.getWorld(), context.getPos());
    }

    protected int getDistance(BlockState neighbor) {
        if (ModBlockTags.ALFHEIM_LOGS.contains(neighbor.getBlock())) {
            return 0;
        } else {
            return neighbor.getBlock() instanceof LeavesBlock ? neighbor.get(DISTANCE) : 7;
        }
    }

    protected BlockState updateDistance(BlockState state, IWorld worldIn, BlockPos pos) {
        int distance = 7;
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        for (Direction dir : Direction.values()) {
            mpos.setAndMove(pos, dir);
            distance = Math.min(distance, getDistance(worldIn.getBlockState(mpos)) + 1);
            if (distance == 1) {
                break;
            }
        }
        return state.with(DISTANCE, distance);
    }
}
