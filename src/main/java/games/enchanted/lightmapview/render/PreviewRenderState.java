package games.enchanted.lightmapview.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.lightmapview.preview.PreviewType;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record PreviewRenderState(RenderPipeline pipeline, PreviewType previewType, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public PreviewRenderState(RenderPipeline pipeline, PreviewType previewType, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        this(pipeline, previewType, pose, x0, y0, x1, y1, scissorArea, calcBounds(pose, x0, y0, x1, y1, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer vertexConsumer, float f) {
        boolean vflip = previewType.vflip();
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y0(), f).setUv(0, vflip ? 1 : 0);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y1(), f).setUv(0, vflip ? 0 : 1);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y1(), f).setUv(1, vflip ? 0 : 1);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y0(), f).setUv(1, vflip ? 1 : 0);
    }

    @Override
    public @NotNull RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public @NotNull TextureSetup textureSetup() {
        return new TextureSetup(previewType.textureViewSupplier.get(), null, null);
    }

    @Override
    public @Nullable ScreenRectangle scissorArea() {
        return null;
    }

    private static @Nullable ScreenRectangle calcBounds(Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        ScreenRectangle screenRectangle2 = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRectangle2) : screenRectangle2;
    }

    public static PreviewRenderState makeState(PreviewType previewType, int width, int height, int x, int y, Matrix3x2f pose) {
        return new PreviewRenderState(
            ModRenderPipelines.TEXTURE_VIEW,
            previewType,
            pose,
            x,
            y,
            x + width,
            y + height,
            new ScreenRectangle(x, y, x + width, y + height)
        );
    }
}
