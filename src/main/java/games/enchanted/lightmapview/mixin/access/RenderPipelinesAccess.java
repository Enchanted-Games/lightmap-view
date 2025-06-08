package games.enchanted.lightmapview.mixin.access;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccess {
    @Accessor("MATRICES_PROJECTION_SNIPPET")
    static RenderPipeline.Snippet egLightmapView$MATRICES_PROJECTION_SNIPPET() {
        throw new AssertionError("MATRICES_PROJECTION_SNIPPET accessor not asserted");
    }
}
