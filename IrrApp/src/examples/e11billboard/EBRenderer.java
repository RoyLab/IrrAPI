package examples.e11billboard;

import zte.irrlib.CameraFPSWrapper;
import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.BillboardGroup;
import zte.irrlib.scene.BillboardSceneNode;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EBRenderer implements Renderer {

	public CameraFPSWrapper FPSWrapper;
	
	private BillboardGroup bbgroup;
	private CameraSceneNode camera;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();		
		scene.drawAllNodes();
		scene.drawText("FPS: " + engine.getFPS(), new Vector2i(), new Color4i(0x7f, 0x7f, 0xbf, 0xff));
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/** 与第6节一样，我们建立一个第一人称相机以便于观察*/
		scene.getActiveCamera().remove(); 
		camera = scene.addCameraSceneNode(new Vector3d(0, 5, -1), new Vector3d(0, 5, 0), true, null);
		FPSWrapper = new CameraFPSWrapper(camera);
		
		/** 新建一个公告板组*/
		bbgroup = scene.addBillboardGroup(new Vector3d(0, 0, 0), null);
		
		/** 设定公告班组的可见范围*/
		bbgroup.setVisibleDistance(0.1, 30);
		
		/** 使得公告班组内节点的可见性根据可见范围在绘制循环中被刷新*/
		bbgroup.enableUpdate(scene, true);
		 
		/** 添加300个节点*/
		for (int i = 0; i < 300; i++){
			BillboardSceneNode node = scene.addBillboardSceneNode(
					new Vector3d((Math.random()-0.5)*100, 0, (Math.random()-0.5)*100),
					new Vector2d(5, 5), null);
			
			/** 应用贴图的透明通道，如果质量要求不高，也可以使用EMT_TRANSPARENT_ALPHA_CHANNEL_REF*/
			node.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
			node.setTexture(Engine.SYSTEM_MEDIA + "grass.png");
			
			/** 将节点加入公告板组*/
			bbgroup.add(node);
		}
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
