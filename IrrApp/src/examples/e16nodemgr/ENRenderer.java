package examples.e16nodemgr;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.SceneNode;

public class ENRenderer implements Renderer {
	
	public SceneNode group[] = new SceneNode[4];

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();
		
		/** 新建一个空白节点，并加入4个子节点*/
		group[0] = scene.addEmptySceneNode(new Vector3d(-4, 4, 20), null);
		scene.addCubeSceneNode(new Vector3d(-2, -2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(2, -2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(-2, 2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(2, 2, 0), 3, group[0]);
		
		/** 把这个节点的所有直接子节点的材质都设定为b&w.bmp*/
		group[0].do2EveryChild(new SceneNode.TraversalCallback() {
			public void operate(SceneNode node) {
				((MeshSceneNode)node).setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
			}
		});
		
		/** 将这个节点克隆三遍，并放到合适的位置*/
		group[1] = group[0].clone();
		group[2] = group[0].clone();
		group[3] = group[0].clone();
		
		group[1].setPosition(new Vector3d(4, 4, 20), SceneNode.TRANS_ABSOLUTE);
		
		/** 可以通过getChild访问节点的子节点*/
		group[1].getChild(2).setPosition(new Vector3d(), SceneNode.TRANS_ABSOLUTE);
		group[1].addRotationAnimator(new Vector3d(0, 0.5, 0.1));
		
		group[2].setPosition(new Vector3d(4, -4, 20), SceneNode.TRANS_ABSOLUTE);
		group[3].setPosition(new Vector3d(-4, -4, 20), SceneNode.TRANS_ABSOLUTE);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
