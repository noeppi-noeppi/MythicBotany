package mythicbotany.network;

import mythicbotany.ModItems;
import mythicbotany.alftools.AlfsteelSword;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AlfSwordLeftClickHandler {

    public static void handle(AlfSwordLeftClickSerializer.AlfSwordLeftClickMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null && (sender.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == ModItems.alfsteelSword || sender.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == ModItems.alfsteelSword)) {
                ((AlfsteelSword) ModItems.alfsteelSword).trySpawnAlfBurst(sender);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
