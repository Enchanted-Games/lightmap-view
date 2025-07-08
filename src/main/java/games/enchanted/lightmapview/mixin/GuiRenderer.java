package games.enchanted.lightmapview.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import games.enchanted.lightmapview.TextureViewState;
import games.enchanted.lightmapview.preview.PreviewType;
import games.enchanted.lightmapview.render.PreviewRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.render.GuiRenderer.class)
public class GuiRenderer {
    @Shadow @Final GuiRenderState renderState;
    @Shadow @Nullable private GpuTextureView itemsAtlasView;

    @Inject(
        at = @At("TAIL"),
        method = "prepareItemElements"
    )
    private void egLightmapView$drawItemsAtlas(CallbackInfo ci) {
        if(!TextureViewState.itemAtlasEnabled) return;
        renderState.submitGuiElement(
            PreviewRenderState.makeState(
                new PreviewType(() -> itemsAtlasView, true),
                TextureViewState.itemsAtlasSize,
                TextureViewState.itemsAtlasSize,
                TextureViewState.PADDING,
                TextureViewState.PADDING,
                new Matrix3x2fStack(12)
            )
        );
    }
}
