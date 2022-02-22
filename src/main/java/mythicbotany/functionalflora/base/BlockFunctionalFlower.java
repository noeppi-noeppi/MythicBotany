package mythicbotany.functionalflora.base;

import com.google.common.collect.ImmutableMap;
import io.github.noeppi_noeppi.libx.base.tile.BlockBE;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlockFunctionalFlower<T extends FunctionalFlowerBase> extends BlockBE<T> implements IPlantable {
    
    private static final VoxelShape SHAPE = box(4.8D, 0.0D, 4.8D, 12.8D, 16.0D, 12.8D);
    
    private final BlockFloatingFunctionalFlower<T> floatingBlock;
    public final boolean isGenerating;

    public BlockFunctionalFlower(ModX mod, Class<T> beClass, Properties properties, boolean isGenerating) {
        this(mod, beClass, properties, new Item.Properties(), isGenerating);
    }

    public BlockFunctionalFlower(ModX mod, Class<T> beClass, Properties properties, Item.Properties itemProperties, boolean isGenerating) {
        super(mod, beClass, properties.noCollission()
                .isRedstoneConductor((state, world, pos) -> false)
                .instabreak().sound(SoundType.GRASS), itemProperties);
        this.isGenerating = isGenerating;
        this.floatingBlock = new BlockFloatingFunctionalFlower<>(mod, beClass, this);
    }

    @Override
    public Map<String, Object> getNamedAdditionalRegisters(ResourceLocation id) {
        return ImmutableMap.of("floating", floatingBlock);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(ResourceLocation id, Consumer<Runnable> defer) {
        BlockEntityRenderers.register(this.getBlockEntityType(), mgr -> new RenderFunctionalFlower<>());
        ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, @Nonnull Level level, @Nonnull BlockPos pos) {
        FunctionalFlowerBase te = getBlockEntity(level, pos);
        if (te.getCurrentMana() > 0) {
            return 1 + (int) ((te.getCurrentMana() / (double) te.maxMana) * 14);
        } else {
            return 0;
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        Vec3 shift = state.getOffset(level, pos);
        return SHAPE.move(shift.x, shift.y, shift.z);
    }

    @Nonnull
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    public BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
        return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    protected boolean isValidGround(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.PODZOL) || state.is(Blocks.FARMLAND) || state.is(ModBlocks.enchantedSoil)
                || state.is(Blocks.MYCELIUM) || state.canSustainPlant(level, pos, Direction.UP, this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(level.getBlockState(blockpos), level, blockpos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull PathComputationType type) {
        return type == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, level, pos, type);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (isGenerating) {
            tooltip.add(new TranslatableComponent("botania.flowerType.generating").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));
        } else {
            tooltip.add(new TranslatableComponent("botania.flowerType.functional").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));

        }
        //noinspection ConstantConditions
        tooltip.add(new TranslatableComponent("block." + mod.modid + "." + this.getRegistryName().getPath() + ".description").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

    public BlockFloatingFunctionalFlower<T> getFloatingBlock() {
        return floatingBlock;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getPlant(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() != this) return defaultBlockState();
        return state;
    }
}
