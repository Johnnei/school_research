package research;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import research.shader.Program;
import research.shader.Shader;
import research.shader.ShaderType;

public class TestGrouped implements ITest {
	
	private static int SIZE_FLOAT = 4;
	private static int ATTRIB_VERTEX = 0;
	private static int ATTRIB_COLOR = 1;
	
	private Program program;
	
	private int vertexArrayObjectId;
	private int arrayBufferId;
	private int cubeCount;

	@Override
	public void init(int cubeCount) throws Exception {
		this.cubeCount = cubeCount;
		this.program = new Program();
		Shader shaderVertex = new Shader(ShaderType.VERTEX, "/assets/color.vert.glsl");
		Shader shaderFragment = new Shader(ShaderType.FRAGMENT, "/assets/color.frag.glsl");
		
		program.attachShader(shaderVertex);
		program.attachShader(shaderFragment);
		program.attachAttrib("in_Position", ATTRIB_VERTEX);
		program.attachAttrib("in_Color", ATTRIB_COLOR);
		program.compile();
		program.addUniform("projectionMatrix");
		program.addUniform("viewMatrix");
		program.addUniform("modelMatrix");
		
		vertexArrayObjectId = GL30.glGenVertexArrays();
		arrayBufferId = GL15.glGenBuffers();
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(6 * 7 * cubeCount);
		
		Vector4f leftTop     = new Vector4f(0, 0, 0, 1);
		Vector4f leftBottom  = new Vector4f(0, 1, 0, 1);
		Vector4f rightBottom = new Vector4f(1, 1, 0, 1);
		Vector4f rightTop    = new Vector4f(1, 0, 0, 1);
		
		Random rand = new Random(0);
		
		for (int i = 0; i < cubeCount; i++) {
			Matrix4f modelMatrix = new Matrix4f();
			modelMatrix.translate(new Vector2f(-250 + rand.nextInt(500), -250 + rand.nextInt(500)));
			modelMatrix.scale(new Vector3f(10, 10, 1));
			
			Vector4f modelLeftTop = Matrix4f.transform(modelMatrix, leftTop, null);
			Vector4f modelLeftBottom = Matrix4f.transform(modelMatrix, leftBottom, null);
			Vector4f modelRightBottom = Matrix4f.transform(modelMatrix, rightBottom, null);
			Vector4f modelRightTop = Matrix4f.transform(modelMatrix, rightTop, null);
			
			float[] RED = new float[] { 1, 0, 0 };
			float[] BLUE = new float[] { 0, 0, 1 };
			
			modelLeftTop.store(vertices);
			vertices.put(RED);
			modelLeftBottom.store(vertices);
			vertices.put(BLUE);
			modelRightBottom.store(vertices);
			vertices.put(BLUE);
			modelRightBottom.store(vertices);
			vertices.put(BLUE);
			modelRightTop.store(vertices);
			vertices.put(BLUE);
			modelLeftTop.store(vertices);
			vertices.put(RED);
		}
		vertices.flip();
		
		GL30.glBindVertexArray(vertexArrayObjectId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, arrayBufferId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
		
		GL20.glVertexAttribPointer(ATTRIB_VERTEX, 4, GL11.GL_FLOAT, false, 7 * SIZE_FLOAT, 0); // Vertices, Store in: XY format
		GL20.glVertexAttribPointer(ATTRIB_COLOR, 3, GL11.GL_FLOAT, false, 7 * SIZE_FLOAT, 4 * SIZE_FLOAT); // Color
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GL11.GL_NONE);
		GL30.glBindVertexArray(GL11.GL_NONE);
	}

	@Override
	public void renderFrame(Matrix4f projection, Matrix4f view) {
		program.use();
		
		GL30.glBindVertexArray(vertexArrayObjectId);
		GL20.glEnableVertexAttribArray(ATTRIB_VERTEX);
		GL20.glEnableVertexAttribArray(ATTRIB_COLOR);
		
		FloatBuffer tempBuffer = BufferUtils.createFloatBuffer(16);
		
		projection.store(tempBuffer);
		tempBuffer.flip();
		GL20.glUniformMatrix4(program.getUniform("projectionMatrix"), false, tempBuffer);
		view.store(tempBuffer);
		tempBuffer.flip();
		GL20.glUniformMatrix4(program.getUniform("viewMatrix"), false, tempBuffer);
		
		new Matrix4f().store(tempBuffer); // Identity model matrix
		tempBuffer.flip();
		GL20.glUniformMatrix4(program.getUniform("modelMatrix"), false, tempBuffer);
			
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6 * cubeCount);
		
		// Reset State
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		
		GL30.glBindVertexArray(GL11.GL_NONE);
		program.unbind();
	}

	@Override
	public void cleanup() {
		GL30.glDeleteVertexArrays(vertexArrayObjectId);
		GL15.glDeleteBuffers(arrayBufferId);
	}

}
