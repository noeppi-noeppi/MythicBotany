package mythicbotany.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class InfusionSerializer implements PacketSerializer<InfusionSerializer.InfusionMessage> {

    @Override
    public Class<InfusionMessage> messageClass() {
        return InfusionMessage.class;
    }

    @Override
    public void encode(InfusionMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);
        buf.writeInt(msg.z);
        buf.writeResourceLocation(msg.dimension);
        buf.writeDouble(msg.progress);
        buf.writeInt(msg.fromColor);
        buf.writeInt(msg.toColor);
    }

    @Override
    public InfusionMessage decode(FriendlyByteBuf buf) {
        InfusionMessage msg = new InfusionMessage();
        msg.x = buf.readInt();
        msg.y = buf.readInt();
        msg.z = buf.readInt();
        msg.dimension = buf.readResourceLocation();
        msg.progress = buf.readDouble();
        msg.fromColor = buf.readInt();
        msg.toColor = buf.readInt();
        return msg;
    }

    public static class InfusionMessage {

        public InfusionMessage() {

        }

        public InfusionMessage(int x, int y, int z, ResourceLocation dimension, double progress, int fromColor, int toColor) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
            this.progress = progress;
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        public int x, y, z;
        public ResourceLocation dimension;
        public double progress;
        public int fromColor, toColor;
    }
}
