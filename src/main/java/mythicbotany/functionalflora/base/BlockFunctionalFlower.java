package mythicbotany.functionalflora.base;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockBase;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class BlockFunctionalFlower<T extends FunctionalFlowerBase> extends BlockBase implements IWandHUD, IWandable, IPlantable {

    public static final ResourceLocation POOL_ID = new ResourceLocation("botania", "mana_pool");
    public static final ResourceLocation SPREADER_ID = new ResourceLocation("botania", "mana_spreader");
    private static final VoxelShape SHAPE = makeCuboidShape(4.8D, 0.0D, 4.8D, 12.8D, 16.0D, 12.8D);

    private final Class<T> teClass;
    private final Constructor<T> teCtor;
    private final TileEntityType<T> teType;
    private final BlockFloatingFunctionalFlower<T> floatingBlock;
    public final boolean isGenerating;

    public BlockFunctionalFlower(ModX mod, Class<T> teClass, Properties properties, boolean isGenerating) {
        this(mod, teClass, properties.doesNotBlockMovement()
                .setOpaque((state, world, pos) -> false)
                .zeroHardnessAndResistance().sound(SoundType.PLANT), new Item.Properties(), isGenerating);
    }

    public BlockFunctionalFlower(ModX mod, Class<T> teClass, Properties properties, Item.Properties itemProperties, boolean isGenerating) {
        super(mod, properties.doesNotBlockMovement()
                .setOpaque((state, world, pos) -> false)
                .zeroHardnessAndResistance().sound(SoundType.PLANT), itemProperties);

        this.teClass = teClass;
        this.isGenerating = isGenerating;
        this.floatingBlock = new BlockFloatingFunctionalFlower<>(mod, this);

        try {
            this.teCtor = teClass.getConstructor(TileEntityType.class);
        } catch (ReflectiveOperationException e) {
            if (e.getCause() != null)
                e.getCause().printStackTrace();
            throw new RuntimeException("Could not get constructor for tile entity " + teClass + ".", e);
        }
        //noinspection ConstantConditions
        teType = new TileEntityType<>(() -> {
            try {
                return teCtor.newInstance(getTileType());
            } catch (ReflectiveOperationException e) {
                if (e.getCause() != null)
                    e.getCause().printStackTrace();
                throw new RuntimeException("Could not create TileEntity of type " + teClass + ".", e);
            }
        }, ImmutableSet.of(this, floatingBlock), null);
    }

    @Override
    public Set<Object> getAdditionalRegisters() {
        return ImmutableSet.builder().addAll(super.getAdditionalRegisters()).add(teType).build();
    }

    @Override
    public Map<String, Object> getNamedAdditionalRegisters() {
        return ImmutableMap.of("floating", floatingBlock);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        ClientRegistry.bindTileEntityRenderer(teType, RenderFunctionalFlower::new);
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(MatrixStack matrixStack, Minecraft minecraft, World world, BlockPos pos) {
        FunctionalFlowerBase te = getTile(world, pos);
        String name = I18n.format(getTranslationKey());
        BotaniaAPIClient.instance().drawComplexManaHUD(matrixStack, te.color, te.getCurrentMana(), te.maxMana, name, new ItemStack(ForgeRegistries.ITEMS.getValue(isGenerating ? SPREADER_ID : POOL_ID)), te.isValidBinding());
    }

    @Override
    public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
        if (world.isRemote) {
            LibX.getNetwork().requestTE(world, pos);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorInputOverride(@Nonnull BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        FunctionalFlowerBase te = getTile(world, pos);
        if (te.getCurrentMana() > 0) {
            return 1 + (int) ((te.getCurrentMana() / (double) te.maxMana) * 14);
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        Vector3d shift = state.getOffset(world, pos);
        return SHAPE.withOffset(shift.x, shift.y, shift.z);
    }

    @Nonnull
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public BlockState updatePostPlacement(BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull IWorld world, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }

    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos) {
        return state.matchesBlock(Blocks.GRASS_BLOCK) || state.matchesBlock(Blocks.DIRT) || state.matchesBlock(Blocks.COARSE_DIRT)
                || state.matchesBlock(Blocks.PODZOL) || state.matchesBlock(Blocks.FARMLAND) || state.matchesBlock(ModBlocks.enchantedSoil)
                || state.matchesBlock(Blocks.MYCELIUM) || state.canSustainPlant(world, pos, Direction.UP, this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(@Nonnull BlockState state, @Nonnull IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean allowsMovement(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull PathType type) {
        return type == PathType.AIR && !this.canCollide || super.allowsMovement(state, worldIn, pos, type);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        if (isGenerating) {
            list.add(new TranslationTextComponent("botania.flowerType.generating").mergeStyle(TextFormatting.BLUE, TextFormatting.ITALIC));

        } else {
            list.add(new TranslationTextComponent("botania.flowerType.functional").mergeStyle(TextFormatting.BLUE, TextFormatting.ITALIC));

        }
        //noinspection ConstantConditions
        list.add(new TranslationTextComponent("block." + mod.modid + "." + this.getRegistryName().getPath() + ".description").mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTransparent(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public T createTileEntity(BlockState state, IBlockReader world) {
        return teType.create();
    }

    public T getTile(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te == null || !teClass.isAssignableFrom(te.getClass())) {
            throw new IllegalStateException("Expected a tile entity of type " + teClass + " at " + world + " " + pos + ", got" + te);
        }
        //noinspection unchecked
        return (T) te;
    }

    public TileEntityType<T> getTileType() {
        return teType;
    }

    public BlockFloatingFunctionalFlower<T> getFloatingBlock() {
        return floatingBlock;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
    }
}
