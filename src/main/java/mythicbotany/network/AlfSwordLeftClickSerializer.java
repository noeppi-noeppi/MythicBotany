package mythicbotany.network;

import org.moddingx.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class AlfSwordLeftClickSerializer implements PacketSerializer<AlfSwordLeftClickSerializer.AlfSwordLeftClickMessage> {

    @Override
    public Class<AlfSwordLeftClickMessage> messageClass() {
        return AlfSwordLeftClickMessage.class;
    }

    @Override
    public void encode(AlfSwordLeftClickMessage msg, FriendlyByteBuf buffer) {
        //
    }

    @Override
    public AlfSwordLeftClickMessage decode(FriendlyByteBuf buffer) {
        return new AlfSwordLeftClickMessage();
    }

    public static class AlfSwordLeftClickMessage {

    }
}
