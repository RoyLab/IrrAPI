package examples.transformandhierarchy;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ETAHRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		Scene scene = engine.getScene();
		cube1 = scene.addCubeSceneNode(new Vector3d(-3, 0, 20), 5, null);
		/**
		 * 将cube2设定为cube1的子节点
		 */
		cube2 = scene.addCubeSceneNode(new Vector3d(), 2, cube1);
		
		/**
		 * 对cube2进行一些变换：注意，变换有两种模式，一种是相对标定的状态进行变换，
		 * 称为TRANS_RELATIVE，一种是相对零点（即位置(0, 0, 0)，无旋转，无缩放）
		 * 进行的变换，称为TRANS_ABSOLUTE。这两种变换均是相对于父节点空间的。父节
		 * 点的变换和子节点的变换叠加之后才是子节点在世界坐标系中的真正变换
		 */
		cube2.setPosition(new Vector3d(0, 6, 0), SceneNode.TRANS_ABSOLUTE);
	}

	public void onResize(Engine engine, int width, int height) {
	}

}
