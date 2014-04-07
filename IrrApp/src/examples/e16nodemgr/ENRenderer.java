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
		
		Scene scene = engine.getScene();
		
		/** �½�һ���հ׽ڵ㣬������4���ӽڵ�*/
		group[0] = scene.addEmptySceneNode(new Vector3d(-4, 4, 20), null);
		scene.addCubeSceneNode(new Vector3d(-2, -2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(2, -2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(-2, 2, 0), 3, group[0]);
		scene.addCubeSceneNode(new Vector3d(2, 2, 0), 3, group[0]);
		
		/** ������ڵ������ֱ���ӽڵ�Ĳ��ʶ��趨Ϊb&w.bmp*/
		group[0].do2EveryChild(new SceneNode.TraversalCallback() {
			public void operate(SceneNode node) {
				((MeshSceneNode)node).setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
			}
		});
		
		/** ������ڵ��¡���飬���ŵ����ʵ�λ��*/
		group[1] = group[0].clone();
		group[2] = group[0].clone();
		group[3] = group[0].clone();
		
		group[1].setPosition(new Vector3d(4, 4, 20), SceneNode.TRANS_ABSOLUTE);
		
		/** ����ͨ��getChild���ʽڵ���ӽڵ�*/
		group[1].getChild(2).setPosition(new Vector3d(), SceneNode.TRANS_ABSOLUTE);
		group[1].addRotationAnimator(new Vector3d(0, 0.5, 0.1));
		
		group[2].setPosition(new Vector3d(4, -4, 20), SceneNode.TRANS_ABSOLUTE);
		group[3].setPosition(new Vector3d(-4, -4, 20), SceneNode.TRANS_ABSOLUTE);
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
