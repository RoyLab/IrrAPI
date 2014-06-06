package examples.e18psextension;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;
import zte.irrlib.scene.ParticleSystemSceneNode;
import zte.irrlib.core.BoxEmitter;
import zte.irrlib.core.Vector2d;
import zte.irrlib.core.Color4i;

public class EPSERenderer implements Renderer {
	private ParticleSystemSceneNode flame,cometTail,explosion,stars;
	private MeshSceneNode comet;
	private BoxEmitter em;
	
	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();		
		scene.drawAllNodes();
	}
	
	public void onCreate(Engine engine){
		
		engine.addAssetsDir("sysmedia", false);	
		Scene scene = engine.getScene();
		
		/** 设置黑色背景增强加色透明效果 */
		scene.setClearColor(new Color4i(0,0,0,255));
		
		/** 设置火焰发射器的各个参数值*/
		em = new BoxEmitter();
		em.MaxEmitRate = 1000;
		em.MinEmitRate = 500;
		em.MinSize = new Vector2d(1,1);
		em.MaxSize = new Vector2d(2.0,2.0);
		em.MinLifeTime = 100;
		em.MaxLifeTime = 2000;
		em.InitialDirection = (new Vector3d(0,0.004,0.0));	//y轴分量让火焰呈向上燃烧的趋势
		em.MaxAngleDegrees = 30;								//让粒子有一定的出射角度更符合自然界火焰的生成情况
		em.BBox = new double[]{-0.5,-2,-2,0.5,-1.5,2};		//包围盒在x轴上的范围大小应取小一些，使火焰从一个点发出
		
		flame = scene.addParticleSystemSceneNode(new Vector3d(30,-15,50), true, null);
		flame.setBoxEmitter(em);			//将粒子发射器和粒子系统节点绑定
		
		/**为粒子设置材质并根据材质图像设置材质类型，以透明化黑色的背景部分*
		 * 如果材质类型为带alpha通道的图片，如.png格式，材质类型对应改为EMT_TRANSPARENT_ALPHA_CHANNEL*/
		flame.setTexture(Engine.SYSTEM_MEDIA+"fire.bmp");
		flame.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		
		/**为粒子添加渐隐效果，取代直接消失更符合自然界火焰的消失过程*/
		flame.addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 400);
		
		/**让火焰粒子有个膨胀的过程，使之更为逼真*/
		flame.addScaleParticleAffector(new Vector2d(2,2));
		
		/**让火焰粒子最终往起始点上方的某个点聚拢，使火焰不至于呈发散状*/
		flame.addAttractionParticleAffector(new Vector3d(30,-5,50), 3, true, true, true, true);
		
		/**添加彗星拖尾运动效果，首先添加一个水平运动的球体作为彗星*/
		comet = scene.addSphereSceneNode(new Vector3d(0,0,30), 0.5, 16, null);
		comet.addFlyStraightAnimator(new Vector3d(40,5,30), new Vector3d(-40,5,30), 4000, true, false);
		
		/**添加拖尾粒子节点，并将其父节点绑定为刚添加的彗星节点，使拖尾随彗星的运动而运动*/
		cometTail = scene.addCometTailSceneNode(new Vector3d(1.0,0,0),1.0, 10.0, new Vector3d(0.01,0.0,0.0), comet);
		
		cometTail.setTexture(Engine.SYSTEM_MEDIA+"portal1.bmp");	//为粒子设置材质
		
		/**默认的材质类型EMT_TRANSPARENT_ADD_COLOR。
		 * 如果材质类型为带alpha通道的图片，如.png格式，材质类型对应改为EMT_TRANSPARENT_ALPHA_CHANNEL*/
		//cometTail.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
		/**添加爆炸效果的粒子系统节点*/
		explosion = scene.addExplosionParticleSceneNode(new Vector3d(-30,-10,50), 1.0, 10.0, null);
		explosion.setTexture(Engine.SYSTEM_MEDIA+"fire.bmp");		//为粒子设置材质
		
		/**默认的材质类型EMT_TRANSPARENT_ADD_COLOR。
		 * 如果材质类型为带alpha通道的图片，如.png格式，材质类型对应改为EMT_TRANSPARENT_ALPHA_CHANNEL*/
		//explosion.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
		/**添加星光效果的粒子系统节点*/
		stars = scene.addStarsParticleSceneNode(new Vector3d(0,0,50), 1000, null);
		stars.setTexture(Engine.SYSTEM_MEDIA+"portal1.bmp");		//为粒子设置材质
		
		/**默认的材质类型EMT_TRANSPARENT_ADD_COLOR。
		 * 如果材质类型为带alpha通道的图片，如.png格式，材质类型对应改为EMT_TRANSPARENT_ALPHA_CHANNEL*/
		//stars.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
