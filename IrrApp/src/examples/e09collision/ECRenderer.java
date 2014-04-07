package examples.e09collision;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Rect4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ECRenderer implements Renderer {
	
	private MeshSceneNode cube1, cube2, cube3;
	private SceneNode cubeTouched;
	
	public float X, Y;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
		
		/** ��ʾ�����������*/
		scene.drawText("Point: " + (int)X + ", " + (int)Y,
				new Vector2i(10, 10), new Color4i());
		
		/** ��ʾ�������λ��*/
		scene.drawRectangle(new Rect4i((int)X-2, (int)Y-2, (int)X+2, (int)Y+2), 
				new Color4i(0xff, 0x00, 0x00, 0xff));
		
		/** �жϴ������Ľڵ�*/
		cubeTouched = scene.getTouchedSceneNode(X, Y, null);
		String title = "";
		
		if (cubeTouched == null){
			title = "NULL";
		}
		else if (cubeTouched == cube1){
			title = "cube1";
		}
		else if (cubeTouched == cube2){
			title = "cube2";
		}
		else if (cubeTouched == cube3){
			title = "cube3";
		}
		
		scene.drawText("Touched Node: " + title,
				new Vector2i(10, 50), new Color4i(0xff, 0xff, 0xff, 0xff));
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		cube1 = scene.addCubeSceneNode(new Vector3d(-6, 0, 20), 3, null);
		cube2 = scene.addCubeSceneNode(new Vector3d(0, 0, 20), 3, null);
		cube3 = scene.addCubeSceneNode(new Vector3d(6, 0, 20), 3, null);
		
		cube1.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		cube2.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		cube3.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** ʹ���⼸���ڵ��Ϊ�ɴ����Ľڵ�*/
		cube1.setTouchable(true);
		cube2.setTouchable(true);
		cube3.setTouchable(true);
		
		/** ��cube2�������ط��еĶ���������ʹ������cube1֮�������ײ��Ӧ����cube2�޷�Խ��cube1*/
		cube2.addFlyStraightAnimator(new Vector3d(-6, 0, 20), new Vector3d(6, 0, 20), 2000, true, true);
		cube2.addCollisionResponseAnimator(cube1, false, false);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
