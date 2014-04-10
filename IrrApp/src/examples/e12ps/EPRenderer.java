package examples.e12ps;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.ParticleSystemSceneNode;
import zte.irrlib.core.BoxEmitter;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Color4i;

public class EPRenderer implements Renderer {
	private ParticleSystemSceneNode circles[];
	private BoxEmitter em;
	
	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();		
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		
		/** 设置黑色背景增强加色透明效果 */
		scene.setClearColor(new Color4i(0,0,0,255));
		
		/** 设置发射器的各个参数值*/
		em = new BoxEmitter();
		em.MaxEmitRate = 100;
		em.MinEmitRate = 50;
		em.MinSize = new Vector2d(1.0,1.0);
		em.MaxSize = new Vector2d(1.0,1.0);
		em.MinLifeTime = 8000;
		em.MaxLifeTime = 10000;
		em.InitialDirection = (new Vector3d(0,0.0,0.0));
		em.BBox = new double[]{-2,-2,-1,2,2,1};
		
		/** 初始化粒子系统节点 */
		circles = new ParticleSystemSceneNode[5];
		
		for(int i=0;i<5;++i){
			/**添加粒子系统节点*/
			circles[i] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
			
			/**将粒子系统节点和发射器绑定*/
			circles[i].setBoxEmitter(em);
			
			/**设置加色透明的材质类型将材质黑色部分视为透明*/
			circles[i].setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
			
			/**设置材质贴图*/
			circles[i].setTexture(Engine.SYSTEM_MEDIA + "portal1.bmp");
		}
		
		/** 排列出五环图像 ,并且为每个粒子系统节点添加旋转影响因子
		 * 使得粒子运动轨迹为围绕圆心的环状运动 */
		float zVal = 30;
		circles[0].setPosition(new Vector3d(-6,10,zVal), 0);
		circles[0].addRotationAffector(new Vector3d(0.0,0.0,50), new Vector3d(-6,5.0,zVal));
		circles[1].setPosition(new Vector3d(0,10.0,zVal), 0);
		circles[1].addRotationAffector(new Vector3d(0.0,0.0,50), new Vector3d(0.0,5.0,zVal));
		circles[2].setPosition(new Vector3d(6,10.0,zVal), 0);
		circles[2].addRotationAffector(new Vector3d(0.0,0.0,50), new Vector3d(6,5.0,zVal));
		circles[3].setPosition(new Vector3d(-3,5.0,zVal), 0);
		circles[3].addRotationAffector(new Vector3d(0.0,0.0,50), new Vector3d(-3.0,-0.0,zVal));
		circles[4].setPosition(new Vector3d(3.5,5.0,zVal), 0);
		circles[4].addRotationAffector(new Vector3d(0.0,0.0,50), new Vector3d(3.0,-0.0,zVal));
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
