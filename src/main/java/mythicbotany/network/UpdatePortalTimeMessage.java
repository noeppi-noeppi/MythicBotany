package mythicbotany.network;

import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record UpdatePortalTimeMessage(int portalTime) {
    
    public static class Serializer implements PacketSerializer<UpdatePortalTimeMessage> {

        @Override
        public Class<UpdatePortalTimeMessage> messageClass() {
            return UpdatePortalTimeMessage.class;
        }

        @Override
        public void encode(UpdatePortalTimeMessage msg, FriendlyByteBuf buffer) {
            buffer.writeVarInt(msg.portalTime());
        }

        @Override
        public UpdatePortalTimeMessage decode(FriendlyByteBuf buffer) {
            return new UpdatePortalTimeMessage(buffer.readVarInt());
        }
    }
    
    public static class Handler implements PacketHandler<UpdatePortalTimeMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(UpdatePortalTimeMessage msg, Supplier<NetworkEvent.Context> ctx) {
            AlfheimPortalHandler.clientInPortalTime = msg.portalTime;
            return true;
        }
    }
}
