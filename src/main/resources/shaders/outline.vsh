#version 120

in vec4 Position;

uniform mat4 ProjMat;
uniform vec2 InSize = vec2(1.0, 1.0);

varying vec2 texCoord;
varying vec2 oneTexel;

void main() {
    vec2 OutSize = vec2(1.0, 1.0);

    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);

    oneTexel = 1.0 / InSize;

    texCoord = Position.xy / OutSize;
}
