package games.enchanted.lightmapview.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.*;
import games.enchanted.lightmapview.LightmapView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.OptionalInt;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Inject(
        at = @At("TAIL"),
        method = "dumpAllSheets"
    )
    private void egLightmapView$dumpLightmap(Path debugScreenshotsPath, CallbackInfo ci) {
        GpuTextureView lightmapTextureView = Minecraft.getInstance().gameRenderer.lightTexture().getTextureView();
        GpuDevice device = RenderSystem.getDevice();
        CommandEncoder commandEncoder = device.createCommandEncoder();

        final int lightmapTextureDimensions = 16;
        int lightmapSize = lightmapTextureView.texture().getFormat().pixelSize() * lightmapTextureDimensions * lightmapTextureDimensions;

        // build a quad for blitting the lightmap
        GpuBuffer quadData;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 4)){
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            bufferBuilder.addVertex(0.0f, 0.0f, 0.0f);
            bufferBuilder.addVertex(1.0f, 0.0f, 0.0f);
            bufferBuilder.addVertex(1.0f, 1.0f, 0.0f);
            bufferBuilder.addVertex(0.0f, 1.0f, 0.0f);
            try (MeshData meshData = bufferBuilder.buildOrThrow()){
                quadData = device.createBuffer(() -> "Scratch lightmap quad", 32, meshData.vertexBuffer());
            }
        }

        // blit lightmap to scratch texture
        GpuTexture scratchTexture = device.createTexture("Scratch light texture", GpuTexture.USAGE_RENDER_ATTACHMENT + GpuTexture.USAGE_COPY_SRC, TextureFormat.RGBA8, lightmapTextureDimensions, lightmapTextureDimensions, 1, 1);
        GpuTextureView scratchTextureView = device.createTextureView(scratchTexture);
        RenderSystem.AutoStorageIndexBuffer autoStorageIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer indexBuffer = autoStorageIndexBuffer.getBuffer(6);
        try (RenderPass renderPass = commandEncoder.createRenderPass(() -> "Scratch light texture blit", scratchTextureView, OptionalInt.empty())) {
            renderPass.setPipeline(RenderPipelines.TRACY_BLIT);
            renderPass.setVertexBuffer(0, quadData);
            renderPass.setIndexBuffer(indexBuffer, autoStorageIndexBuffer.type());
            renderPass.bindSampler("InSampler", lightmapTextureView);
            renderPass.drawIndexed(0, 0, 6, 1);
        }

        // write scratch data to buffer
        GpuBuffer outputImageBuffer = device.createBuffer(() -> "Lightmap buffer", GpuBuffer.USAGE_COPY_DST + GpuBuffer.USAGE_MAP_READ, lightmapSize);
        commandEncoder.copyTextureToBuffer(scratchTextureView.texture(), outputImageBuffer, 0, () -> {
            try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(outputImageBuffer, true, false)){
                NativeImage nativeImage = new NativeImage(lightmapTextureDimensions, lightmapTextureDimensions, false);
                for (int y = 0; y < lightmapTextureDimensions; ++y) {
                    for (int x = 0; x < lightmapTextureDimensions; ++x) {
                        int col = mappedView.data().getInt((x + y * lightmapTextureDimensions) * scratchTexture.getFormat().pixelSize());
                        nativeImage.setPixelABGR(x, y, col | 0xFF000000);
                    }
                }
                nativeImage.writeToFile(debugScreenshotsPath.resolve("minecraft_lightmap.png"));
                nativeImage.close();
                outputImageBuffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LightmapView.LOGGER.info("Dumped lightmap");

            quadData.close();
            scratchTexture.close();
            scratchTextureView.close();
            outputImageBuffer.close();
        }, 0);
    }
}
