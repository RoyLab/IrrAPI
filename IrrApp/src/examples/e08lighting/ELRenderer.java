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
		
		/** Ҫʹ�ƹ������Ч������򿪳�����Ч���أ�Ĭ�Ϲرգ�*/
		scene.enableLighting(true);
		
		/** ���һ��ģ�ͽڵ�*/
		cube = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "cube.obj",
				new Vector3d(0, 0, 20), false, null);
		cube.addRotationAnimator(new Vector3d(0, 0.5, 0.1));
		
		/** ��Ҳ����ͨ������ķ������Ƶ����ڵ��Ƿ�Ӧ�õ�Ч*/
		//cube.enableLighting(false);
		
		/** ���һ���ƹ�Ľڵ�*/
		light = scene.addLightSceneNode(new Vector3d(20, 20, 0), 20, new Color3i(0xff, 0x9f, 0x9f), null);
		
		/** ���Ĳ��ֵƹ�����*/
		light.LightData.DiffuseColor = new Color3i(0xff, 0xff, 0xff);
		light.LightData.AmbientColor = new Color3i(0xff, 0x9f, 0x9f);
		
		/** �������ύ����������*/
		light.upLoadLightData();
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
