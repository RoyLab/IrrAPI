package examples.e01gettingstart;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EGSRenderer implements Renderer {
	
	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		
		/**
		 * ��ӵĽڵ�ֻ��ͨ��drawAllNodes�Żᱻ���Ƶ���Ļ�ϡ�
		 */
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		/**
		 * ͨ��getScene����������ȡ�ó���������
		 */
		Scene scene = engine.getScene();
		
		/**
		 * �󲿷ֳ����Ĵ��������ͨ��������������ɣ���������򳡾������
		 * ��һ����СΪ10�������壬λ����(0, 0, 20)��ע�⣬����ʹ�õ�������ϵ��
		 */
		MeshSceneNode cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/** ����㲻����Ҫ�ýڵ㣬���Խ���ӳ�����ȥ�����������������ǵȼ۵�*/
		//scene.removeNode(cube);
		//cube.remove();
		
		/** ����㲻ϣ���ýڵ㱻��ʾ������������趨��Ϊ���ɼ� */
		//cube.setVisible(false);
		cube.setVisible(true);
		
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
