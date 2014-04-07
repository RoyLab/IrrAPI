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

		//麻烦补齐一下文档，并且写完这个demo。
		//写完之后，请删除irrapp包内的文件，并且删除androidmanifest.xml中对应的activity注册
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
