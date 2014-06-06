package examples;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

/**
 * 这是个测试用的模块，可以删除
 * @author Roy
 *
 */
public class TestRenderer implements Renderer {
	
	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();
		float a = 50;       
		MeshSceneNode cube = scene.addCubeSceneNode(new Vector3d(0, 0, a+6), a, null); 
		cube.setVisible(true);
		cube.setTexture(Engine.SYSTEM_MEDIA+"b&w.bmp");
	}
  
	public void onResize(Engine engine, int width, int height) {
		                                  
	}
}
