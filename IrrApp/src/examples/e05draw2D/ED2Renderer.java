package examples.e05draw2D;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4d;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.scene.Scene;

public class ED2Renderer implements Renderer {	

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();
		
		/**
		 * 绘制文字，必须在assets读取可用(isNativeAssetsReaderEnabled)的状态下才能生效
		 */
		scene.drawText("FPS: " + engine.getFPS(), new Vector2i(), new Color4i(0x7f, 0x7f, 0xbf, 0xff));
		
		/**
		 * 绘制一张图片的全部或一部分
		 */
		scene.drawImage(Engine.SYSTEM_MEDIA + "b&w.bmp", 
				new Rect4i(100, 100, 300, 300), new Rect4d(0.1, 0.2, 1.0, 0.9), false);
		
		/**
		 * 绘制矩形区域
		 */
		scene.drawRectangle(new Rect4i(200, 100, 400, 300), new Color4i(0xff, 0, 0, 0x7f));
	}

	public void onCreate(Engine engine) {
		
	}

	public void onResize(Engine engine, int width, int height) {
		
	}

}
