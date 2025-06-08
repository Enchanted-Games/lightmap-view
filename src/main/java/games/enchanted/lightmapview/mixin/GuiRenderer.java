package games.enchanted.lightmapview.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import games.enchanted.lightmapview.LightmapViewState;
import games.enchanted.lightmapview.render.ImageViewRenderState;
import games.enchanted.lightmapview.render.ModRenderPipelines;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.render.GuiRenderer.class)
public class GuiRenderer {
    @Unique
    private static final int LIGHTMAP_VIEWER_PADDING = 8;

    @Shadow @Final private GuiRenderState renderState;

    @Shadow @Nullable private GpuTextureView itemsAtlasView;

    @Inject(
        at = @At("TAIL"),
        method = "prepareItemElements"
    )
    private void egLightmapView$drawItemsAtlas(CallbackInfo ci) {
        if(!LightmapViewState.itemAtlasEnabled) return;
        egLightmatView$blitItemsAtlas(
            LightmapViewState.lightmapSize,
            LightmapViewState.lightmapSize,
            LIGHTMAP_VIEWER_PADDING,
            LIGHTMAP_VIEWER_PADDING,
            new Matrix3x2fStack(12)
        );
    }

    @Unique
    private void egLightmatView$blitItemsAtlas(int width, int height, int x, int y, Matrix3x2f pose) {
        renderState.submitGuiElement(
            new ImageViewRenderState(
                ModRenderPipelines.TEXTURE_VIEW,
                itemsAtlasView,
                pose,
                x,
                y,
                x + width,
                y + height,
                new ScreenRectangle(x, y, x + width, y + height)
            )
        );
    }
}
