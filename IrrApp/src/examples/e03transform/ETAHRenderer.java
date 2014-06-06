package examples.e03transform;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Matrix4;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ETAHRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		
		/**
		 * 将当前变换记录下来。mark()会覆盖之前的记录，以后的变换，如果使用TRANS_RELATIVE
		 * 模式，则相对于记录的这个状态进行变换
		 */
		cube1.mark();
		
		/**
		 * mark()和TRANS_RELATIVE一起，可以产生步进的效果
		 */
		cube1.setRotation(new Vector3d(0, 0.1, 0), SceneNode.TRANS_RELATIVE);
		
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		engine.addAssetsDir("sysmedia", false);
		
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
		
		/** 使用矩阵进行变换*/
		Matrix4 mat = new Matrix4();
		mat.makeIdentity();
		mat.setTranslation(new Vector3d(1, 0, 0));
		mat.setRotationDegrees(new Vector3d());
		mat.setScale(new Vector3d(1, 1, 3));
		cube2.mark();
		cube2.setMatrix(mat, SceneNode.TRANS_RELATIVE);
		
		/** 为了显示美观，这里给两个立方体贴图*/
		cube1.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		cube2.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
	}

	public void onResize(Engine engine, int width, int height) {
	}

}
