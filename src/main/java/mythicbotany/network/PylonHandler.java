package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;
import vazkii.botania.client.fx.WispParticleData;

import java.util.function.Supplier;

public class PylonHandler {

    public static void handle(PylonSerializer.PylonMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null || !Minecraft.getInstance().level.dimension().getRegistryName().equals(msg.dimension))
                return;
            WispParticleData data = WispParticleData.wisp(0.85f, 1f, 0.6f, 0f, 0.25f);
            Minecraft.getInstance().level.addParticle(data, msg.pos.getX() + 0.25 + (Minecraft.getInstance().level.random.nextFloat() / 2), msg.pos.getY() + 0.75 + (Minecraft.getInstance().level.random.nextFloat() / 4), msg.pos.getZ() + 0.25 + (Minecraft.getInstance().level.random.nextFloat() / 2), 0, 0.3, 0);
        });
        ctx.get().setPacketHandled(true);
    }
}
