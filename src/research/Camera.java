package research;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	/**
	 * The translation of the camera
	 */
	private Vector3f position;
	
	/**
	 * The rotation of the camera
	 */
	private Vector3f rotation;
	
	/**
	 * The cached view matrix
	 */
	private Matrix4f viewMatrix;
	
	/**
	 * If the matrix should be updated
	 */
	private boolean invalidatedMatrix;
	
	/**
	 * The projection which is applied on the camera
	 */
	private Matrix4f cameraProjection;
	
	public Camera() {
		position = new Vector3f(0, 0, 1);
		rotation = new Vector3f(0, 0, 0);
		invalidatedMatrix = true;
		viewMatrix = new Matrix4f();
		cameraProjection = new Matrix4f();
	}
	
	/**
	 * Changes the camera into a orthographic view
	 * @param width
	 * @param height
	 * @param near
	 * @param far
	 */
	public void setOrthoView(float width, float height, float near, float far) {
		cameraProjection.setIdentity();
		cameraProjection.m00 = 1 / (width / 2);
		cameraProjection.m11 = 1 / (height / 2);
		cameraProjection.m22 = 1 / ((far - near) / 2);
		cameraProjection.m32 = -((far + near) / (far - near));

		resetRotation();
		//resetTranslation();
		//translate(new Vector3f(0, 0, -far));
	}
	
	/**
	 * Gets the view matrix based on the camera properties
	 * @return
	 */
	public Matrix4f getViewMatrix() {
		if (invalidatedMatrix) {
			calculateMatrix();
			
			invalidatedMatrix = false;
		}
		
		return viewMatrix;
	}
	
	/**
	 * Gets the matrix which defines how the world-space should be transformed into camera space
	 * @return
	 */
	public Matrix4f getProjectionMatrix() {
		return cameraProjection;
	}
	
	/**
	 * Adds the given vector to the rotation
	 * @param vec
	 */
	public void rotate(ReadableVector3f vec) {
		rotation.x += vec.getX();
		rotation.y += vec.getY();
		rotation.z += vec.getZ();
		invalidatedMatrix = true;
	}
	
	/**
	 * Adds the given vector to the camera translation
	 * @param vec
	 */
	public void translate(ReadableVector3f vec) {
		position.x -= vec.getX();
		position.y -= vec.getY();
		position.z -= vec.getZ();
		invalidatedMatrix = true;
	}
	
	/**
	 * Resets the translation of the camera
	 */
	public void resetTranslation() {
		position.set(0, 0, 0);
		invalidatedMatrix = true;
	}
	
	/**
	 * Resets the rotation of the camera
	 */
	public void resetRotation() {
		rotation.set(0, 0, 0);
		invalidatedMatrix = true;
	}
	
	private void calculateMatrix() {
		viewMatrix.setIdentity();
		viewMatrix.translate(position);
		viewMatrix.rotate(MathUtil.degreesToRadians(rotation.z), new Vector3f(0, 0, 1));
		viewMatrix.rotate(MathUtil.degreesToRadians(rotation.y), new Vector3f(0, 1, 0));
		viewMatrix.rotate(MathUtil.degreesToRadians(rotation.x), new Vector3f(1, 0, 0));
	}
	
	/**
	 * Gets the translation of the camera
	 * @return a vector containing the cloned coordinates of the position
	 */
	public ReadableVector3f getPosition() {
		return new Vector3f(position);
	}
	
	/**
	 * Gets the rotation of the camera
	 * @return a vector containing the cloned rotation components of the camera
	 */
	public ReadableVector3f getRotation() {
		return new Vector3f(rotation);
	}

}
