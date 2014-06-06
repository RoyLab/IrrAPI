package examples.e13cubelayout;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.NineCubeLayout;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ECLRenderer implements Renderer {
	
	public NineCubeLayout layout;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();
		
		/** 为了方便观察，我们移动一下相机的位置*/
		scene.getActiveCamera().setPosition(new Vector3d(-1, 0, 0), SceneNode.TRANS_ABSOLUTE);
		
		/** 添加一个9方块布局类*/
		layout = scene.addNineCubeLayoutSceneNode(new Vector3d(0, 0, 10),
				new Vector3d(5, 5, 1), 0.7, 0.7, null);
		
		/** 应用透明材质*/
		layout.enableTransparentTex(true);
		
		/** 将第4个方块（注意，编号是从0开始的）的前方的贴图改为b&w.bmp*/
		layout.setFrontTexture(Engine.SYSTEM_MEDIA + "b&w.bmp", 3);
		
		/** 将所有方块左边的贴图改为grass.png*/
		layout.setUniTexture(Engine.SYSTEM_MEDIA + "grass.png", NineCubeLayout.LEFT_MATERIAL_ID);
		
		/** 我们给出了使得所有节点朝向相机的位置的方法，不过效果不是很好*/
		//layout.enableUpdate(scene, true);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
