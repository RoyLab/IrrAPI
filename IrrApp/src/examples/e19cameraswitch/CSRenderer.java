package examples.e19cameraswitch;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Matrix4;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class CSRenderer implements Renderer {
	
	private CameraSceneNode c1, c2, c3;
	private Scene mScene;
	
	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();
		mScene = scene;
		
		/** 添加两个方形节点，并添加不同的材质*/
		MeshSceneNode cube = scene.addCubeSceneNode(new Vector3d(), 5, null);
		cube.setTexture(Engine.SYSTEM_MEDIA+"b&w.bmp");
		
		/** 添加三个不同的相机，并且使用c1作为激活的相机*/
		c1 = scene.addCameraSceneNode(new Vector3d(-10, 0, -20), new Vector3d(), true, null);
		c2 = scene.addCameraSceneNode(new Vector3d(-10, 0, -20), new Vector3d(), false, null);
		c3 = scene.addCameraSceneNode(new Vector3d(10, 0, -20), new Vector3d(), false, null);
	}

	public void onResize(Engine engine, int width, int height) {
		
		/**
		 *  在绝大部分情况下，我们并不需要在OnResize中添加任何代码，引擎会自动得根据视图大小调整相机的透视投影矩阵。
		 *  然而，如果有需要场景对视图大小的改变做出特别的动作，或者需要使用其他形式的投影矩阵，则需要在OnResize中
		 *  添加自定义内容。
		 *  下面两行代码展示了如何使c2照相机节点应用正交投影。
		 */
		Matrix4 mat = Matrix4.buildProjectionMatrixOrthoLH(width/25.f, height/25.f, 0.1f, 100);
		c2.setProjectionMatrix(mat);
	}
	
	/** 在三个相机之间顺序切换*/
	public void toNextCamera(){
		if (mScene.getActiveCamera() == c1){
			mScene.setActiveCamera(c2);
		}
		else if (mScene.getActiveCamera() == c2){
			mScene.setActiveCamera(c3);
		}
		else if (mScene.getActiveCamera() == c3){
			mScene.setActiveCamera(c1);
		}
	}
}
