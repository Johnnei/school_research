package research.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public enum ShaderType {
	
	VERTEX(GL20.GL_VERTEX_SHADER, "vert"),
	FRAGMENT(GL20.GL_FRAGMENT_SHADER, "frag"),
	GEOMETRY(GL32.GL_GEOMETRY_SHADER, "geom");
	
	public final int glId;
	public final String extension;
	
	private ShaderType(int type, String extension) {
		this.glId = type;
		this.extension = extension;
	}
	
	@Override
	public String toString() {
		String name = super.toString();
		return String.format("%s%s", name.substring(0, 1), name.substring(1).toLowerCase());
	}
}
