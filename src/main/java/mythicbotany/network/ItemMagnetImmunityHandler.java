package mythicbotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemMagnetImmunityHandler {
    
    public static void handle(ItemMagnetImmunitySerializer.ItemMagnetImmunityMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            if (world != null) {
                Entity entity = world.getEntityByID(msg.entityId);
                if (entity != null) {
                    entity.getPersistentData().putBoolean("PreventRemoteMovement", true);
                    entity.setPosition(msg.x, msg.y, msg.z);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
