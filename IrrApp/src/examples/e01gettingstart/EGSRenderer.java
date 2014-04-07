package examples.e01gettingstart;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
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
		MeshSceneNode cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/** 如果你不再需要该节点，可以将其从场景中去除。下面两个方法是等价的*/
		//scene.removeNode(cube);
		//cube.remove();
		
		/** 如果你不希望该节点被显示出来，你可以设定其为不可见 */
		//cube.setVisible(false);
		cube.setVisible(true);
		
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
