package examples.e17bbox;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.BoundingBox;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.AnimateMeshSceneNode;
import zte.irrlib.scene.Scene;

public class EBRenderer implements Renderer {
	
	public AnimateMeshSceneNode model;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
		scene.drawText("BoundingBox:", new Vector2i(), new Color4i());
		
		/** 取得这个模型的BoundingBox，由于引擎提供的碰撞检测十分简单，这里使用
		 * BoundingBox可以做稍微复杂的碰撞检测
		 */
		BoundingBox bbox = model.getAbsoluteBoundingBox();
		
		scene.drawText("" + bbox.MinEdge, new Vector2i(0, 40), new Color4i());
		scene.drawText("" + bbox.MaxEdge, new Vector2i(0, 80), new Color4i());
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		model = scene.addAnimateMeshSceneNode(Engine.SYSTEM_MEDIA + "sydney.md2",
				new Vector3d(10, 0, 100), null);
		model.setTexture(Engine.SYSTEM_MEDIA + "sydney.bmp");
		
		/** 显示这个模型的BoundingBox*/
		model.setBBoxVisibility(true);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
