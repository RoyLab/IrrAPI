package examples.e14material;

import zte.irrlib.Engine;
import zte.irrlib.Engine.Renderer;
import zte.irrlib.core.Color3i;
import zte.irrlib.core.Color4i;
import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.MeshSceneNode;
import zte.irrlib.scene.Scene;

public class EMRenderer implements Renderer {
	
	public MeshSceneNode model, cube1, cube2;

	public void onDrawFrame(Engine engine) {
		
		Scene scene = engine.getScene();	
		scene.drawAllNodes();
		
		/** 显示model节点包含多少个材质*/
		scene.drawText("Material Count: " + model.getMaterialCount(),
				new Vector2i(0, 0), new Color4i());
	}
	
	public void onCreate(Engine engine){
		
		engine.addAssetsDir("sysmedia", false);
		Scene scene = engine.getScene();
		scene.setFont(Engine.SYSTEM_MEDIA+"defaultfont.png");
		scene.enableLighting(true);
		
		/** 为了体现材质的差别，我们加入灯效*/
		scene.addLightSceneNode(
				new Vector3d(0, 10, 0), 30, new Color3i(0xff, 0xff, 0xff), null);
		
		model = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "ext_cube.obj", 
				new Vector3d(0, 4, 20), false, null);
		model.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		/** 清空所有的材质*/
		model.setTexture(null);
		
		/** 将第0个材质改为ext_tex1.png，等等*/
		model.setTexture(Engine.SYSTEM_MEDIA + "ext_tex1.png", 0);
		model.setTexture(Engine.SYSTEM_MEDIA + "grass.png", 1);
		model.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp", 2);
		
		/** 让第三个材质的漫反射色变为黄色*/
		model.setDiffuseColor(new Color4i(0xff, 0xff, 0x00, 0xff), 3);
		
		cube1 = scene.addCubeSceneNode(new Vector3d(-4, -4, 20), 5, null);
		
		/** 让cube1（因为它只有一个材质）所有材质应用平滑着色*/
		cube1.setSmoothShade(true, 0);
		cube1.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		cube1.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		cube2 = scene.addCubeSceneNode(new Vector3d(4, -4, 20), 5, null);
		
		/** 让cube2（因为它只有一个材质）所有材质应用非平滑着色*/
		cube2.setSmoothShade(false, 0);
		cube2.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** 关闭了cube2的贴图的双线性滤波*/
		cube2.setMaterialFlag(MeshSceneNode.EMF_BILINEAR_FILTER, false);
		cube2.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		/** 下面就让我们看看它们有哪些差别。注意，CubeSceneNode因为没有规定法向量，
		 * 所以应用光照后会比较奇怪		
		 */
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
