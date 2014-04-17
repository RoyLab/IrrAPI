package examples.e02config;

import android.os.Environment;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class ECRenderer implements Renderer {
	
	private MeshSceneNode cube;

	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		/**
		 * Ҫ��������ʹ�ò��ʣ�ģ�͵���Դ�ļ�������Ҫָ����Դ�ļ����ڵ�·����
		 * �������ͨ���������д���ָ��·����������Դ�ļ�������sd����irrmediaĿ¼�£�
		 */
		engine.setResourceDir(Environment.getExternalStorageDirectory(
				).getAbsolutePath() + "/irrmedia/");
		
		/**
		 * ��Ҳ����ѡ���assets�ж�ȡ���ʣ�����ȥ�˿������ʵ�ָ��·���µ��鷳��
		 * Ȼ�������ַ���������һ�����ӳ٣�Ŀǰ���ԵĽ��ԼΪ0.2s~0.4s��
		 */
		engine.addAssetsDir("sysmedia", false);
		
		Scene scene = engine.getScene();
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/**
		 * ������Ѿ����ļ�������ָ��Ŀ¼�£���������·����ȡ�ò��ʡ�
		 */
		//cube.setTexture("tex.bmp");
		
		/**
		 * ��Ҳ����ʹ�þ���·����ָ������λ�õĲ��ʣ�����·��������'/'��ͷ��
		 */
		//cube.setTexture("/mnt/sdcard/irrmedia/tex.bmp");
		
		/**
		 * ����ʹ���Ѿ����ӵ�assetsĿ¼�е���Դ��
		 */
		cube.setTexture(Engine.ASSETS_MARK + "sysmedia/b&w.bmp");
		
		/**
		 * �����������assetsĿ¼��ʱ�������·����������������ô����
		 * �������ǲ�������ʹ�ú���·������������Ϊ�����ļ�ϵͳ�������������
		 * ������Ī���������ʾ����
		 */
		//cube.setTexture(Engine.ASSETS_MARK + "b&w.bmp");
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}