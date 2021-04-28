package mythicbotany.alfheim.teleporter;

import mythicbotany.ModBlocks;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.AlfheimWorldGen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Objects;

public class AlfheimTeleporter {

    public static void teleportToAlfheim(ServerPlayerEntity player, BlockPos sourcePos) {
        ServerWorld target = player.getServerWorld().getServer().getWorld(Alfheim.DIMENSION);
        Objects.requireNonNull(target, "Alfheim dimension not generated.");
        BlockPos pos = findBlock(target, sourcePos, ModBlocks.returnPortal);
        if (pos == null) {
            pos = AlfheimWorldGen.highestFreeBlock(target, sourcePos, AlfheimWorldGen::passReplaceableAndDreamwood).down();
            target.setBlockState(pos.north(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.getDefaultState(), 3);
            target.setBlockState(pos.south(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.getDefaultState(), 3);
            target.setBlockState(pos.east(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.getDefaultState(), 3);
            target.setBlockState(pos.west(), vazkii.botania.common.block.ModBlocks.livingwoodGlimmering.getDefaultState(), 3);
            target.setBlockState(pos.add(-1, 0, -1), vazkii.botania.common.block.ModBlocks.livingwood.getDefaultState(), 3);
            target.setBlockState(pos.add(-1, 0, 1), vazkii.botania.common.block.ModBlocks.livingwood.getDefaultState(), 3);
            target.setBlockState(pos.add(1, 0, -1), vazkii.botania.common.block.ModBlocks.livingwood.getDefaultState(), 3);
            target.setBlockState(pos.add(1, 0, 1), vazkii.botania.common.block.ModBlocks.livingwood.getDefaultState(), 3);
            target.setBlockState(pos, ModBlocks.returnPortal.getDefaultState(), 3);
        }
        player.teleport(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
        player.func_242279_ag();
    }
    
    public static void teleportToOverworld(ServerPlayerEntity player, BlockPos sourcePos) {
        ServerWorld target = player.getServerWorld().getServer().getWorld(World.OVERWORLD);
        Objects.requireNonNull(target, "Overworld dimension not generated.");
        BlockPos pos = findBlock(target, sourcePos, vazkii.botania.common.block.ModBlocks.alfPortal);
        if (pos != null) {
            pos = pos.up();
        } else {
            pos = AlfheimWorldGen.highestFreeBlock(target, sourcePos, AlfheimWorldGen::passReplaceableAndLeaves);
        }
        player.teleport(target, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
        player.func_242279_ag();
    }
    
    @Nullable
    private static BlockPos findBlock(ServerWorld world, BlockPos sourcePos, Block block) {
        BlockPos.Mutable mpos = new BlockPos.Mutable(sourcePos.getX(), world.getHeight(), sourcePos.getZ());
        while (mpos.getY() > 0) {
            mpos.move(Direction.DOWN);
            if (world.getBlockState(mpos).getBlock() == block) {
                return mpos.toImmutable();
            }
        }
        return null;
    }
}
