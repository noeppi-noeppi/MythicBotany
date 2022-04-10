package mythicbotany.alfheim.teleporter;

import mythicbotany.ModBlocks;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.util.AlfheimWorldGenUtil;
import mythicbotany.alfheim.util.HorizontalPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public class AlfheimTeleporter {

    public static boolean teleportToAlfheim(ServerPlayer player, BlockPos sourcePos) {
        ServerLevel target = player.getLevel().getServer().getLevel(Alfheim.DIMENSION);
        if (target != null) {
            BlockPos pos = findBlock(target, HorizontalPos.from(sourcePos), ModBlocks.returnPortal);
            if (pos == null) {
                pos = AlfheimWorldGenUtil.highestFreeBlock(target, HorizontalPos.from(sourcePos), AlfheimWorldGenUtil::passReplaceableAndDreamWood).below();
                target.setBlock(pos.north(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.south(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.east(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.west(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.offset(-1, 0, -1), vazkii.botania.common.block.ModBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(-1, 0, 1), vazkii.botania.common.block.ModBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(1, 0, -1), vazkii.botania.common.block.ModBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(1, 0, 1), vazkii.botania.common.block.ModBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos, ModBlocks.returnPortal.defaultBlockState(), 3);
            }
            player.teleportTo(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.getYRot(), player.getXRot());
            player.setPortalCooldown();
            return true;
        } else {
            player.portalCooldown = 200;
            return false;
        }
    }
    
    public static void teleportToOverworld(ServerPlayer player, BlockPos sourcePos) {
        ServerLevel target = player.getLevel().getServer().overworld();
        BlockPos pos = findBlock(target, HorizontalPos.from(sourcePos), vazkii.botania.common.block.ModBlocks.alfPortal);
        if (pos != null) {
            pos = pos.above();
        } else {
            pos = AlfheimWorldGenUtil.highestFreeBlock(target, HorizontalPos.from(sourcePos), AlfheimWorldGenUtil::passReplaceableAndDreamWood);
        }
        player.teleportTo(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.getYRot(), player.getXRot());
        player.setPortalCooldown();
    }
    
    @Nullable
    private static BlockPos findBlock(ServerLevel level, HorizontalPos sourcePos, Block block) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(sourcePos.x(), level.getMaxBuildHeight(), sourcePos.z());
        while (mpos.getY() > level.getMinBuildHeight()) {
            mpos.move(Direction.DOWN);
            if (level.getBlockState(mpos).getBlock() == block) {
                return mpos.immutable();
            }
        }
        return null;
    }
}
