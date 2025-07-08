package games.enchanted.lightmapview.mixin;

import games.enchanted.lightmapview.TextureViewState;
import games.enchanted.lightmapview.mixin.access.GuiGraphicsAccess;
import games.enchanted.lightmapview.preview.PreviewType;
import games.enchanted.lightmapview.render.PreviewRenderState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class RenderLightmapMixin {
	@Inject(
		at = @At("TAIL"),
		method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
	)
	private void renderLightmapToScreen(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if(!TextureViewState.lightmapEnabled) return;
		((GuiGraphicsAccess) guiGraphics).egLightmapView$getGuiRenderState().submitGuiElement(
			PreviewRenderState.makeState(
				PreviewType.LIGHTMAP,
				TextureViewState.lightmapSize,
				TextureViewState.lightmapSize,
				TextureViewState.PADDING,
				TextureViewState.PADDING,
				new Matrix3x2fStack(12)
			)
		);
	}
}