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

import research.shader.Program;
import research.shader.Shader;
import research.shader.ShaderType;

public class TestOneByOne implements ITest {
	
	private static int SIZE_FLOAT = 4;
	private static int ATTRIB_VERTEX = 0;
	private static int ATTRIB_COLOR = 1;
	
	private Program program;
	
	private int vertexArrayObjectId;
	private int arrayBufferId;
	private Matrix4f models[];

	@Override
	public void init(int cubeCount) throws Exception {
		models = new Matrix4f[cubeCount];
		program = new Program();
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
		
		Random rand = new Random(0);
		for (int i = 0; i < cubeCount; i++) {
			models[i] = new Matrix4f();
			models[i].translate(new Vector2f(-250 + rand.nextInt(500), -250 + rand.nextInt(500)));
			models[i].scale(new Vector3f(10, 10, 1));
		}
		
		vertexArrayObjectId = GL30.glGenVertexArrays();
		arrayBufferId = GL15.glGenBuffers();
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(6 * 7);
		vertices.put(new float[] { 
			0, 0, 0, 1,  1, 0, 0,
			0, 1, 0, 1,  0, 0, 1,
			1, 1, 0, 1,  0, 0, 1,
			
			1, 1, 0, 1,  0, 0, 1,
			1, 0, 0, 1,  1, 0, 0,
			0, 0, 0, 1,  1, 0, 0,
		});
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
		
		for (Matrix4f model : models) {
			model.store(tempBuffer);
			tempBuffer.flip();
			GL20.glUniformMatrix4(program.getUniform("modelMatrix"), false, tempBuffer);
			
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		}
		
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
