package mythicbotany.rune;

import org.moddingx.libx.mod.ModX;
import mythicbotany.MythicPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vazkii.botania.common.item.ItemTwigWand;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockCentralRuneHolder extends BlockRuneHolder<TileCentralRuneHolder> {

    public BlockCentralRuneHolder(ModX mod, Properties properties) {
        this(mod, properties, new Item.Properties());
    }

    public BlockCentralRuneHolder(ModX mod, Properties properties, Item.Properties itemProperties) {
        super(mod, TileCentralRuneHolder.class, properties, itemProperties);
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (player.getItemInHand(hand).getItem() instanceof ItemTwigWand) {
            if (!level.isClientSide) {
                if (!MythicPlayerData.getData(player).getBoolean("MimirKnowledge")) {
                    player.sendMessage(new TranslatableComponent("message.mythicbotany.mimir_unknown").withStyle(ChatFormatting.GRAY), player.getUUID());
                } else {
                    TileCentralRuneHolder tile = this.getBlockEntity(level, pos);
                    tile.tryStartRitual(player);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof TileCentralRuneHolder) {
            ((TileCentralRuneHolder) te).cancelRecipe();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
