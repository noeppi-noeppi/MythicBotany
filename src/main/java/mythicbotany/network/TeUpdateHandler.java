package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TeUpdateHandler implements MythicHandler<TeUpdateHandler.TeUpdateMessage> {

    @Override
    public Class<TeUpdateMessage> messageClass() {
        return TeUpdateMessage.class;
    }

    @Override
    public void encode(TeUpdateMessage msg, PacketBuffer buffer) {
        buffer.writeResourceLocation(msg.dimension);
        buffer.writeBlockPos(msg.pos);
        buffer.writeResourceLocation(msg.id);
        buffer.writeCompoundTag(msg.nbt);
    }

    @Override
    public TeUpdateMessage decode(PacketBuffer buffer) {
        TeUpdateMessage msg = new TeUpdateMessage();
        msg.dimension = buffer.readResourceLocation();
        msg.pos = buffer.readBlockPos();
        msg.id = buffer.readResourceLocation();
        msg.nbt = buffer.readCompoundTag();
        return msg;
    }

    @Override
    public void handle(TeUpdateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if (world == null || !world.func_234923_W_().getRegistryName().equals(msg.dimension))
                return;
            TileEntity te = world.getTileEntity(msg.pos);
            if (te != null && msg.id.equals(te.getType().getRegistryName())) {
                te.handleUpdateTag(world.getBlockState(msg.pos), msg.nbt);
            }
        });
    }

    public static class TeUpdateMessage {

        public TeUpdateMessage() {
        }

        public TeUpdateMessage(ResourceLocation dimension, BlockPos pos, ResourceLocation id, CompoundNBT nbt) {
            this.dimension = dimension;
            this.pos = pos;
            this.id = id;
            this.nbt = nbt;
        }

        public ResourceLocation dimension;
        public BlockPos pos;
        public ResourceLocation id;
        public CompoundNBT nbt;
    }
}
