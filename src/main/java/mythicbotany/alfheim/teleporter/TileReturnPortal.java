package mythicbotany.alfheim.teleporter;

import io.github.noeppi_noeppi.libx.mod.registration.TileEntityBase;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class TileReturnPortal extends TileEntityBase implements ITickableTileEntity {

    public TileReturnPortal(TileEntityType<?> teType) {
        super(teType);
    }

    @Override
    public void tick() {
        if (world != null && pos != null && !world.isRemote) {
            if (!validPortal(world, pos)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return;
            }
            if (AlfheimPortalHandler.shouldCheck(world)) {
                List<PlayerEntity> playersInPortal = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
                for (PlayerEntity player : playersInPortal) {
                    if (player instanceof ServerPlayerEntity) {
                        if (AlfheimPortalHandler.setInPortal(world, player)) {
                            AlfheimTeleporter.teleportToOverworld((ServerPlayerEntity) player, pos);
                        }
                    }
                }
            }
        }
    }

    public static boolean validPortal(World world, BlockPos pos) {
        // Only allow return portals in alfheim.
        if (!Alfheim.DIMENSION.equals(world.getDimensionKey())) {
            return false;
        }
        Boolean isDreamwood = null;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x != 0 || z != 0) {
                    Block required1 = x == 0 || z == 0 ? ModBlocks.livingwoodGlimmering : ModBlocks.livingwood;
                    Block required2 = x == 0 || z == 0 ? ModBlocks.dreamwoodGlimmering : ModBlocks.dreamwood;
                    Block actual = world.getBlockState(pos.add(x, 0, z)).getBlock();
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
