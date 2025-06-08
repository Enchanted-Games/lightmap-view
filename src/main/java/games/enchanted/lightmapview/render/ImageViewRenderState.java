package games.enchanted.lightmapview.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import games.enchanted.lightmapview.preview.PreviewType;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record ImageViewRenderState(RenderPipeline pipeline, @Nullable PreviewType previewType, @Nullable GpuTextureView textureView, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public ImageViewRenderState(RenderPipeline pipeline, PreviewType previewType, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        this(pipeline, previewType, null, pose, x0, y0, x1, y1, scissorArea, calcBounds(pose, x0, y0, x1, y1, scissorArea));
    }

    public ImageViewRenderState(RenderPipeline pipeline, GpuTextureView textureView, Matrix3x2f pose, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        this(pipeline, null, textureView, pose, x0, y0, x1, y1, scissorArea, calcBounds(pose, x0, y0, x1, y1, scissorArea));
    }

    @Override
    public void buildVertices(VertexConsumer vertexConsumer, float f) {
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y0(), f).setUv(0, 0);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y1(), f).setUv(0, 1);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y1(), f).setUv(1, 1);
        vertexConsumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y0(), f).setUv(1, 0);
    }

    @Override
    public @NotNull RenderPipeline pipeline() {
        return pipeline;
    }

    @Override
    public @NotNull TextureSetup textureSetup() {
        if(previewType == null && textureView == null) {
            throw new IllegalStateException("ImageViewRenderState created with no texture view or preview type");
        }
        return new TextureSetup(previewType == null ? textureView : previewType.textureViewSupplier.get(), null, null);
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
