package examples.e04animator;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EARenderer implements Renderer {
	
	private MeshSceneNode cube;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		
		Scene scene = engine.getScene();
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/** ��assets��ȡ���ܴ򿪵�����£�assetsϵͳ����Ŀ¼�ᱻ�Զ����أ�����ֱ��ʹ��*/
		cube.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** ���Ը��ڵ���Ӹ��ָ����Ķ���Ч��*/
		cube.addRotationAnimator(new Vector3d(0, 0, 0.3));

		/** ���������һ��Ч��*/
		cube.addFlyStraightAnimator(new Vector3d(0, -10, 20), new Vector3d(0, 10, 20), 2000, true, true);
		
		/** ȥ����һ������Ķ���*/
		//cube.removeLastAnimator();
		
		/** ȥ�����еĶ���*/
		//cube.removeAllAnimators();
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
