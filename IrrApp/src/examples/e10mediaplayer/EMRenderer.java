package examples.e10mediaplayer;

import java.io.IOException;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.TexMediaPlayer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import android.os.Environment;

public class EMRenderer implements Renderer {
	
	public TexMediaPlayer player;
	public final String mediaTexName = "mediaTex";

	public void onDrawFrame(Engine engine) {
		
		/** ������ͼ*/
		player.update();
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		/** �뽫��Դ���Ƶ���sd����irrmediaĿ¼��*/
		engine.setResourceDir(Environment.getExternalStorageDirectory(
				).getAbsolutePath() + "/irrmedia/");

		Scene scene = engine.getScene();
		
		/** ����������һ���ⲿ��ͼ��*/
		int TexId = scene.applyNewExternalTexture(mediaTexName);
		
		if (TexId > 0){
			/** ʹ�ø���ͼ���½�һ������ý�岥����*/
			player = new TexMediaPlayer(scene, TexId);
		}
		
		/** ��ʼ��������*/
		if (player != null){
			try {
				player.setDataSource("NativeMedia.ts");
			} catch (Exception e) {
				e.printStackTrace();
			}
						
			try {
				player.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		MeshSceneNode cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		cube.addRotationAnimator(new Vector3d(0, 0.1, 0.5));
		/** Ϊ�ڵ�ָ���ⲿ��ͼ*/
		cube.setTexture(Engine.EXTERNAL_TEX_MARK + mediaTexName);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
