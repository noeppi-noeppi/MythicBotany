package mythicbotany.network;

import org.moddingx.libx.network.PacketSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class PylonSerializer implements PacketSerializer<PylonSerializer.PylonMessage> {

    @Override
    public Class<PylonMessage> messageClass() {
        return PylonMessage.class;
    }

    @Override
    public void encode(PylonMessage msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.dimension);
        buffer.writeBlockPos(msg.pos);
    }

    @Override
    public PylonMessage decode(FriendlyByteBuf buffer) {
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
