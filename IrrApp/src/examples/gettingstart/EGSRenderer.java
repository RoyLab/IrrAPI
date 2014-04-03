package examples.gettingstart;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.Scene;

public class EGSRenderer implements Renderer {

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		
		/**
		 * 添加的节点只有通过drawAllNodes才会被绘制到屏幕上。
		 */
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		/**
		 * 通过getScene方法，可以取得场景管理类
		 */
		Scene scene = engine.getScene();
		
		/**
		 * 大部分场景的搭建工作可以通过场景管理类完成，这个函数向场景中添加
		 * 了一个大小为10的立方体，位置在(0, 0, 20)。注意，引擎使用的是左手系。
		 */
		scene.addCubeSceneNode(new Vector3d(0, 0, 20), 10, null);
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
