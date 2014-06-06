package examples.e21animatedmesh;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.AnimateMeshSceneNode;
import zte.irrlib.scene.Scene;

public class AMRenderer implements Renderer {

	private AnimateMeshSceneNode model;
	
	public void onDrawFrame(Engine engine) {
		Scene scene = engine.getScene();
		scene.drawAllNodes();
	}

	public void onCreate(Engine engine) {
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();		
		
		/** 添加一个动态模型节点*/
		model = scene.addAnimateMeshSceneNode(Engine.SYSTEM_MEDIA+"sydney.md2", new Vector3d(10, 0, 100), null);
		model.setTexture(Engine.SYSTEM_MEDIA+"sydney.bmp");
		
		/** 设置动画模式为循环播放*/
		model.setLoopMode(true);
		
		/** 停止动画播放*/
		model.setAnimationSpeed(0.0);  
	}

	public void onResize(Engine engine, int width, int height) {
		   
	}
	
	/** 开始播放动画，设定速度为20帧/s*/
	public void play(){
		if (model != null)
			model.setAnimationSpeed(20.0);
	}
	
	/** 暂停播放动画*/
	public void pause(){
		if (model != null)
			model.setAnimationSpeed(0.0);		
	}
	
	/** 从头开始播放动画*/
	public void rewind(){
		if (model != null)
			model.setCurrentFrame(0);		
	}
}
