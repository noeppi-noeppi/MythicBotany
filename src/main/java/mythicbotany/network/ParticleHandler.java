package mythicbotany.network;

import io.github.noeppi_noeppi.libx.network.NetworkHandler;
import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticleHandler implements NetworkHandler<ParticleHandler.ParticleMessage> {

    @Override
    public Class<ParticleMessage> messageClass() {
        return ParticleMessage.class;
    }

    @Override
    public void encode(ParticleMessage msg, PacketBuffer buf) {
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
    public ParticleMessage decode(PacketBuffer buf) {
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

    @Override
    public void handle(ParticleMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if (world == null || !world.func_234923_W_().getRegistryName().equals(msg.dimension))
                return;
            ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(msg.particleId);
            if (particle instanceof BasicParticleType) {
                MythicBotany.getNetwork().spawnParticle(world, (BasicParticleType) particle, msg.amount, msg.x, msg.y, msg.z, msg.xm, msg.ym, msg.zm, msg.xd, msg.yd, msg.zd, msg.randomizePosition);
            }
        });
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
