package examples.e08lighting;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.LightSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class ELRenderer implements Renderer {
	
	private MeshSceneNode cube;
	private LightSceneNode light;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();		
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/** 要使灯光可以生效，则须打开场景等效开关（默认关闭）*/
		scene.enableLighting(true);
		
		/** 添加一个模型节点*/
		cube = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "cube.obj",
				new Vector3d(0, 0, 20), false, null);
		cube.addRotationAnimator(new Vector3d(0, 0.5, 0.1));
		
		/** 你也可以通过下面的方法控制单个节点是否应用灯效*/
		//cube.enableLighting(false);
		
		/** 添加一个灯光的节点*/
		light = scene.addLightSceneNode(new Vector3d(20, 20, 0), 20, new Color3i(0xff, 0x9f, 0x9f), null);
		
		/** 更改部分灯光属性*/
		light.LightData.DiffuseColor = new Color3i(0xff, 0xff, 0xff);
		light.LightData.AmbientColor = new Color3i(0xff, 0x9f, 0x9f);
		
		/** 将更改提交到本地引擎*/
		light.upLoadLightData();
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
