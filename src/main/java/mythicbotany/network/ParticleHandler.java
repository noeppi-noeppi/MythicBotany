package mythicbotany.network;

import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticleHandler {

    public static void handle(ParticleSerializer.ParticleMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level == null || !level.dimension().getRegistryName().equals(msg.dimension))
                return;
            ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(msg.particleId);
            if (particle instanceof SimpleParticleType) {
                MythicBotany.getNetwork().spawnParticle(level, (SimpleParticleType) particle, msg.amount, msg.x, msg.y, msg.z, msg.xm, msg.ym, msg.zm, msg.xd, msg.yd, msg.zd, msg.randomizePosition);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
