package games.enchanted.lightmapview.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import games.enchanted.lightmapview.LightmapView;
import games.enchanted.lightmapview.mixin.access.RenderPipelinesAccess;
import net.minecraft.resources.ResourceLocation;

public class ModRenderPipelines {
    public static final RenderPipeline TEXTURE_VIEW = RenderPipeline
        .builder(RenderPipelinesAccess.egLightmapView$MATRICES_PROJECTION_SNIPPET())
        .withBlend(BlendFunction.TRANSLUCENT)
        .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
        .withSampler("Sampler0")
        .withLocation(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "texture_blit"))
        .withVertexShader(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "core/texture_blit"))
        .withFragmentShader(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "core/texture_blit"))
    .build();
}
