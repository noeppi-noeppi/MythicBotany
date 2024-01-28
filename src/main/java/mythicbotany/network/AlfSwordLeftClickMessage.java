package mythicbotany.network;

import mythicbotany.alftools.AlfsteelSword;
import mythicbotany.register.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;
import org.moddingx.libx.network.PacketHandler;
import org.moddingx.libx.network.PacketSerializer;

import java.util.function.Supplier;

public record AlfSwordLeftClickMessage() {
    
    public static class Serializer implements PacketSerializer<AlfSwordLeftClickMessage> {

        @Override
        public Class<AlfSwordLeftClickMessage> messageClass() {
            return AlfSwordLeftClickMessage.class;
        }

        @Override
        public void encode(AlfSwordLeftClickMessage msg, FriendlyByteBuf buffer) {
            //
        }

        @Override
        public AlfSwordLeftClickMessage decode(FriendlyByteBuf buffer) {
            return new AlfSwordLeftClickMessage();
        }
    }
    
    public static class Handler implements PacketHandler<AlfSwordLeftClickMessage> {

        @Override
        public Target target() {
            return Target.MAIN_THREAD;
        }

        @Override
        public boolean handle(AlfSwordLeftClickMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null && (sender.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == ModItems.alfsteelSword || sender.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == ModItems.alfsteelSword)) {
                ((AlfsteelSword) ModItems.alfsteelSword).trySpawnAlfBurst(sender);
            }
            return true;
        }
    }
}
