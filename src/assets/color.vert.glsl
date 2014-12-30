#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

in vec4 in_Position;
in vec3 in_Color;

out vec3 pass_Color;

void main() {
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * in_Position;
	pass_Color = in_Color;
}
