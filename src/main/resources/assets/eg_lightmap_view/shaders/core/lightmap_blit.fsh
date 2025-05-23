#version 150

uniform sampler2D Sampler2;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 texSize = vec2(textureSize(Sampler2, 0));
    vec2 nearestUV = (vec2(ivec2(texCoord0 * texSize)) + 0.5) / texSize;
    fragColor = texture(Sampler2, nearestUV);
}
