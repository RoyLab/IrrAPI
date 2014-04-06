package examples.e09collision;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class ECRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2, cube3;
	
	public float X, Y;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
		
		scene.drawText("Touched Point: " + X + ", " + "Y",
				new Vector2i(10, 10), new Color4i(0xff, 0xff, 0xff, 0xff));
		
		scene.drawText("Touched Node: " + scene.getTouchedSceneNode(X, Y, null).toString(),
				new Vector2i(50, 10), new Color4i(0xff, 0xff, 0xff, 0xff));
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		cube1 = scene.addCubeSceneNode(new Vector3d(-10, 0, 20), 3, null);
		cube2 = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 3, null);
		cube3 = scene.addCubeSceneNode(new Vector3d(10, 0, 20), 3, null);
		
		cube1.setTexture(Engine.SYSTEM_MEDIA_FULL + "b&w.bmp");
		cube2.setTexture(Engine.SYSTEM_MEDIA_FULL + "b&w.bmp");
		cube3.setTexture(Engine.SYSTEM_MEDIA_FULL + "b&w.bmp");
		
		cube1.setTouchable(true);
		cube1.setTouchable(true);
		cube1.setTouchable(true);
		
		cube2.addFlyStraightAnimator(new Vector3d(-10, 0, 20), new Vector3d(10, 0, 20), 2000, true, true);
		cube2.addCollisionResponseAnimator(cube1, false, false);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
