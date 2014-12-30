package research.shader;


public class ShaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ShaderException(Shader shader, String message) {
		super(message + " Log: " + shader.getLog());
	}

}
