package mythicbotany.mimir;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.block.DirectionShape;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import io.github.noeppi_noeppi.libx.render.ItemStackRenderer;
import mythicbotany.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.item.ItemTwigWand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlockYggdrasilBranch extends BlockTE<TileYggdrasilBranch> implements IWandHUD, IWandable {

    public static DirectionShape SHAPE = new DirectionShape(makeCuboidShape(5, 0, 8, 11, 12, 14));
    
    public BlockYggdrasilBranch(ModX mod, Properties properties) {
        this(mod, properties, new Item.Properties());
    }

    public BlockYggdrasilBranch(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileYggdrasilBranch.class, properties, itemProperties.setISTER(() -> ItemStackRenderer::get));
        this.setDefaultState(this.getStateContainer().getBaseState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
    }

    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ClientRegistry.bindTileEntityRenderer(getTileType(), RenderYggdrasilBranch::new);
        ItemStackRenderer.addRenderTile(getTileType(), false);
    }

    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        TileYggdrasilBranch tile = getTile(world, pos);
        if (player.getHeldItem(hand).getItem() instanceof ItemTwigWand) {
            return ActionResultType.PASS;
        } else if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            if (!world.isRemote) {
                if (player.getHeldItem(hand).isEmpty()) {
                    player.setHeldItem(hand, tile.getInventory().getStackInSlot(0).copy());
                } else {
                    player.inventory.addItemStackToInventory(tile.getInventory().getStackInSlot(0).copy());
                }
                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
            return ActionResultType.CONSUME;
        } else if (player.getHeldItem(hand).getItem() == ModItems.gjallarHornEmpty && player.getHeldItem(hand).getCount() == 1) {
            if (!world.isRemote) {
                tile.getInventory().setStackInSlot(0, player.getHeldItem(hand).copy());
                player.setHeldItem(hand, ItemStack.EMPTY);
            }
            return ActionResultType.CONSUME;
        } else {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        return SHAPE.getShape(state.get(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(MatrixStack matrixStack, Minecraft minecraft, World world, BlockPos pos) {
        TileYggdrasilBranch te = getTile(world, pos);
        String name = I18n.format(getTranslationKey());
        BotaniaAPIClient.instance().drawSimpleManaHUD(matrixStack, 0x00FF00, te.getCurrentMana(), te.maxMana, name);
    }

    @Override
    public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
        if (world.isRemote) {
            LibX.getNetwork().requestTE(world, pos);
        }
        return true;
    }
}
