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
		 * ���һ����������ҽ����趨Ϊ����������������ʼ��ʱ������һ��Ĭ�������λ��Ϊ(0, 0, 0)����z�������򿴣�
		 * �����������Լ������֮ǰɾ����������ֱ���޸�Ĭ������Ĳ�����Ҳ����ֱ�Ӵ��������������Ϊ�˷����������
		 * ��ɾ��ԭ����������½�һ������������趨��Ϊ����������
		 */
		scene.getActiveCamera().remove();
		camera = scene.addCameraSceneNode(new Vector3d(0, 0, -20), new Vector3d(), true, null);
		
		/**
		 * ���ǽ������װ��һ��FPS����һ�˳��ӽǣ�������ɴ˿���ͨ�������װ�������������Ϸ���������Ƹ������
		 * λ�ú��ӽǡ�Ϊ�˱�֤�ӽ��趨����ȷ�ԣ��벻ҪԽ����װ��ֱ�����������λ�á�Ŀ�ꡢ������������λ���йصĲ�����
		 */
		FPSWrapper = new CameraFPSWrapper(camera);
		
		cube = scene.addCubeSceneNode(new Vector3d(), 5, null);
		cube.setTexture(Engine.SYSTEM_MEDIA_FULL + "b&w.bmp");
		cube.addRotationAnimator(new Vector3d(0, 0.1, 0));
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
