package mythicbotany.alfheim.content;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModBlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class BlockAlfheimLeaves extends LeavesBlock implements Registerable {

    protected final ModX mod;
    private final Item item;

    public BlockAlfheimLeaves(ModX mod) {
        this(mod, new net.minecraft.world.item.Item.Properties());
    }

    public BlockAlfheimLeaves(ModX mod, net.minecraft.world.item.Item.Properties itemProperties) {
        super(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((a, b, c, d) -> false).isSuffocating((a, b, c) -> false).isViewBlocking((a, b, c) -> false));
        this.mod = mod;
        if (mod.tab != null) {
            itemProperties.tab(mod.tab);
        }
        this.item = new BlockItem(this, itemProperties);
    }

    @Override
    public Set<Object> getAdditionalRegisters(ResourceLocation id) {
        return ImmutableSet.of(this.item);
    }

    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    }

    @Nonnull
    @Override
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        int distance = this.getDistanceAt(facingState) + 1;
        if (distance != 1 || state.getValue(DISTANCE) != distance) {
            level.scheduleTick(currentPos, this, 1);
        }
        return state;
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull Random random) {
        level.setBlock(pos, this.updateDistance(state, level, pos), 3);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.updateDistance(this.defaultBlockState().setValue(PERSISTENT, true), context.getLevel(), context.getClickedPos());
    }

    protected int getDistanceAt(BlockState neighbor) {
        if (ModBlockTags.ALFHEIM_LOGS.contains(neighbor.getBlock())) {
            return 0;
        } else {
            return neighbor.getBlock() instanceof LeavesBlock ? neighbor.getValue(DISTANCE) : 7;
        }
    }

    protected BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int distance = 7;
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            mpos.setWithOffset(pos, dir);
            distance = Math.min(distance, this.getDistanceAt(level.getBlockState(mpos)) + 1);
            if (distance == 1) {
                break;
            }
        }
        return state.setValue(DISTANCE, distance);
    }
}
