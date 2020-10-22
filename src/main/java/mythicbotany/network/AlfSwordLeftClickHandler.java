package mythicbotany.network;

import mythicbotany.ModItems;
import mythicbotany.alftools.AlfsteelSword;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AlfSwordLeftClickHandler {

    public static void handle(AlfSwordLeftClickSerializer.AlfSwordLeftClickMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.get().getSender();
            if (sender != null && (sender.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == ModItems.alfsteelSword
                    || sender.getItemStackFromSlot(EquipmentSlotType.OFFHAND).getItem() == ModItems.alfsteelSword))
            ((AlfsteelSword) ModItems.alfsteelSword).trySpawnBurst((ctx.get()).getSender());
        });
    }
}
