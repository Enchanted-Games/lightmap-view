package games.enchanted.lightmapview.mixin.access;

import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightTexture.class)
public interface LightTextureAccess {
    @Accessor("texture")
    GpuTexture egLightmapView$getTexture();
}
