package mythicbotany.network;

import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdatePortalTimeHandler {
    
    public static void handle(UpdatePortalTimeSerializer.UpdatePortalTimeMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AlfheimPortalHandler.clientInPortalTime = msg.portalTime);
        ctx.get().setPacketHandled(true);
    }
}
