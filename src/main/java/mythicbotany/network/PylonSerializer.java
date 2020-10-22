package mythicbotany.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class PylonSerializer implements PacketSerializer<PylonSerializer.PylonMessage> {

    @Override
    public Class<PylonMessage> messageClass() {
        return PylonMessage.class;
    }

    @Override
    public void encode(PylonMessage msg, PacketBuffer buffer) {
        buffer.writeResourceLocation(msg.dimension);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public PylonMessage decode(PacketBuffer buffer) {
        PylonMessage msg = new PylonMessage();
        msg.dimension = buffer.readResourceLocation();
        msg.pos = buffer.readBlockPos();
        return msg;
    }

    public static class PylonMessage {

        public ResourceLocation dimension;
        public BlockPos pos;

        public PylonMessage() {

        }

        public PylonMessage(ResourceLocation dimension, BlockPos pos) {
            this.dimension = dimension;
            this.pos = pos;
        }
    }
}
