package examples.transformandhierarchy;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ETAHRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		Scene scene = engine.getScene();
		cube1 = scene.addCubeSceneNode(new Vector3d(-3, 0, 20), 5, null);
		/**
		 * ��cube2�趨Ϊcube1���ӽڵ�
		 */
		cube2 = scene.addCubeSceneNode(new Vector3d(), 2, cube1);
		
		/**
		 * ��cube2����һЩ�任��ע�⣬�任������ģʽ��һ������Ա궨��״̬���б任��
		 * ��ΪTRANS_RELATIVE��һ���������㣨��λ��(0, 0, 0)������ת�������ţ�
		 * ���еı任����ΪTRANS_ABSOLUTE�������ֱ任��������ڸ��ڵ�ռ�ġ�����
		 * ��ı任���ӽڵ�ı任����֮������ӽڵ�����������ϵ�е������任
		 */
		cube2.setPosition(new Vector3d(0, 6, 0), SceneNode.TRANS_ABSOLUTE);
	}

	public void onResize(Engine engine, int width, int height) {
	}

}
