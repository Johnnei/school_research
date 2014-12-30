package research.shader;


public class ProgramException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProgramException(Program program, String message) {
		super(message);
	}
}
