package mythicbotany.alfheim.teleporter;

import com.mojang.blaze3d.systems.RenderSystem;
import mythicbotany.MythicBotany;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.config.MythicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import org.moddingx.libx.render.RenderHelper;

import java.util.*;

public class AlfheimPortalHandler {

    public static int clientInPortalTime;
    
    private static final Set<ServerPlayer> inPortal = new HashSet<>();
    private static final Set<ServerPlayer> portalBlocked = new HashSet<>();
    private static final Map<ServerPlayer, Integer> timesInPortal = new HashMap<>();

    public static void serverStarted(ServerStartedEvent event) {
        inPortal.clear();
        portalBlocked.clear();
        timesInPortal.clear();
    }
    
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
            
            itr = portalBlocked.iterator();
            while (itr.hasNext()) {
                ServerPlayer player = itr.next();
                if (canRemovePortalBlocked(player)) {
                    MythicBotany.getNetwork().updatePortalTime(player, 0);
                    itr.remove();
                }
            }
            
            portalBlocked.removeIf(AlfheimPortalHandler::canRemovePortalBlocked);
            inPortal.clear();
        }
    }
    
    private static boolean canRemovePortalBlocked(ServerPlayer player) {
        if (player.isOnPortalCooldown()) return false;
        if (inPortal.contains(player)) return false;
        // Only remove portal blocked state when the chunk of the player is loaded
        // so the block entities can update the inPortal state through setInPortal
        BlockPos pos = player.blockPosition();
        return player.level().isOutsideBuildHeight(pos) || player.level().isLoaded(pos);
    }
    
    public static boolean setInPortal(Level level, Player playerEntity) {
        if (!level.isClientSide && (MythicConfig.enableAlfheim || Objects.equals(level.dimension(), Alfheim.DIMENSION)) && playerEntity instanceof ServerPlayer player) {
            inPortal.add(player);
            if (!portalBlocked.contains(player) && !playerEntity.isOnPortalCooldown()) {
                int timeInPortal;
                if (!timesInPortal.containsKey(player)) {
                    timesInPortal.put(player, 1);
                    timeInPortal = 1;
                } else {
                    timeInPortal = timesInPortal.get(player) + 1;
                    timesInPortal.put(player, timeInPortal);
                }
                MythicBotany.getNetwork().updatePortalTime(player, timeInPortal);
                if (timeInPortal >= 120) {
                    portalBlocked.add(player);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean shouldCheck(Level level) {
        if (!MythicConfig.enableAlfheim && !Objects.equals(level.dimension(), Alfheim.DIMENSION)) {
            return false;
        } else if (level instanceof ServerLevel) {
            return !timesInPortal.isEmpty() || !portalBlocked.isEmpty() || ((ServerLevel) level).getServer().getTickCount() % 4 == 3;
        } else {
            return false;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void renderGameOverlay(RenderGuiEvent.Post event) {
        if (clientInPortalTime > 0 && !(Minecraft.getInstance().screen instanceof PauseScreen)) {
            GuiGraphics graphics = event.getGuiGraphics();
            graphics.pose().pushPose();
            int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            float scale = Math.max(w, h) / 48f;
            //noinspection IntegerDivisionInFloatingPointContext
            graphics.pose().translate(w < h ? (h - w) / -2 : 0, w > h ? (w - h) / -2 : 0, 0);
            graphics.pose().scale(scale, scale, scale);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1, 1, 1, Mth.clamp((clientInPortalTime + Minecraft.getInstance().getFrameTime()) / 120f, 0.05f, 0.8f));
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("botania", "block/alfheim_portal_swirl"));
            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
            graphics.blit(0, 0, 0, 48, 48, sprite);
            RenderHelper.resetColor();
            RenderSystem.disableBlend();
            graphics.pose().popPose();
        }
    }
}
