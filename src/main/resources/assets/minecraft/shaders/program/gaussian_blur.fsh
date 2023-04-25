#version 110

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;
const uniform float Progress = 5.0;

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
    float progRadius = floor(Radius * Progress);
    for (float r = -progRadius; r <= progRadius; r += 1.0) {
        vec4 theSample = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);

        // Accumulate average alpha
        totalAlpha = totalAlpha + theSample.a;
        totalSamples = totalSamples + 1.0;

        // Accumulate smoothed blur
        float strength = 1.0 - abs(r / progRadius);
        totalStrength = totalStrength + strength;
        blurred = blurred + theSample;
    }
    gl_FragColor = vec4(blurred.rgb / (progRadius * 2.0 + 1.0), totalAlpha);
}