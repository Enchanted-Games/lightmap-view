package games.enchanted.lightmapview.preview;

import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum PreviewType {
    LIGHTMAP(() -> Minecraft.getInstance().gameRenderer.lightTexture().getTextureView());

    public final Supplier<@Nullable GpuTextureView> textureViewSupplier;

    PreviewType(Supplier<@Nullable GpuTextureView> textureViewSupplier) {
        this.textureViewSupplier = textureViewSupplier;
    }
    PreviewType() {
        this.textureViewSupplier = () -> null;
    }
}
