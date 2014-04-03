package examples.gettingstart;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
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
		scene.addCubeSceneNode(new Vector3d(0, 0, 20), 10, null);
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
