package mythicbotany.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import mythicbotany.alfheim.Alfheim;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;

import javax.annotation.Nullable;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Shadow
    @Final
    private VertexFormat skyVertexFormat;

    @Shadow
    @Nullable
    private VertexBuffer starVBO;

    @Unique
    private static boolean isAlfheim() {
        World world = Minecraft.getInstance().world;
        return world != null && Alfheim.DIMENSION.equals(world.getDimensionKey());
    }

    /**
     * Render planets and other extras, after the first invoke to ms.rotate(Y) after getRainStrength is called
     */
    @Inject(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(
                    from = @At(
                            ordinal = 0, value = "INVOKE",
                            target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"
                    )
            ),
            at = @At(
                    shift = At.Shift.AFTER,
                    ordinal = 0,
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/matrix/MatrixStack;rotate(Lnet/minecraft/util/math/vector/Quaternion;)V"
            ),
            require = 0
    )
    private void renderExtras(MatrixStack ms, float partialTicks, CallbackInfo ci) {
        if (isAlfheim() && Minecraft.getInstance().world != null) {
            SkyblockSkyRenderer.renderExtra(ms, Minecraft.getInstance().world, partialTicks, 0);
        }
    }

    /**
     * Make the sun bigger, replace any 30.0F seen before first call to bindTexture
     */
    @ModifyConstant(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")),
            constant = @Constant(floatValue = 30),
            require = 0
    )
    private float makeSunBigger(float oldValue) {
        if (isAlfheim()) {
            return 60;
        } else {
            return oldValue;
        }
    }

    /**
     * Make the moon bigger, replace any 20.0F seen between first and second call to bindTexture
     */
    @ModifyConstant(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            slice = @Slice(
                    from = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"),
                    to = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V")
            ),
            constant = @Constant(floatValue = 20),
            require = 0
    )
    private float makeMoonBigger(float oldValue) {
        if (isAlfheim()) {
            return 60;
        } else {
            return oldValue;
        }
    }

    /**
     * Render lots of extra stars
     */
    @Inject(
            method = "renderSky(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getStarBrightness(F)F"),
            require = 0
    )
    private void renderExtraStars(MatrixStack ms, float partialTicks, CallbackInfo ci) {
        if (isAlfheim() && starVBO != null) {
            SkyblockSkyRenderer.renderStars(skyVertexFormat, starVBO, ms, partialTicks);
        }
    }
    
    /**
     * Remove clouds
     */
    @Inject(
            method = "Lnet/minecraft/client/renderer/WorldRenderer;renderClouds(Lcom/mojang/blaze3d/matrix/MatrixStack;FDDD)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void renderClouds(MatrixStack matrixStackIn, float partialTicks, double viewEntityX, double viewEntityY, double viewEntityZ, CallbackInfo ci) {
        if (isAlfheim()) {
            ci.cancel();
        }
    }
}
