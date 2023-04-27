package mythicbotany.mimir;

import org.moddingx.libx.base.tile.BlockBE;
import org.moddingx.libx.block.RotationShape;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.render.ItemStackRenderer;
import mythicbotany.ModItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import vazkii.botania.common.item.ItemTwigWand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockYggdrasilBranch extends BlockBE<TileYggdrasilBranch> {

    public static final RotationShape SHAPE = new RotationShape(box(5, 0, 8, 11, 12, 14));
    
    public BlockYggdrasilBranch(ModX mod, Properties properties) {
        this(mod, properties, new Item.Properties());
    }

    public BlockYggdrasilBranch(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileYggdrasilBranch.class, properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        BlockEntityRenderers.register(this.getBlockEntityType(), mgr -> new RenderYggdrasilBranch());
        ItemStackRenderer.addRenderBlock(this.getBlockEntityType(), false);
    }

    @Override
//    @OnlyIn(Dist.CLIENT)
    public void initializeItemClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ItemStackRenderer.createProperties());
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        TileYggdrasilBranch tile = this.getBlockEntity(level, pos);
        if (player.getItemInHand(hand).getItem() instanceof ItemTwigWand) {
            return InteractionResult.PASS;
        } else if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            if (!level.isClientSide) {
                if (player.getItemInHand(hand).isEmpty()) {
                    player.setItemInHand(hand, tile.getInventory().getStackInSlot(0).copy());
                } else {
                    player.getInventory().add(tile.getInventory().getStackInSlot(0).copy());
                }
                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
            return InteractionResult.CONSUME;
        } else if (player.getItemInHand(hand).getItem() == ModItems.gjallarHornEmpty && player.getItemInHand(hand).getCount() == 1) {
            if (!level.isClientSide) {
                tile.getInventory().setStackInSlot(0, player.getItemInHand(hand).copy());
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
            return InteractionResult.CONSUME;
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE.getShape(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }
}
