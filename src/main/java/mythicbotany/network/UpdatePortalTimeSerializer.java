package mythicbotany.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.PacketBuffer;

public class UpdatePortalTimeSerializer implements PacketSerializer<UpdatePortalTimeSerializer.UpdatePortalTimeMessage> {

    @Override
    public Class<UpdatePortalTimeMessage> messageClass() {
        return UpdatePortalTimeMessage.class;
    }

    @Override
    public void encode(UpdatePortalTimeMessage msg, PacketBuffer buffer) {
        buffer.writeVarInt(msg.portalTime);
    }

    @Override
    public UpdatePortalTimeMessage decode(PacketBuffer buffer) {
        return new UpdatePortalTimeMessage(buffer.readVarInt());
    }

    public static class UpdatePortalTimeMessage {

        public final int portalTime;

        public UpdatePortalTimeMessage(int portalTime) {
            this.portalTime = portalTime;
        }
    }
}
