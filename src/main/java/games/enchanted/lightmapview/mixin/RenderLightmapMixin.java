package games.enchanted.lightmapview.mixin;

import games.enchanted.lightmapview.LightmapViewRenderState;
import games.enchanted.lightmapview.ModRenderPipelines;
import games.enchanted.lightmapview.mixin.access.GuiGraphicsAccess;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class RenderLightmapMixin {
	@Unique private static final int LIGHTMAP_SIZE = 64;

	@Inject(
		at = @At("TAIL"),
		method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
	)
	private void renderLightmapToScreen(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		egLightmatView$blitLightmap(guiGraphics, LIGHTMAP_SIZE, LIGHTMAP_SIZE, guiGraphics.guiWidth() - LIGHTMAP_SIZE, 0);
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