package examples.e06camera;

import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class ECRenderer implements Renderer {
	
	public CameraFPSWrapper FPSWrapper;
	
	private CameraSceneNode camera;
	private MeshSceneNode cube;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/**
		 * 添加一个相机，并且将其设定为激活的相机，场景初始化时，会有一个默认相机，位置为(0, 0, 0)，向z轴正方向看，
		 * 你可以在添加自己的相机之前删除它，或者直接修改默认相机的参数，也可以直接创建新相机。这里为了方便起见，我
		 * 们删除原来的相机，新建一个相机，并且设定其为激活的相机。
		 */
		scene.getActiveCamera().remove();
		camera = scene.addCameraSceneNode(new Vector3d(0, 0, -20), new Vector3d(), true, null);
		
		/**
		 * 我们将相机包装成一个FPS（第一人称视角）相机，由此可以通过这个包装类像所有射击游戏中那样控制该相机的
		 * 位置和视角。为了保证视角设定的正确性，请不要越过包装类直接设置相机的位置、目标、向上向量等与位置有关的参数。
		 */
		FPSWrapper = new CameraFPSWrapper(camera);
		
		cube = scene.addCubeSceneNode(new Vector3d(), 5, null);
		cube.setTexture(Engine.SYSTEM_MEDIA_FULL + "b&w.bmp");
		cube.addRotationAnimator(new Vector3d(0, 0.1, 0));
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
