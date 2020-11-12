package mythicbotany.network;

import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticleHandler {

    public static void handle(ParticleSerializer.ParticleMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if (world == null || !world.func_234923_W_().getRegistryName().equals(msg.dimension))
                return;
            ParticleType<?> particle = ForgeRegistries.PARTICLE_TYPES.getValue(msg.particleId);
            if (particle instanceof BasicParticleType) {
                MythicBotany.getNetwork().spawnParticle(world, (BasicParticleType) particle, msg.amount, msg.x, msg.y, msg.z, msg.xm, msg.ym, msg.zm, msg.xd, msg.yd, msg.zd, msg.randomizePosition);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
