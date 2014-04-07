package examples.e04animator;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EARenderer implements Renderer {
	
	private MeshSceneNode cube;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		
		Scene scene = engine.getScene();
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/** 在assets读取功能打开的情况下，assets系统材质目录会被自动加载，可以直接使用*/
		cube.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** 可以给节点添加各种各样的动画效果*/
		cube.addRotationAnimator(new Vector3d(0, 0, 0.3));

		/** 可以添加另一个效果*/
		cube.addFlyStraightAnimator(new Vector3d(0, -10, 20), new Vector3d(0, 10, 20), 2000, true, true);
		
		/** 去除上一个加入的动画*/
		//cube.removeLastAnimator();
		
		/** 去除所有的动画*/
		//cube.removeAllAnimators();
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
