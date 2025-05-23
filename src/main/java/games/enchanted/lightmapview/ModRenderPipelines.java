package games.enchanted.lightmapview;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;

public class ModRenderPipelines {
    private static final VertexFormat POSITION_LIGHTMAP = VertexFormat.builder()
        .add("Position", VertexFormatElement.POSITION)
        .add("UV2", VertexFormatElement.UV2)
    .build();

    public static final RenderPipeline LIGHTMAP_VIEW = RenderPipeline
        .builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
        .withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
        .withSampler("Sampler2")
        .withLocation(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "lightmap_blit"))
        .withVertexShader(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "core/lightmap_blit"))
        .withFragmentShader(ResourceLocation.fromNamespaceAndPath(LightmapView.MOD_ID, "core/lightmap_blit"))
    .build();
}
