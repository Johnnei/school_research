package research;

import org.lwjgl.opengl.GL11;

public class Debug {
	
	public static final void OpenGLError() {
		int err = GL11.glGetError();
		if (err != 0) {
			new Exception(String.format("OpenGL Error: " + err)).printStackTrace();
			System.exit(1);
		}
	}

}
