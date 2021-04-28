package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import vazkii.botania.client.core.handler.MiscellaneousIcons;

import java.util.*;

public class AlfheimPortalHandler {

    public static int clientInPortalTime;
    
    private static final Set<ServerPlayerEntity> inPortal = new HashSet<>();
    private static final Map<ServerPlayerEntity, Integer> timesInPortal = new HashMap<>();

    public static void endTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<ServerPlayerEntity> itr = timesInPortal.keySet().iterator();
            while (itr.hasNext()) {
                ServerPlayerEntity player = itr.next();
                if (!inPortal.contains(player)) {
                    MythicBotany.getNetwork().updatePortalTime(player, 0);
                    itr.remove();
                }
            }
            timesInPortal.keySet().removeIf(p -> !inPortal.contains(p));
            inPortal.clear();
        }
    }
    
    public static boolean setInPortal(World world, PlayerEntity playerEntity) {
        if (!world.isRemote && playerEntity instanceof ServerPlayerEntity && !playerEntity.func_242280_ah()) {
            ServerPlayerEntity player = (ServerPlayerEntity) playerEntity; 
            inPortal.add(player);
            int timeInPortal;
            if (!timesInPortal.containsKey(player)) {
                timesInPortal.put(player, 1);
                timeInPortal = 1;
            } else {
                timeInPortal = timesInPortal.get(player) + 1;
                timesInPortal.put(player, timeInPortal);
            }
            MythicBotany.getNetwork().updatePortalTime(player, timeInPortal);
            return timeInPortal >= Math.max(player.getMaxInPortalTime(), 80);
        } else {
            return false;
        }
    }
    
    public static boolean shouldCheck(World world) {
        if (world instanceof ServerWorld) {
            return !timesInPortal.isEmpty() || ((ServerWorld) world).getServer().getTickCounter() % 4 == 3;
        } else {
            return false;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void renderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && clientInPortalTime > 0 && !(Minecraft.getInstance().currentScreen instanceof IngameMenuScreen)) {
            MatrixStack matrixStack = event.getMatrixStack();
            matrixStack.push();
            int w = Minecraft.getInstance().getMainWindow().getScaledWidth();
            int h = Minecraft.getInstance().getMainWindow().getScaledHeight();
            float scale = Math.max(w, h) / 48f;
            //noinspection IntegerDivisionInFloatingPointContext
            matrixStack.translate(w < h ? (h - w) / -2 : 0, w > h ? (w - h) / -2 : 0, 0);
            matrixStack.scale(scale, scale, scale);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            //noinspection deprecation
            RenderSystem.color4f(1, 1, 1, MathHelper.clamp((clientInPortalTime + Minecraft.getInstance().getRenderPartialTicks()) / 80f, 0.05f, 0.8f));
            //noinspection deprecation
            RenderSystem.pushTextureAttributes();
            Minecraft.getInstance().getTextureManager().bindTexture(MiscellaneousIcons.INSTANCE.alfPortalTex.getAtlasLocation());
            AbstractGui.blit(matrixStack, 0, 0, 0, 48, 48, MiscellaneousIcons.INSTANCE.alfPortalTex.getSprite());
            //noinspection deprecation
            RenderSystem.popAttributes();
            //noinspection deprecation
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.disableBlend();
            matrixStack.pop();
        }
    }
}
