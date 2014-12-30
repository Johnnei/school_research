#version 150 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

in vec4 in_Position;
in vec3 in_Color;
in mat4 in_ModelMatrix;

out vec3 pass_Color;

void main() {
	gl_Position = projectionMatrix * viewMatrix * in_ModelMatrix * in_Position;
	pass_Color = in_Color;
}
