package research.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;


public class Shader {
	
	/**
	 * The ID of the shader on the OpenGL context
	 */
	private int id;
	/**
	 * The type of shader
	 */
	private ShaderType type;
	
	public Shader(ShaderType type, String filepath) throws ShaderException {
		this.type = type;
		this.id = GL20.glCreateShader(type.glId);
		load(filepath);
	}
	
	private void load(String filepath) throws ShaderException {
		StringBuilder shaderSource = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(filepath)))) {
			String line;
			while((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
		} catch (NullPointerException | FileNotFoundException e) {
			throw new ShaderException(this, String.format("Failed to locate shader source at %s", new File(filepath).getAbsoluteFile()));
		} catch (IOException e) {
			throw new ShaderException(this, String.format("Read error occured during loading: %s", e.getMessage()));
		}
		
		GL20.glShaderSource(id, shaderSource);
		GL20.glCompileShader(id);
		
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new ShaderException(this, String.format("Failed to compile shader"));
		}
	}
	
	/**
	 * Gets the full log from OpenGL with info about this shader
	 * @return
	 */
	public String getLog() {
		int logLength =  GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
		System.out.println("Log length: " + logLength);
		return GL20.glGetShaderInfoLog(id, logLength);
	}
	
	public String toString() {
		boolean compiled = GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE;
		
		return String.format("%s shader (%s). Compiled: %s", type, id, compiled);
	}

	/**
	 * Retrieves the ID of this shader.<br/>
	 * As these id's should not be tracked by external code the accessibility is package-public.
	 * @return
	 */
	int getId() {
		return id;
	}

}
