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
		
		/** ����һ��ģ��*/
		model = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "ext_cube.obj", 
				new Vector3d(0, 0, 20), false, null);
		
		/** �ٰ���ɾ��*/
		model.remove();
		
		/** ����Ϊ���ͷ����б�ռ�õ���Դ��������Ҫ�ӻ�����Դ���ɾ������ģ�ͺͼ��ص���ͼ*/
		scene.removeMesh(Engine.SYSTEM_MEDIA + "ext_cube.obj");
		scene.removeTexture(Engine.SYSTEM_MEDIA + "ext_tex1.png");
		scene.removeTexture(Engine.SYSTEM_MEDIA + "ext_tex2.png");
		
		/** ��Ҳ�����ͷŲ���ʹ�õ�ģ��*/
		scene.removeUnusedMesh();
		
		cube = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 5, null);
		cube.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		cube.setTexture(Engine.SYSTEM_MEDIA + "grass.png");
		cube.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** ͬ��������ɾ������ʹ�õ���ͼ*/
		scene.removeTexture(Engine.SYSTEM_MEDIA + "grass.png");
		
		InputStream input = null;
		try {
			/** �������仰��һ������˼*/
			input = engine.getContext().getAssets().open("sysmedia/grass.png");
			//input = engine.getAssets().open("sysmedia/grass.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/** ����һ��Bitmap�����ϴ�������*/
		Bitmap bitmap = BitmapFactory.decodeStream(input);
		
		if (bitmap != null){
			scene.uploadBitmap(bitmap, "myBitmap");
			bitmap.recycle();
		}
		
		/** ɾ�����Bitmap*/
		scene.removeBitmap("myBitmap");
		
		/** �����Ͼ���ͬ�������ã��ڶ���ɾ��ʱ����Ϊ�Դ����Ѿ������ڸ���ͼ�������־�ᱨ��*/
		scene.removeTexture(Engine.BITMAP_MARK + "myBitmap");
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
