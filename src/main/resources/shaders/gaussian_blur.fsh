#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 u_BlurDir;
uniform float u_Radius;

void main() {
    float u_Progress = 5.0;

    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
    float progRadius = floor(u_Radius * u_Progress);
    for (float r = -progRadius; r <= progRadius; r += 1.0) {
        vec4 theSample = texture2D(DiffuseSampler, texCoord + oneTexel * r * u_BlurDir);

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