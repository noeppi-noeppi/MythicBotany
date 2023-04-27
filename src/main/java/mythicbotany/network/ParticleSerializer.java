package mythicbotany.network;

import org.moddingx.libx.network.PacketSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ParticleSerializer implements PacketSerializer<ParticleSerializer.ParticleMessage> {

    @Override
    public Class<ParticleMessage> messageClass() {
        return ParticleMessage.class;
    }

    @Override
    public void encode(ParticleMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.particleId);
        buf.writeResourceLocation(msg.dimension);
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeInt(msg.amount);
        buf.writeDouble(msg.xm);
        buf.writeDouble(msg.ym);
        buf.writeDouble(msg.zm);
        buf.writeDouble(msg.xd);
        buf.writeDouble(msg.yd);
        buf.writeDouble(msg.zd);
        buf.writeBoolean(msg.randomizePosition);
    }

    @Override
    public ParticleMessage decode(FriendlyByteBuf buf) {
        ParticleMessage msg = new ParticleMessage();
        msg.particleId = buf.readResourceLocation();
        msg.dimension = buf.readResourceLocation();
        msg.x = buf.readDouble();
        msg.y = buf.readDouble();
        msg.z = buf.readDouble();
        msg.amount = buf.readInt();
        msg.xm = buf.readDouble();
        msg.ym = buf.readDouble();
        msg.zm = buf.readDouble();
        msg.xd = buf.readDouble();
        msg.yd = buf.readDouble();
        msg.zd = buf.readDouble();
        msg.randomizePosition = buf.readBoolean();
        return msg;
    }

    public static class ParticleMessage {

        public ParticleMessage() {

        }

        public ParticleMessage(ResourceLocation particleId, ResourceLocation dimension, double x, double y, double z, int amount, double xm, double ym, double zm, double xd, double yd, double zd, boolean randomizePosition) {
            this.particleId = particleId;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.amount = amount;
            this.xm = xm;
            this.ym = ym;
            this.zm = zm;
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
            this.randomizePosition = randomizePosition;
        }

        public ResourceLocation dimension;
        public ResourceLocation particleId;
        public double x, y, z;
        public int amount;
        public double xm, ym, zm;
        public double xd, yd, zd;
        public boolean randomizePosition;
    }
}
