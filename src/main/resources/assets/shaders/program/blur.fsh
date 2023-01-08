#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;

uniform int x1;
uniform int y1;
uniform int x2;
uniform int y2;

out vec4 fragColor;

void main() {

	vec2 screenCoord = gl_FragCoord.xy;
    if (screenCoord.x < x1 || screenCoord.x > x2 || screenCoord.y < y1 || screenCoord.y > y2) {
        fragColor = texture(DiffuseSampler, texCoord);
        return;
    }

    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;
    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        
		// Accumulate average alpha
        totalAlpha = totalAlpha + sampleValue.a;
        totalSamples = totalSamples + 1.0;

		// Accumulate smoothed blur
        float strength = 1.0 - abs(r / Radius);
        totalStrength = totalStrength + strength;
        blurred = blurred + sampleValue;
    }
    fragColor = vec4(blurred.rgb / (Radius * 2.0 + 1.0), totalAlpha);
}

