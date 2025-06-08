package games.enchanted.lightmapview.mixin;

import games.enchanted.lightmapview.LightmapViewState;
import games.enchanted.lightmapview.mixin.access.GuiGraphicsAccess;
import games.enchanted.lightmapview.preview.PreviewType;
import games.enchanted.lightmapview.render.ImageViewRenderState;
import games.enchanted.lightmapview.render.ModRenderPipelines;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class RenderLightmapMixin {
	@Unique
    private static final int LIGHTMAP_VIEWER_PADDING = 8;

	@Inject(
		at = @At("TAIL"),
		method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
	)
	private void renderLightmapToScreen(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if(!LightmapViewState.lightmapEnabled) return;
		egLightmatView$blitLightmap(
			guiGraphics,
			LightmapViewState.lightmapSize,
			LightmapViewState.lightmapSize,
			LIGHTMAP_VIEWER_PADDING,
			LIGHTMAP_VIEWER_PADDING
		);
	}

	@Unique
	private void egLightmatView$blitLightmap(GuiGraphics guiGraphics, int width, int height, int x, int y) {
		PreviewType previewType;
		if(LightmapViewState.lightmapEnabled) {
			previewType = PreviewType.LIGHTMAP;
		}
		else {
			return;
		}
		((GuiGraphicsAccess) guiGraphics).egLightmapView$getGuiRenderState().submitGuiElement(
			new ImageViewRenderState(
				ModRenderPipelines.TEXTURE_VIEW,
				previewType,
				((GuiGraphicsAccess) guiGraphics).egLightmapView$getPose(),
				x,
				y,
				x + width,
				y + height,
				new ScreenRectangle(x, y, x + width, y + height)
			)
		);
	}
}