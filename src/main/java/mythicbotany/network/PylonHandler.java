package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.botania.client.fx.WispParticleData;

import java.util.function.Supplier;

public class PylonHandler {

    public static void handle(PylonSerializer.PylonMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world == null || !Minecraft.getInstance().world.func_234923_W_().getRegistryName().equals(msg.dimension))
                return;
            WispParticleData data = WispParticleData.wisp(0.85f, 1f, 0.6f, 0f, 0.25f);
            Minecraft.getInstance().world.addParticle(data, msg.pos.getX() + 0.25 + (Minecraft.getInstance().world.rand.nextFloat() / 2), msg.pos.getY() + 0.75 + (Minecraft.getInstance().world.rand.nextFloat() / 4), msg.pos.getZ() + 0.25 + (Minecraft.getInstance().world.rand.nextFloat() / 2), 0, 0.3, 0);
        });
        ctx.get().setPacketHandled(true);
    }
}
