package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BlockRuneHolder<T extends TileRuneHolder> extends BlockBE<T> {

    public static VoxelShape SHAPE = Shapes.or(
            box(6.5, 1, 6.5, 9.5, 3, 9.5),
            box(5, 0, 5, 11, 1, 11)
    );
    
    public BlockRuneHolder(ModX mod, Class<T> tileClass, Properties properties) {
        this(mod, tileClass, properties, new Item.Properties());
    }

    public BlockRuneHolder(ModX mod, Class<T> tileClass, Properties properties, Item.Properties itemProperties) {
        super(mod, tileClass, properties, itemProperties);
    }
    
    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        BlockEntityRenderers.register(getBlockEntityType(), mgr -> new RenderRuneHolder());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        TileRuneHolder tile = getBlockEntity(level, pos);
        if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            if (!level.isClientSide) {
                ItemStack held = player.getItemInHand(hand);
                ItemStack stack = tile.getInventory().getStackInSlot(0);
                if (held.isEmpty()) {
                    player.setItemInHand(hand, stack.copy());
                } else if (ItemStack.isSame(held, stack) && ItemStack.tagMatches(held, stack)
                        && held.getCount() + stack.getCount() <= held.getMaxStackSize()) {
                    held.grow(stack.getCount());
                    player.setItemInHand(hand, held);
                } else {
                    player.getInventory().add(tile.getInventory().getStackInSlot(0).copy());
                }
                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (tile.getInventory().isItemValid(0, player.getItemInHand(hand)) && player.getItemInHand(hand).getCount() >= 1) {
            if (!level.isClientSide) {
                ItemStack held = player.getItemInHand(hand);
                ItemStack stack = held.split(1);
                tile.getInventory().setStackInSlot(0, stack);
                player.setItemInHand(hand, held);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return facing == Direction.DOWN && !this.canSurvive(state, level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.DESTROY;
    }
}
