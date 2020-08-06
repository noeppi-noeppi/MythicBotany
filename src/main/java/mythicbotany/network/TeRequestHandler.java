package mythicbotany.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class TeRequestHandler implements MythicHandler<TeRequestHandler.TeRequestMessage> {

    @Override
    public Class<TeRequestMessage> messageClass() {
        return TeRequestMessage.class;
    }

    @Override
    public void encode(TeRequestMessage msg, PacketBuffer buffer) {
        buffer.writeResourceLocation(msg.dimension);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public TeRequestMessage decode(PacketBuffer buffer) {
        TeRequestMessage msg = new TeRequestMessage();
        msg.dimension = buffer.readResourceLocation();
        msg.pos = buffer.readBlockPos();
        return msg;
    }

    @Override
    public void handle(TeRequestMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if (sender == null || !sender.getServerWorld().func_234923_W_().getRegistryName().equals(msg.dimension))
                return;
            ServerWorld world = sender.getServerWorld();
            //noinspection deprecation
            if (world.isBlockLoaded(msg.pos)) {
                MythicNetwork.updateTE(PacketDistributor.PLAYER.with(() -> sender), world, msg.pos);
            }
        });
    }

    public static class TeRequestMessage {

        public TeRequestMessage() {

        }

        public TeRequestMessage(ResourceLocation dimension, BlockPos pos) {
            this.pos = pos;
            this.dimension = dimension;
        }

        public ResourceLocation dimension;
        public BlockPos pos;
    }
}
