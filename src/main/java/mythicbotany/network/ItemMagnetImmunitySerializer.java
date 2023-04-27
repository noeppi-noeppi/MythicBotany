package mythicbotany.network;

import org.moddingx.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;

public class ItemMagnetImmunitySerializer implements PacketSerializer<ItemMagnetImmunitySerializer.ItemMagnetImmunityMessage> {

    @Override
    public Class<ItemMagnetImmunityMessage> messageClass() {
        return ItemMagnetImmunityMessage.class;
    }

    @Override
    public void encode(ItemMagnetImmunityMessage msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.immune);
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
    }

    @Override
    public ItemMagnetImmunityMessage decode(FriendlyByteBuf buffer) {
        return new ItemMagnetImmunityMessage(buffer.readInt(), buffer.readBoolean(),
                buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static class ItemMagnetImmunityMessage {

        public final int entityId;
        public final boolean immune;
        public final double x;
        public final double y;
        public final double z;

        public ItemMagnetImmunityMessage(int entityId, boolean immune, double x, double y, double z) {
            this.entityId = entityId;
            this.immune = immune;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
