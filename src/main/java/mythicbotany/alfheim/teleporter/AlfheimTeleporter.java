package mythicbotany.alfheim.teleporter;

import mythicbotany.ModBlocks;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.AlfheimWorldGen;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.Objects;

public class AlfheimTeleporter {

    public static void teleportToAlfheim(ServerPlayer player, BlockPos sourcePos) {
        ServerLevel target = player.getLevel().getServer().getLevel(Alfheim.DIMENSION);
        Objects.requireNonNull(target, "Alfheim dimension not generated.");
        BlockPos pos = findBlock(target, sourcePos, ModBlocks.returnPortal);
        if (pos == null) {
            pos = AlfheimWorldGen.highestFreeBlock(target, sourcePos, AlfheimWorldGen::passReplaceableAndDreamwood).below();
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
        player.teleportTo(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.yRot, player.xRot);
        player.setPortalCooldown();
    }
    
    public static void teleportToOverworld(ServerPlayer player, BlockPos sourcePos) {
        ServerLevel target = player.getLevel().getServer().getLevel(Level.OVERWORLD);
        Objects.requireNonNull(target, "Overworld dimension not generated.");
        BlockPos pos = findBlock(target, sourcePos, vazkii.botania.common.block.ModBlocks.alfPortal);
        if (pos != null) {
            pos = pos.above();
        } else {
            pos = AlfheimWorldGen.highestFreeBlock(target, sourcePos, AlfheimWorldGen::passReplaceableAndLeaves);
        }
        player.teleportTo(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.yRot, player.xRot);
        player.setPortalCooldown();
    }
    
    @Nullable
    private static BlockPos findBlock(ServerLevel level, BlockPos sourcePos, Block block) {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(sourcePos.getX(), level.getMaxBuildHeight(), sourcePos.getZ());
        while (mpos.getY() > 0) {
            mpos.move(Direction.DOWN);
            if (level.getBlockState(mpos).getBlock() == block) {
                return mpos.immutable();
            }
        }
        return null;
    }
}
