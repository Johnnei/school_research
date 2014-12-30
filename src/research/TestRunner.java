package research;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class TestRunner {
	
	public static void main(String[] args) {
		new TestRunner().runTests(
			new ITest[] {
//				new TestOneByOne(),
				new TestInstanced(),
//				new TestGrouped()
			}
		);
	}
	
	private Camera camera;
	
	public void runTests(ITest[] tests) {
		final int[] cubeCount = new int[] {
				1,
				10,
				100,
				1_000,
				10_000,
				100_000,
				200_000,
				300_000,
				400_000,
				500_000,
				600_000,
				700_000,
				800_000,
				900_000,
				1_000_000
		};
		
		initializeScreen();
		camera = new Camera();
		camera.setOrthoView(1280, 720, 0.1f, 100f);
		
		System.out.println("Test - Cube Count - Duration (ms)");
		
		for (ITest test : tests) {
			for (int cubes : cubeCount) {
				try {
					Display.setTitle(String.format("TestRunner - %s - %d", test.getClass().getSimpleName(), cubes));
					test.init(cubes);
					Debug.OpenGLError();
					
					List<Long> times = new ArrayList<>(5);

					for (int i = 0; i < 10; i++) {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glDepthFunc(GL11.GL_LEQUAL);
						
						long startTime = System.nanoTime();
						test.renderFrame(camera.getProjectionMatrix(), camera.getViewMatrix());
						Debug.OpenGLError();
						Display.update();
						long endTime = System.nanoTime();
						times.add(endTime - startTime);
						
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDisable(GL11.GL_CULL_FACE);
					}
					
					test.cleanup();
					
					System.out.println(String.format("%-15s - %-7d - %.2f", test.getClass().getSimpleName(), cubes, times.get(times.size() / 2) / 1_000_000f));
				} catch (Exception e) {
					System.out.println(String.format("%s - %d - Failed: %s", test.getClass().getSimpleName(), cubes, e.getMessage()));
					e.printStackTrace();
				}
				
				
			}
		}
		
		Display.destroy();
		Mouse.destroy();
		Keyboard.destroy();
	}
	
	private void initializeScreen() {
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAttributes = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.setTitle("Test Runner");
			Display.create(pixelFormat, contextAttributes);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
