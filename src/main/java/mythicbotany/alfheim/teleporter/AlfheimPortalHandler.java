package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import mythicbotany.MythicBotany;
import mythicbotany.config.MythicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import vazkii.botania.client.core.handler.MiscellaneousModels;

import java.util.*;

public class AlfheimPortalHandler {

    public static int clientInPortalTime;
    
    private static final Set<ServerPlayer> inPortal = new HashSet<>();
    private static final Map<ServerPlayer, Integer> timesInPortal = new HashMap<>();

    public static void endTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<ServerPlayer> itr = timesInPortal.keySet().iterator();
            while (itr.hasNext()) {
                ServerPlayer player = itr.next();
                if (!inPortal.contains(player)) {
                    MythicBotany.getNetwork().updatePortalTime(player, 0);
                    itr.remove();
                }
            }
            timesInPortal.keySet().removeIf(p -> !inPortal.contains(p));
            inPortal.clear();
        }
    }
    
    public static boolean setInPortal(Level level, Player playerEntity) {
        if (!level.isClientSide && playerEntity instanceof ServerPlayer player && !playerEntity.isOnPortalCooldown()) {
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
            return timeInPortal >= Math.max(player.getPortalWaitTime(), 80);
        } else {
            return false;
        }
    }
    
    public static boolean shouldCheck(Level level) {
        if (!MythicConfig.enableAlfheim) {
            return false;
        } else if (level instanceof ServerLevel) {
            return !timesInPortal.isEmpty() || ((ServerLevel) level).getServer().getTickCount() % 4 == 3;
        } else {
            return false;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void renderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && clientInPortalTime > 0 && !(Minecraft.getInstance().screen instanceof PauseScreen)) {
            PoseStack poseStack = event.getMatrixStack();
            poseStack.pushPose();
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            float scale = Math.max(w, h) / 48f;
            //noinspection IntegerDivisionInFloatingPointContext
            poseStack.translate(w < h ? (h - w) / -2 : 0, w > h ? (w - h) / -2 : 0, 0);
            poseStack.scale(scale, scale, scale);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1, 1, 1, Mth.clamp((clientInPortalTime + Minecraft.getInstance().getFrameTime()) / 80f, 0.05f, 0.8f));
            RenderSystem.setShaderTexture(0, MiscellaneousModels.INSTANCE.alfPortalTex.atlasLocation());
            GuiComponent.blit(poseStack, 0, 0, 0, 48, 48, MiscellaneousModels.INSTANCE.alfPortalTex.sprite());
            RenderHelper.resetColor();
            RenderSystem.disableBlend();
            poseStack.popPose();
            
        }
    }
}
