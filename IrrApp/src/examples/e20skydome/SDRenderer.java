package examples.e20skydome;

import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class SDRenderer implements Renderer {
	
	public CameraFPSWrapper FPSWrapper;
	private CameraSceneNode camera;
	private MeshSceneNode environTex1, environTex2, terrain;
	
	public void onDrawFrame(Engine engine) {  
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {  
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();		
		
		/** 与例子06一样，我们去掉了默认摄像机，添加了一个FPS相机*/
		scene.getActiveCamera().remove();
		camera = scene.addCameraSceneNode(new Vector3d(0, 30, 30), new Vector3d(0, 0, 0), true, null);
		FPSWrapper = new CameraFPSWrapper(camera);
		
		/** 添加一个穹顶节点，并使其不可见*/
		environTex1 = scene.addSkyDomeSceneNode(Engine.SYSTEM_MEDIA+"dome.jpg", 20, 20, 0.95, 1.3, 500, null);
		environTex1.setVisible(false);
		
		/** 添加一个天空盒节点*/
		environTex2 = scene.addSkyBoxSceneNode(
				Engine.SYSTEM_MEDIA+"up.jpg", 
				Engine.SYSTEM_MEDIA+"bottom.jpg", 
				Engine.SYSTEM_MEDIA+"left.jpg", 
				Engine.SYSTEM_MEDIA+"right.jpg",
				Engine.SYSTEM_MEDIA+"front.jpg", 
				Engine.SYSTEM_MEDIA+"back.jpg",
				null);
		environTex2.setVisible(true);
		
		/** 添加一个地形节点*/
		terrain = scene.addTerrainSceneNode(Engine.SYSTEM_MEDIA+"heightmap.bmp", new Color4i(), 0, null);
		terrain.setPosition(new Vector3d(-400, -70, -400), SceneNode.TRANS_ABSOLUTE);
		terrain.setScale(new Vector3d(4.f, 0.3f, 4.f), SceneNode.TRANS_ABSOLUTE);
		terrain.setTexture(Engine.SYSTEM_MEDIA+"terrain.jpg");
	}

	public void onResize(Engine engine, int width, int height) {
		   
	}
	
	/** 在两种环境贴图之间切换*/
	public void changeCurtain(){
		if (environTex1.getVisibility()){
			environTex1.setVisible(false);
			environTex2.setVisible(true);
		} else {
			environTex2.setVisible(false);
			environTex1.setVisible(true);			
		}
	}
}
