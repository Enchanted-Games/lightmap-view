package games.enchanted.lightmapview.mixin;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.nio.file.Path;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Inject(
        at = @At("TAIL"),
        method = "dumpAllSheets"
    )
    private void egLightmapView$dumpLightmap(Path path, CallbackInfo ci) {
        GpuTextureView textureView = Minecraft.getInstance().gameRenderer.lightTexture().getTextureView();
        GpuDevice device = RenderSystem.getDevice();
        int lightmapSize = textureView.texture().getFormat().pixelSize() * 16 * 16;

        GpuTexture scratchTexture = device.createTexture("Light Texture", GpuBuffer.USAGE_COPY_SRC, TextureFormat.RGBA8, 16, 16, 1, 1);
        device.createCommandEncoder().copyTextureToTexture(textureView.texture(), scratchTexture, 0, 0, 0, 0, 0, 16, 16);

        GpuBuffer buffer = RenderSystem.getDevice().createBuffer(() -> "Lightmap buffer", GpuBuffer.USAGE_COPY_DST, lightmapSize);
        device.createCommandEncoder().copyTextureToBuffer(scratchTexture, buffer, 0, () -> {}, 0);
        ByteBuffer outBuffer = ByteBuffer.allocate(lightmapSize);
        device.createCommandEncoder().writeToBuffer(buffer.slice(), outBuffer);
        System.out.println();
    }
}
