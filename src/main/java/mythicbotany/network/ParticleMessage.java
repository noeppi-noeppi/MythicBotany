package mythicbotany.network;

import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record ParticleMessage(ResourceLocation particleId, double x, double y, double z, int amount, double xm, double ym, double zm, double xd, double yd, double zd, boolean randomizePosition) {
    
    public static class Serializer implements PacketSerializer<ParticleMessage> {

        @Override
        public Class<ParticleMessage> messageClass() {
            return ParticleMessage.class;
        }

        @Override
        public void encode(ParticleMessage msg, FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(msg.particleId());
            buffer.writeDouble(msg.x());
            buffer.writeDouble(msg.y());
            buffer.writeDouble(msg.z());
            buffer.writeInt(msg.amount());
            buffer.writeDouble(msg.xm());
            buffer.writeDouble(msg.ym());
            buffer.writeDouble(msg.zm());
            buffer.writeDouble(msg.xd());
            buffer.writeDouble(msg.yd());
            buffer.writeDouble(msg.zd());
            buffer.writeBoolean(msg.randomizePosition());
        }

        @Override
        public ParticleMessage decode(FriendlyByteBuf buffer) {
            ResourceLocation particleId = buffer.readResourceLocation();
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();
            int amount = buffer.readInt();
            double xm = buffer.readDouble();
            double ym = buffer.readDouble();
            double zm = buffer.readDouble();
            double xd = buffer.readDouble();
            double yd = buffer.readDouble();
            double zd = buffer.readDouble();
            boolean randomizePosition = buffer.readBoolean();
            return new ParticleMessage(particleId, x, y, z, amount, xm, ym, zm, xd, yd, zd, randomizePosition);
        }
    }
    
    public static class Handler implements PacketHandler<ParticleMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(ParticleMessage msg, Supplier<NetworkEvent.Context> ctx) {
            Level level = Minecraft.getInstance().level;
            if (level == null) return true;
            ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(msg.particleId());
            if (particle instanceof SimpleParticleType) {
                MythicBotany.getNetwork().spawnParticle(level, (SimpleParticleType) particle, msg.amount(), msg.x(), msg.y(), msg.z(), msg.xm(), msg.ym(), msg.zm(), msg.xd(), msg.yd(), msg.zd(), msg.randomizePosition());
            }
            return true;
        }
    }
}
