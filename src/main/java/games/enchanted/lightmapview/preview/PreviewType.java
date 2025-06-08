package games.enchanted.lightmapview.preview;

import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PreviewType {
    public static final PreviewType LIGHTMAP = new PreviewType(() -> Minecraft.getInstance().gameRenderer.lightTexture().getTextureView());

    public final Supplier<@Nullable GpuTextureView> textureViewSupplier;
    private final boolean vflip;

    public PreviewType(Supplier<@Nullable GpuTextureView> textureViewSupplier) {
        this.textureViewSupplier = textureViewSupplier;
        vflip = false;
    }
    public PreviewType(Supplier<@Nullable GpuTextureView> textureViewSupplier, boolean vflip) {
        this.textureViewSupplier = textureViewSupplier;
        this.vflip = vflip;
    }

    public boolean vflip() {
        return vflip;
    }
}
