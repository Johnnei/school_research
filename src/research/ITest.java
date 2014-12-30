package research;

import org.lwjgl.util.vector.Matrix4f;

public interface ITest {
	
	public void init(int cubeCount) throws Exception;
	
	public void renderFrame(Matrix4f projection, Matrix4f view);
	
	public void cleanup();

}
