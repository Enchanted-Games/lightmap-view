package games.enchanted.lightmapview.preview;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PreviewType {
    private static final GpuSampler sampler = RenderSystem.getSamplerCache().getSampler(AddressMode.REPEAT, AddressMode.REPEAT, FilterMode.NEAREST, FilterMode.NEAREST, false);

    public static final PreviewType LIGHTMAP = new PreviewType(
        () -> Minecraft.getInstance().gameRenderer.lightTexture().getTextureView(),
        () -> sampler
    );

    public final Supplier<@Nullable GpuTextureView> textureViewSupplier;
    public final Supplier<@Nullable GpuSampler> textureSamplerSupplier;
    private final boolean vflip;

    public PreviewType(Supplier<@Nullable GpuTextureView> textureViewSupplier, Supplier<@Nullable GpuSampler> textureSamplerSupplier) {
        this(textureViewSupplier, textureSamplerSupplier, false);
    }
    public PreviewType(Supplier<@Nullable GpuTextureView> textureViewSupplier, Supplier<@Nullable GpuSampler> textureSamplerSupplier, boolean vflip) {
        this.textureViewSupplier = textureViewSupplier;
        this.textureSamplerSupplier = textureSamplerSupplier;
        this.vflip = vflip;
    }

    public boolean vflip() {
        return vflip;
    }
}
