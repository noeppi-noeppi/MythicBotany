package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record MagnetImmunityMessage(int entityId, boolean immune, double x, double y, double z) {

    public static class Serializer implements PacketSerializer<MagnetImmunityMessage> {

        @Override
        public Class<MagnetImmunityMessage> messageClass() {
            return MagnetImmunityMessage.class;
        }

        @Override
        public void encode(MagnetImmunityMessage msg, FriendlyByteBuf buffer) {
            buffer.writeInt(msg.entityId());
            buffer.writeBoolean(msg.immune());
            buffer.writeDouble(msg.x());
            buffer.writeDouble(msg.y());
            buffer.writeDouble(msg.z());
        }

        @Override
        public MagnetImmunityMessage decode(FriendlyByteBuf buffer) {
            int entityId = buffer.readInt();
            boolean immune = buffer.readBoolean();
            int x = buffer.readInt();
            int y = buffer.readInt();
            int z = buffer.readInt();
            return new MagnetImmunityMessage(entityId, immune, x, y, z);
        }
    }
    
    public static class Handler implements PacketHandler<MagnetImmunityMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(MagnetImmunityMessage msg, Supplier<NetworkEvent.Context> ctx) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(msg.entityId());
                if (entity != null) {
                    entity.getPersistentData().putBoolean("PreventRemoteMovement", msg.immune());
                    entity.setPos(msg.x(), msg.y(), msg.z());
                }
            }
            return true;
        }
    }
}
