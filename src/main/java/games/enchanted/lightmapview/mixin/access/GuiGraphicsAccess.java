package games.enchanted.lightmapview.mixin.access;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccess {
    @Accessor("guiRenderState")
    GuiRenderState egLightmapView$getGuiRenderState();

    @Accessor("pose")
    Matrix3x2fStack egLightmapView$getPose();
}
