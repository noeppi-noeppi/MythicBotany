package mythicbotany.network;

import io.github.noeppi_noeppi.libx.network.PacketSerializer;
import net.minecraft.network.PacketBuffer;

public class AlfSwordLeftClickSerializer implements PacketSerializer<AlfSwordLeftClickSerializer.AlfSwordLeftClickMessage> {

    @Override
    public Class<AlfSwordLeftClickMessage> messageClass() {
        return AlfSwordLeftClickMessage.class;
    }

    @Override
    public void encode(AlfSwordLeftClickMessage msg, PacketBuffer buffer) {
        //
    }

    @Override
    public AlfSwordLeftClickMessage decode(PacketBuffer buffer) {
        return new AlfSwordLeftClickMessage();
    }

    public static class AlfSwordLeftClickMessage {

    }
}
