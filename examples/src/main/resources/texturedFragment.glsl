#version 330 core

in vec2 texCoords;
out vec4 FragColor;
uniform sampler2D sampledTexture;

void main() {
    FragColor = texture(sampledTexture, texCoords);
}