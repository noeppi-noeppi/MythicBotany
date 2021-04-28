package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BlockRuneHolder<T extends TileRuneHolder> extends BlockTE<T> {

    public static VoxelShape SHAPE = VoxelShapes.or(
            makeCuboidShape(6.5, 1, 6.5, 9.5, 3, 9.5),
            makeCuboidShape(5, 0, 5, 11, 1, 11)
    );
    
    public BlockRuneHolder(ModX mod, Class<T> tileClass, Properties properties) {
        this(mod, tileClass, properties, new Item.Properties());
    }

    public BlockRuneHolder(ModX mod, Class<T> tileClass, Properties properties, Item.Properties itemProperties) {
        super(mod, tileClass, properties, itemProperties);
    }
    
    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ClientRegistry.bindTileEntityRenderer(getTileType(), RenderRuneHolder::new);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        TileRuneHolder tile = getTile(world, pos);
        if (!tile.getInventory().getStackInSlot(0).isEmpty()) {
            if (!world.isRemote) {
                ItemStack held = player.getHeldItem(hand);
                ItemStack stack = tile.getInventory().getStackInSlot(0);
                if (held.isEmpty()) {
                    player.setHeldItem(hand, stack.copy());
                } else if (ItemStack.areItemsEqual(held, stack) && ItemStack.areItemStackTagsEqual(held, stack)
                        && held.getCount() + stack.getCount() <= held.getMaxStackSize()) {
                    held.grow(stack.getCount());
                    player.setHeldItem(hand, held);
                } else {
                    player.inventory.addItemStackToInventory(tile.getInventory().getStackInSlot(0).copy());
                }
                tile.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        } else if (tile.getInventory().isItemValid(0, player.getHeldItem(hand)) && player.getHeldItem(hand).getCount() >= 1) {
            if (!world.isRemote) {
                ItemStack held = player.getHeldItem(hand);
                ItemStack stack = held.split(1);
                tile.getInventory().setStackInSlot(0, stack);
                player.setHeldItem(hand, held);
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        } else {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
    }
    
    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        return SHAPE;
    }
}
