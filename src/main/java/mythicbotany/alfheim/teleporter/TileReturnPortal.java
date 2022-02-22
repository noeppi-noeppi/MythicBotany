package mythicbotany.alfheim.teleporter;

import io.github.noeppi_noeppi.libx.base.tile.BlockEntityBase;
import io.github.noeppi_noeppi.libx.base.tile.TickableBlock;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class TileReturnPortal extends BlockEntityBase implements TickableBlock {

    public TileReturnPortal(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            if (!validPortal(level, worldPosition)) {
                level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
                return;
            }
            if (AlfheimPortalHandler.shouldCheck(level)) {
                List<Player> playersInPortal = level.getEntitiesOfClass(Player.class, new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1));
                for (Player player : playersInPortal) {
                    if (player instanceof ServerPlayer) {
                        if (AlfheimPortalHandler.setInPortal(level, player)) {
                            AlfheimTeleporter.teleportToOverworld((ServerPlayer) player, worldPosition);
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
                    Block required1 = x == 0 || z == 0 ? ModBlocks.livingwoodGlimmering : ModBlocks.livingwood;
                    Block required2 = x == 0 || z == 0 ? ModBlocks.dreamwoodGlimmering : ModBlocks.dreamwood;
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
