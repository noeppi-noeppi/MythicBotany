package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;
import vazkii.botania.client.fx.WispParticleData;

import java.util.function.Supplier;

public record PylonMessage(BlockPos pos) {
    
    public static class Serializer implements PacketSerializer<PylonMessage> {

        @Override
        public Class<PylonMessage> messageClass() {
            return PylonMessage.class;
        }

        @Override
        public void encode(PylonMessage msg, FriendlyByteBuf buffer) {
            buffer.writeBlockPos(msg.pos());
        }

        @Override
        public PylonMessage decode(FriendlyByteBuf buffer) {
            return new PylonMessage(buffer.readBlockPos());
        }
    }
    
    public static class Handler implements PacketHandler<PylonMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(PylonMessage msg, Supplier<NetworkEvent.Context> ctx) {
            Level level = Minecraft.getInstance().level;
            if (level == null) return true;
            WispParticleData data = WispParticleData.wisp(0.85f, 1f, 0.6f, 0f, 0.25f);
            level.addParticle(data, msg.pos().getX() + 0.25 + (level.random.nextFloat() / 2), msg.pos().getY() + 0.75 + (level.random.nextFloat() / 4), msg.pos().getZ() + 0.25 + (level.random.nextFloat() / 2), 0, 0.3, 0);
            return true;
        }
    }
}
