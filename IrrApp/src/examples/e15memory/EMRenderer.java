package examples.e15memory;

import java.io.IOException;
import java.io.InputStream;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EMRenderer implements Renderer {
	
	public MeshSceneNode model, cube;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/** 载入一个模型*/
		model = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "ext_cube.obj", 
				new Vector3d(0, 0, 20), false, null);
		
		/** 再把它删除*/
		model.remove();
		
		/** 但是为了释放所有被占用的资源，我们需要从缓存和显存中删除它的模型和加载的贴图*/
		scene.removeMesh(Engine.SYSTEM_MEDIA + "ext_cube.obj");
		scene.removeTexture(Engine.SYSTEM_MEDIA + "ext_tex1.png");
		scene.removeTexture(Engine.SYSTEM_MEDIA + "ext_tex2.png");
		
		/** 它也可以释放不被使用的模型*/
		scene.removeUnusedMesh();
		
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		cube.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		cube.setTexture(Engine.SYSTEM_MEDIA + "grass.png");
		cube.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** 同样，我们删除不再使用的贴图*/
		scene.removeTexture(Engine.SYSTEM_MEDIA + "grass.png");
		
		InputStream input = null;
		try {
			/** 下面两句话是一样的意思*/
			input = engine.getContext().getAssets().open("sysmedia/grass.png");
			//input = engine.getAssets().open("sysmedia/grass.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** 建立一个Bitmap并且上传到引擎*/
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		
		if (bitmap != null){
			scene.uploadBitmap(bitmap, "myBitmap");
			bitmap.recycle();
		}
		
		/** 删除这个Bitmap*/
		scene.removeBitmap("myBitmap");
		
		/** 这句跟上句有同样的作用，第二次删除时，因为显存中已经不存在该贴图，因此日志会报警*/
		scene.removeTexture(Engine.BITMAP_MARK + "myBitmap");
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
