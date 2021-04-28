package mythicbotany.mjoellnir;

import com.google.common.collect.ImmutableSet;
import io.github.noeppi_noeppi.libx.mod.registration.Registerable;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BlockMjoellnir extends Block implements Registerable {

    public static final VoxelShape SHAPE = VoxelShapes.or(
            makeCuboidShape(0, 0, 3, 16, 9, 13),
            makeCuboidShape(7, 9, 7, 9, 22, 9)
    );

    private final Item item;
    private final TileEntityType<TileMjoellnir> teType;
    private final EntityType<EntityMjoellnir> entityType;

    public BlockMjoellnir(Properties properties, net.minecraft.item.Item.Properties itemProperties) {
        super(properties);
        this.item = new ItemMjoellnir(this, itemProperties.group(MythicBotany.getInstance().tab));
        //noinspection ConstantConditions
        this.teType = new TileEntityType<>(() -> new TileMjoellnir(getTileType()), ImmutableSet.of(this), null);
        this.entityType = EntityType.Builder.<EntityMjoellnir>create(EntityMjoellnir::new, EntityClassification.MISC).size(0.6f, 0.9f).trackingRange(20).build(MythicBotany.getInstance().modid + "_mjoellnir");
    }

    public TileEntityType<TileMjoellnir> getTileType() {
        return teType;
    }

    public EntityType<EntityMjoellnir> getEntityType() {
        return entityType;
    }

    @Override
    public Set<Object> getAdditionalRegisters() {
        return ImmutableSet.of(item, teType, entityType);
    }

    @Override
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
        ClientRegistry.bindTileEntityRenderer(teType, RenderMjoellnir::new);
        RenderingRegistry.registerEntityRenderingHandler(entityType, RenderEntityMjoellnir::new);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        if (!world.isRemote) {
            if (canHold(player)) {
                TileMjoellnir tile = getTile(world, pos);
                if (putInInventory(player, tile.getStack().copy(), getHotbarSlot(player, hand))) {
                    world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
                } else {
                    return ActionResultType.FAIL;
                }
            } else {
                player.sendMessage(new TranslationTextComponent("message.mythicbotany.mjoellnir_heavy_pick").mergeStyle(TextFormatting.GRAY), player.getUniqueID());
            }
        }
        return ActionResultType.func_233537_a_(world.isRemote);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        return SHAPE;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    public TileMjoellnir createTileEntity(BlockState state, IBlockReader world) {
        return this.teType.create();
    }

    private static TileMjoellnir getTile(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileMjoellnir) {
            return (TileMjoellnir) te;
        } else {
            throw new IllegalStateException("expected a tile entity of type TileMjoellnir, got " + te);
        }
    }

    public static boolean placeInWorld(ItemStack stack, World world, BlockPos pos) {
        return placeInWorld(stack, world, pos, true);
    }
    
    public static boolean placeInWorld(ItemStack stack, World world, BlockPos pos, boolean dropOld) {
        BlockState state = world.getBlockState(pos);
        float hardness = state.getBlockHardness(world, pos);
        if (state.getBlock() != ModBlocks.mjoellnir && ((hardness >= 0 && hardness <= 60) || state.getMaterial().isReplaceable())) {
            List<ItemStack> drops = null;
            if (dropOld && world instanceof ServerWorld) {
                drops = Block.getDrops(state, (ServerWorld) world, pos, world.getTileEntity(pos));
            }
            if (world.setBlockState(pos, ModBlocks.mjoellnir.getDefaultState(), 11)) {
                if (drops != null) {
                    drops.forEach(drop -> {
                        ItemEntity ie = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop);
                        world.addEntity(ie);
                    });
                }
                getTile(world, pos).setStack(stack);
                if (!world.isRemote) {
                    world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1, 1);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void putInWorld(ItemStack stack, World world, BlockPos pos) {
        putInWorld(stack, world, pos, true);
    }
    
    public static void putInWorld(ItemStack stack, World world, BlockPos pos, boolean dropOldOnReplace) {
        if (!placeInWorld(stack, world, pos, dropOldOnReplace)) {
            ItemEntity ie = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            ie.setInvulnerable(true);
            world.addEntity(ie);
        }
    }

    public static boolean canHold(PlayerEntity player) {
        return player.isCreative() || player.isSpectator() || (player.getActivePotionEffect(Effects.ABSORPTION) != null/* && player.getAbsorptionAmount() > 0*/);
    }

    public static boolean putInInventory(PlayerEntity player, ItemStack stack, int hotbarSlot) {
        boolean inserted = false;
        if (hotbarSlot < 9) {
            ItemStack current = player.inventory.getStackInSlot(hotbarSlot);
            if (current.isEmpty()) {
                player.inventory.setInventorySlotContents(hotbarSlot, stack);
                inserted = true;
            }
        } else if (hotbarSlot == 9) {
            ItemStack current = player.inventory.offHandInventory.get(0);
            if (current.isEmpty()) {
                player.inventory.offHandInventory.set(0, stack);
                inserted = true;
            }
        }
        if (!inserted) {
            int slot = player.inventory.getFirstEmptyStack();
            if (slot >= 0) {
                player.inventory.setInventorySlotContents(slot, stack);
                inserted = true;
            }
        }
        return inserted;
    }

    public static int getHotbarSlot(PlayerEntity player, Hand hand) {
        return hand == Hand.MAIN_HAND ? player.inventory.currentItem : 9;
    }
}
