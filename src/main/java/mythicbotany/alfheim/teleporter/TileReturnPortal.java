package mythicbotany.alfheim.teleporter;

import org.moddingx.libx.base.tile.BlockEntityBase;
import org.moddingx.libx.base.tile.TickingBlock;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.List;

public class TileReturnPortal extends BlockEntityBase implements TickingBlock {

    public TileReturnPortal(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            if (!validPortal(this.level, this.worldPosition)) {
                this.level.setBlockAndUpdate(this.worldPosition, Blocks.AIR.defaultBlockState());
                return;
            }
            if (AlfheimPortalHandler.shouldCheck(this.level)) {
                List<Player> playersInPortal = this.level.getEntitiesOfClass(Player.class, new AABB(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), this.worldPosition.getX() + 1, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 1));
                for (Player player : playersInPortal) {
                    if (player instanceof ServerPlayer) {
                        if (AlfheimPortalHandler.setInPortal(this.level, player)) {
                            AlfheimTeleporter.teleportToOverworld((ServerPlayer) player, this.worldPosition);
                        }
                    }
                }
            }
        }
    }

    public static boolean validPortal(Level level, BlockPos pos) {
        // Only allow return portals in alfheim.
        if (!Alfheim.DIMENSION.equals(level.dimension())) {
            return false;
        }
        Boolean isDreamwood = null;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    Block required1 = x == 0 || z == 0 ? BotaniaBlocks.livingwoodGlimmering : BotaniaBlocks.livingwood;
                    Block required2 = x == 0 || z == 0 ? BotaniaBlocks.dreamwoodGlimmering : BotaniaBlocks.dreamwood;
                    Block actual = level.getBlockState(pos.offset(x, 0, z)).getBlock();
                    if (isDreamwood == null) {
                        if (actual == required1) {
                            isDreamwood = false;
                        } else if (actual == required2) {
                            isDreamwood = true;
                        } else {
                            return false;
                        }
                    } else if (isDreamwood) {
                        if (actual != required2) {
                            return false;
                        }
                    } else {
                        if (actual != required1) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
