package examples.e12ps;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.LightSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EPRenderer implements Renderer {
	
	private MeshSceneNode cube;
	private LightSceneNode light;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();		
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();

		//�鷳����һ���ĵ�������д�����demo��
		//д��֮����ɾ��irrapp���ڵ��ļ�������ɾ��androidmanifest.xml�ж�Ӧ��activityע��
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
