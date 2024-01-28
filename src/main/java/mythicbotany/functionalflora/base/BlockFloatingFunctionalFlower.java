package mythicbotany.functionalflora.base;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.base.tile.BlockBE;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.registration.SetupContext;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockFloatingFunctionalFlower<T extends FunctionalFlowerBase> extends BlockBE<T> {

    private static final VoxelShape SHAPE = box(1.6D, 1.6D, 1.6D, 14.4D, 14.4D, 14.4D);

    private final BlockFunctionalFlower<T> nonFloatingBlock;

    public BlockFloatingFunctionalFlower(ModX mod, Class<T> beClass, BlockFunctionalFlower<T> nonFloatingBlock) {
        super(mod, beClass, Properties.copy(Blocks.RED_TULIP).isRedstoneConductor((state, world, pos) -> false)
                .instabreak().sound(SoundType.GRASS));
        this.nonFloatingBlock = nonFloatingBlock;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient(SetupContext ctx) {
        ctx.enqueue(() -> BlockEntityRenderers.register(this.getBlockEntityType(), mgr -> new RenderFunctionalFlower<>()));
    }

    public BlockFunctionalFlower<T> getNonFloatingBlock() {
        return this.nonFloatingBlock;
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(@Nonnull BlockState blockState, @Nonnull Level level, @Nonnull BlockPos pos) {
        FunctionalFlowerBase te = this.getBlockEntity(level, pos);
        if (te.getCurrentMana() > 0) {
            return 1 + (int) ((te.getCurrentMana() / (double) te.maxMana) * 14);
        } else {
            return 0;
        }
    }

    public boolean propagatesSkylightDown(BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (this.getNonFloatingBlock().isGenerating) {
            tooltip.add(Component.translatable("botania.flowerType.generating").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));

        } else {
            tooltip.add(Component.translatable("botania.flowerType.functional").withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC));

        }
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(this.getNonFloatingBlock());
        if (id != null) {
            tooltip.add(Component.translatable("block." + id.getNamespace() + "." + id.getPath() + ".description").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        if (FMLEnvironment.dist != Dist.CLIENT) return RenderShape.ENTITYBLOCK_ANIMATED;
        return BotaniaConfig.client().staticFloaters() ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
