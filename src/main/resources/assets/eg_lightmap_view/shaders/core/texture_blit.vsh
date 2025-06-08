#version 150

in vec3 Position;
in vec2 UV0;

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texCoord0 = UV0;
}
