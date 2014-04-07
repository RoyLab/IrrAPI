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
		
		Scene scene = engine.getScene();
		
		/** Ϊ�˷���۲죬�����ƶ�һ�������λ��*/
		scene.getActiveCamera().setPosition(new Vector3d(-1, 0, 0), SceneNode.TRANS_ABSOLUTE);
		
		/** ���һ��9���鲼����*/
		layout = scene.addNineCubeLayoutSceneNode(new Vector3d(0, 0, 10),
				new Vector3d(5, 5, 1), 0.7, 0.7, null);
		
		/** Ӧ��͸������*/
		layout.enableTransparentTex(true);
		
		/** ����4�����飨ע�⣬����Ǵ�0��ʼ�ģ���ǰ������ͼ��Ϊb&w.bmp*/
		layout.setFrontTexture(Engine.SYSTEM_MEDIA + "b&w.bmp", 3);
		
		/** �����з�����ߵ���ͼ��Ϊgrass.png*/
		layout.setUniTexture(Engine.SYSTEM_MEDIA + "grass.png", NineCubeLayout.LEFT_MATERIAL_ID);
		
		/** ���Ǹ�����ʹ�����нڵ㳯�������λ�õķ���������Ч�����Ǻܺ�*/
		//layout.enableUpdate(scene, true);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
