package games.enchanted.lightmapview.mixin;

import games.enchanted.lightmapview.LightmapViewState;
import games.enchanted.lightmapview.mixin.access.GuiGraphicsAccess;
import games.enchanted.lightmapview.render.LightmapViewRenderState;
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
			guiGraphics.guiWidth() - LightmapViewState.lightmapSize - LIGHTMAP_VIEWER_PADDING,
			LIGHTMAP_VIEWER_PADDING
		);
	}

	@Unique
	private void egLightmatView$blitLightmap(GuiGraphics guiGraphics, int width, int height, int x, int y) {
		((GuiGraphicsAccess) guiGraphics).egLightmapView$getGuiRenderState().submitGuiElement(
			new LightmapViewRenderState(
				ModRenderPipelines.LIGHTMAP_VIEW,
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