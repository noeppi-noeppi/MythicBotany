package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.botania.client.fx.WispParticleData;

import java.util.function.Supplier;

public class InfusionHandler implements MythicHandler<InfusionHandler.InfusionMessage> {

    @Override
    public Class<InfusionMessage> messageClass() {
        return InfusionMessage.class;
    }

    @Override
    public void encode(InfusionMessage msg, PacketBuffer buf) {
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);
        buf.writeInt(msg.z);
        buf.writeResourceLocation(msg.dimension);
        buf.writeDouble(msg.progress);
        buf.writeInt(msg.fromColor);
        buf.writeInt(msg.toColor);
    }

    @Override
    public InfusionMessage decode(PacketBuffer buf) {
        InfusionMessage msg = new InfusionMessage();
        msg.x = buf.readInt();
        msg.y = buf.readInt();
        msg.z = buf.readInt();
        msg.dimension = buf.readResourceLocation();
        msg.progress = buf.readDouble();
        msg.fromColor = buf.readInt();
        msg.toColor = buf.readInt();
        return msg;
    }

    @Override
    public void handle(InfusionMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            //noinspection ConstantConditions
            if (world == null && !msg.dimension.equals(world.func_234923_W_().getRegistryName()))
                return;

            int ticks = (int) (100.0 * msg.progress);

            int totalSpiritCount = 3;
            double tickIncrement = 360D / totalSpiritCount;

            int speed = 5;
            double wticks = ticks * speed - tickIncrement;

            double r = Math.sin((ticks - 100) / 10D) * 2;
            double g = Math.sin(wticks * Math.PI / 180 * 0.55);

            for (int i = 0; i < totalSpiritCount; i++) {
                double x = msg.x + Math.sin(wticks * Math.PI / 180) * r + 0.5;
                double y = msg.y + 0.25 + Math.abs(r) * 0.7;
                double z = msg.z + Math.cos(wticks * Math.PI / 180) * r + 0.5;

                wticks += tickIncrement;
                System.out.println(msg.progress);
                float fromR = ((msg.fromColor >> 16) & 0xFF) / 255f;
                float fromG = ((msg.fromColor >> 8) & 0xFF) / 255f;
                float fromB = ((msg.fromColor) & 0xFF) / 255f;
                float toR = ((msg.toColor >> 16) & 0xFF) / 255f;
                float toG = ((msg.toColor >> 8) & 0xFF) / 255f;
                float toB = ((msg.toColor) & 0xFF) / 255f;
                System.out.println(fromR + "  " + fromG + "  " + fromB);
                System.out.println(toR + "  " + toG + "  " + toB);
                float[] colorsfx = new float[] {
                        fromR + ((toR - fromR) * (float) msg.progress),
                        fromG + ((toG - fromG) * (float) msg.progress),
                        fromB + ((toB - fromB) * (float) msg.progress)
                };
                System.out.println(colorsfx[0] + "  " + colorsfx[1] + "  " + colorsfx[2]);
                WispParticleData data = WispParticleData.wisp(0.85F, colorsfx[0], colorsfx[1], colorsfx[2], 0.25F);
                world.addParticle(data, x, y, z, 0, (float) (-g * 0.05), 0);
                data = WispParticleData.wisp((float) Math.random() * 0.1F + 0.1F, colorsfx[0], colorsfx[1], colorsfx[2], 0.9F);
                world.addParticle(data, x, y, z, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F, (float) (Math.random() - 0.5) * 0.05F);

                if (ticks == 100) {
                    for (int j = 0; j < 15; j++) {
                        data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, colorsfx[0], colorsfx[1], colorsfx[2]);
                        world.addParticle(data, msg.x + 0.5, msg.y + 0.5, msg.z + 0.5, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F, (float) (Math.random() - 0.5F) * 0.125F);
                    }
                }
            }
        });
    }

    public static class InfusionMessage {

        public InfusionMessage() {

        }

        public InfusionMessage(int x, int y, int z, ResourceLocation dimension, double progress, int fromColor, int toColor) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
            this.progress = progress;
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        public int x, y, z;
        public ResourceLocation dimension;
        public double progress;
        public int fromColor, toColor;
    }
}
