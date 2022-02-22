package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemMagnetImmunityHandler {
    
    public static void handle(ItemMagnetImmunitySerializer.ItemMagnetImmunityMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(msg.entityId);
                if (entity != null) {
                    entity.getPersistentData().putBoolean("PreventRemoteMovement", true);
                    entity.setPos(msg.x, msg.y, msg.z);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
