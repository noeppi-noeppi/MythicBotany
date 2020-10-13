package mythicbotany.functionalflora.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockBase;
import mythicbotany.MythicBotany;
import mythicbotany.network.MythicNetwork;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static mythicbotany.functionalflora.base.BlockFunctionalFlower.POOL_ID;
import static mythicbotany.functionalflora.base.BlockFunctionalFlower.SPREADER_ID;

public class BlockFloatingFunctionalFlower<T extends FunctionalFlowerBase> extends BlockBase implements IWandHUD, IWandable {

    private static final VoxelShape SHAPE = makeCuboidShape(1.6D, 1.6D, 1.6D, 14.4D, 14.4D, 14.4D);

    private final BlockFunctionalFlower<T> nonFloatingBlock;

    public BlockFloatingFunctionalFlower(ModX mod, BlockFunctionalFlower<T> nonFloatingBlock) {
        super(mod, Properties.create(Material.PLANTS).setOpaque((state, world, pos) -> false)
                .zeroHardnessAndResistance().sound(SoundType.PLANT));
        this.nonFloatingBlock = nonFloatingBlock;
    }

    public BlockFunctionalFlower<T> getNonFloatingBlock() {
        return nonFloatingBlock;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public T createTileEntity(BlockState state, IBlockReader world) {
        T te = nonFloatingBlock.getTileType().create();
        if (te != null)
            te.setFloating(true);
        return te;
    }

    public T getTile(World world, BlockPos pos) {
        return nonFloatingBlock.getTile(world, pos);
    }

    public TileEntityType<T> getTileType() {
        return nonFloatingBlock.getTileType();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(MatrixStack matrixStack, Minecraft minecraft, World world, BlockPos pos) {
        FunctionalFlowerBase te = getTile(world, pos);
        String name = I18n.format(getNonFloatingBlock().getTranslationKey());
        // noinspection deprecation,deprecation
        BotaniaAPIClient.instance().drawComplexManaHUD(matrixStack, te.color, te.getCurrentMana(), te.maxMana, name, Registry.ITEM.func_241873_b(getNonFloatingBlock().isGenerating ? SPREADER_ID : POOL_ID).map(ItemStack::new).orElse(ItemStack.EMPTY), te.isValidBinding());
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

    public boolean propagatesSkylightDown(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable IBlockReader world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        if (getNonFloatingBlock().isGenerating) {
            list.add(new TranslationTextComponent("botania.flowerType.generating").mergeStyle(TextFormatting.BLUE, TextFormatting.ITALIC));

        } else {
            list.add(new TranslationTextComponent("botania.flowerType.functional").mergeStyle(TextFormatting.BLUE, TextFormatting.ITALIC));

        }
        //noinspection ConstantConditions
        list.add(new TranslationTextComponent("block." + mod.modid + "." + this.getNonFloatingBlock().getRegistryName().getPath() + ".description").mergeStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTransparent(@Nonnull BlockState state) {
        return true;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state) {
        return ConfigHandler.CLIENT.staticFloaters.get() ? BlockRenderType.MODEL : BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
