#version 150 core

in vec3 pass_Color;

out vec4 out_Color;

void main() {
	//out_Color = vec4(pass_Color, 1);
	out_Color = vec4(1, 1, 1, 1);
}