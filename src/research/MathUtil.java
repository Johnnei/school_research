package research;

public class MathUtil {
	
	public static final float degreesToRadians(float degrees) {
		return degrees * (float)(Math.PI / 180d);
	}
	
	public static final float coTangent(float angle) {
		return (float)(1f / Math.tan(angle));
	}

}
