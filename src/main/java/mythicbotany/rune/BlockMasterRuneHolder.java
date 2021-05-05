package mythicbotany.rune;

import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.MythicPlayerData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import vazkii.botania.common.item.ItemTwigWand;

import javax.annotation.Nonnull;

public class BlockMasterRuneHolder extends BlockRuneHolder<TileMasterRuneHolder> {

    public BlockMasterRuneHolder(ModX mod, Properties properties) {
        this(mod, properties, new Item.Properties());
    }

    public BlockMasterRuneHolder(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileMasterRuneHolder.class, properties, itemProperties);
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
        if (player.getHeldItem(hand).getItem() instanceof ItemTwigWand) {
            if (!world.isRemote) {
                if (!MythicPlayerData.getData(player).getBoolean("MimirKnowledge")) {
                    player.sendMessage(new TranslationTextComponent("message.mythicbotany.mimir_unknown").mergeStyle(TextFormatting.GRAY), player.getUniqueID());
                } else {
                    TileMasterRuneHolder tile = getTile(world, pos);
                    tile.tryStartRitual(player);
                }
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        } else {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileMasterRuneHolder) {
            ((TileMasterRuneHolder) te).cancelRecipe();
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }
}
