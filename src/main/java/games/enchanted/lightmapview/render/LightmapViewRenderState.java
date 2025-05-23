package games.enchanted.lightmapview.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record LightmapViewRenderState(RenderPipeline pipeline, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {

    public LightmapViewRenderState(RenderPipeline pipeline, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        this(pipeline, pose, x0, y0, x1, y1, scissorArea, calcBounds(pose, x0, y0, x1, y1, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer vertexConsumer, float f) {
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y0(), f);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y1(), f);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y1(), f);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y0(), f);
    }

    @Override
    public @NotNull RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public @NotNull TextureSetup textureSetup() {
        return new TextureSetup(null, null, Minecraft.getInstance().gameRenderer.lightTexture().getTextureView());
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }

    private static @Nullable ScreenRectangle calcBounds(Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        ScreenRectangle screenRectangle2 = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRectangle2) : screenRectangle2;
    }
}
