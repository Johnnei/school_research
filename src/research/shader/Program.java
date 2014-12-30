package research.shader;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Program {
	
	/**
	 * The ID for this program
	 */
	public int id;
	/**
	 * The list of names of uniforms which correspond to their GPU Ids
	 */
	private HashMap<String, Integer> uniforms;
	/**
	 * Remembers if the program was compiled
	 */
	private boolean compiled;
	/**
	 * Remembers if the program is currently bound to the OpenGL context
	 */
	private boolean inUse;
	
	public Program() {
		id = GL20.glCreateProgram();
		uniforms = new HashMap<>();
		compiled = false;
		inUse = false;
	}
	
	/**
	 * Attaches the shader to this program
	 * @param shader The shader to attach
	 * @throws ProgramException 
	 */
	public void attachShader(Shader shader) throws ProgramException {
		if (shader == null) {
			throw new ProgramException(this, "Trying to attach shader which is null");
		}
		
		GL20.glAttachShader(id, shader.getId());
	}
	
	/**
	 * Registers an input variable to the program.<br/>
	 * @param variableName the name of variable in the GLSL program
	 * @param attrib The type of data which is expected for this variable
	 * 
	 * @see
	 * {@link VertexArray#ATTR_VERTEX}<br/>
	 * {@link VertexArray#ATTR_COLOR}<br/>
	 * {@link VertexArray#ATTR_TEXTURE}
	 */
	public void attachAttrib(String variableName, int attrib) {
		GL20.glBindAttribLocation(id, attrib, variableName);
	}
	
	/**
	 * Registers an uniform variable to the program.<br/>
	 * These variables are defined as <code>uniform {type} {name}</code> in glsl<br/>
	 * This call only works after {@link #compile()} has been completed successfully
	 * @param name the name part of the variable
	 * @throws ProgramException 
	 */
	public void addUniform(String name) throws ProgramException {
		if (!compiled)
			throw new ProgramException(this, "Registering uniform before compiling");
		
		int uniformId = GL20.glGetUniformLocation(id, name);
		uniforms.put(name, uniformId);
	}
	
	/**
	 * Retrieves a previously register uniform variable to the program.<br/>
	 * These variables are defined as <code>uniform {type} {name}</code> in glsl<br/>
	 * This call only works after {@link #compile()} has been completed successfully
	 * @param name the name part of the variable
	 * @throws ProgramException 
	 */
	public int getUniform(String name) {
		if (!compiled)
			return -1;
		
		return uniforms.get(name);
	}
	
	/**
	 * Compiles the program onto the GPU and makes {@link #addUniform(String)} available for use.
	 * @throws ProgramException 
	 */
	public void compile() throws ProgramException {
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
		compiled = true;
		
		boolean link = GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL11.GL_TRUE;
		boolean validate = GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == GL11.GL_TRUE;
		
		if (!link || !validate) {
			int length = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
			System.out.println(GL20.glGetProgramInfoLog(id, length));
		}
	}
	
	/**
	 * Uses this program for rendering
	 * @throws ProgramException 
	 */
	public void use() {
		if (!compiled) {
			System.out.println("Program got used before it was compiled.");
			return;
		}
		
		if (inUse) {
			return;
		}
		
		GL20.glUseProgram(id);
	}
	
	/**
	 * Unbinds the program from the OpenGL Context
	 */
	public void unbind() {
		inUse = false;
		GL20.glUseProgram(GL11.GL_NONE);
	}
	
	@Override
	public String toString() {
		return String.format("Program(id = %s, compiled = %s, uniformCount = %s)", id, compiled, uniforms.size());
	}

}
