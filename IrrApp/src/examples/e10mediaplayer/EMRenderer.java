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
		
		/** 更新贴图*/
		player.update();
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		/** 请将资源复制到主sd卡的irrmedia目录下*/
		engine.setResourceDir(Environment.getExternalStorageDirectory(
				).getAbsolutePath() + "/irrmedia/");

		Scene scene = engine.getScene();
		
		/** 向引擎申请一个外部贴图号*/
		int TexId = scene.applyNewExternalTexture(mediaTexName);
		
		if (TexId > 0){
			/** 使用该贴图号新建一个材质媒体播放器*/
			player = new TexMediaPlayer(scene, TexId);
		}
		
		/** 初始化播放器*/
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
		/** 为节点指定外部贴图*/
		cube.setTexture(Engine.EXTERNAL_TEX_MARK + mediaTexName);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
