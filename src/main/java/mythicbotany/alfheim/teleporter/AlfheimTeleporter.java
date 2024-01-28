package mythicbotany.alfheim.teleporter;

import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.worldgen.AlfheimWorldGen;
import mythicbotany.register.ModBlocks;
import mythicbotany.util.HorizontalPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import vazkii.botania.common.block.BotaniaBlocks;

import javax.annotation.Nullable;

public class AlfheimTeleporter {

    public static boolean teleportToAlfheim(ServerPlayer player, BlockPos sourcePos) {
        ServerLevel target = player.server.getLevel(Alfheim.DIMENSION);
        if (target != null) {
            BlockPos pos = findBlock(target, new HorizontalPos(sourcePos), ModBlocks.returnPortal);
            if (pos == null) {
                pos = AlfheimWorldGen.highestFreeBlock(target, new HorizontalPos(sourcePos)).below();
                target.setBlock(pos.north(), BotaniaBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.south(), BotaniaBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.east(), BotaniaBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.west(), BotaniaBlocks.livingwoodGlimmering.defaultBlockState(), 3);
                target.setBlock(pos.offset(-1, 0, -1), BotaniaBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(-1, 0, 1), BotaniaBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(1, 0, -1), BotaniaBlocks.livingwood.defaultBlockState(), 3);
                target.setBlock(pos.offset(1, 0, 1), BotaniaBlocks.livingwood.defaultBlockState(), 3);
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
        ServerLevel target = player.server.overworld();
        BlockPos pos = findBlock(target, new HorizontalPos(sourcePos), BotaniaBlocks.alfPortal);
        if (pos != null) {
            pos = pos.above();
        } else {
            pos = AlfheimWorldGen.highestFreeBlock(target, new HorizontalPos(sourcePos));
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
