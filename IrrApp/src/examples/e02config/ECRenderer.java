package examples.e02config;

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
		 * 要在引擎中使用材质，模型等资源文件，则需要指定资源文件所在的路径，
		 * 比如可以通过下面这行代码指定路径（假设资源文件放在sd卡的media目录下）
		 */
		//engine.setResourceDir("/sdcard/oneDir/");
		
		/**
		 * 你也可以选择从assets中读取材质，这免去了拷贝材质到指定路径下的麻烦。
		 * 然而，这种方法将引起一定的延迟，目前测试的结果约为0.2s~0.4s。
		 */
		engine.addAssetsDir("sysmedia", false);
		
		Scene scene = engine.getScene();
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		
		/**
		 * 如果你已经将文件拷贝到指定目录下，则可以相对路径来取得材质。
		 */
		//cube.setTexture("tex.bmp");
		
		/**
		 * 你也可以使用绝对路径来指向其他位置的材质。
		 */
		//cube.setTexture("/sdcard/anotherDir/tex.bmp");
		
		/**
		 * 或者使用已经添加的assets目录中的资源。
		 */
		cube.setTexture(Engine.ASSETS_PATH + "sysmedia/b&w.bmp");
		
		/**
		 * 如果你在添加assets目录的时候忽略了路径，你甚至可以这么做。
		 * 但是我们并不建议使用忽略路径，它可能导致引擎文件系统里面出现重名，
		 * 而导致莫名其妙的显示错误。
		 */
		//cube.setTexture(Engine.ASSETS_PATH + "b&w.bmp");
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
