package examples.e07bitmap;

import java.io.IOException;
import java.io.InputStream;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Rect4d;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.MeshSceneNode.E_MATERIAL_TYPE;
import zte.irrlib.scene.Scene;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class EBRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2, cube3, cube4;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();
		
		/** ��ͼƬ���Ƶ�������*/
		scene.drawImage("text", new Rect4i(100, 100, 300, 300), new Rect4d(), true);
		
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/** ���������ȴ�Assets�ж�ȡ����ͼƬ���ֱ�����Ϊtex1, tex2*/
		InputStream out = null;
		try {
			out = engine.getAssets().open("sysmedia/b&w.bmp");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Bitmap tex1 = null;
		if (out != null){
			tex1 = BitmapFactory.decodeStream(out);
		}
		
		try {
			out = engine.getAssets().open("sysmedia/ext_1.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Bitmap tex2 = null;
		if (out != null){
			tex2 = BitmapFactory.decodeStream(out);
		}
		
		/** �ϴ���Щλͼ*/
		scene.uploadBitmap(tex1, "tex1");
		scene.uploadBitmap(tex2, "tex2");
		scene.uploadBitmap(tex2, "tex2");//ddddddddddddddddddddddddddddddddddddddddd
		
		/** ��Ȼ��Ҫ�����ͷ��ڴ�*/
		tex1.recycle();
		tex2.recycle();
		
		/** �������Ǵ���4��������ڵ�*/
		cube1 = scene.addCubeSceneNode(new Vector3d(-7, 7, 20), 5, null);
		cube2 = scene.addCubeSceneNode(new Vector3d(7, 7, 20), 5, null);
		cube3 = scene.addCubeSceneNode(new Vector3d(7, -7, 20), 5, null);
		cube4 = scene.addCubeSceneNode(new Vector3d(-7, -7, 20), 5, null);
		
		/** ���⼸����������ͼ*/
		cube1.setTexture("tex1");
		cube2.setTexture("tex2");
		cube3.setTexture("tex2");
		
		/** �����Ҫ������Կ���ʹ��͸����ͼ����*/
		cube3.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
		/** ��Ҳ�����Լ�����һ��λͼ*/
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		bitmap.eraseColor(0);

		/** ��������*/
		Paint textPaint = new Paint();
		textPaint.setTextSize(32);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0x00, 0x00, 0xff);
		canvas.drawText("Hello World", 16,112, textPaint);
		
		scene.uploadBitmap(bitmap, "text");
		bitmap.recycle();
		
		cube4.setTexture("text");
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
