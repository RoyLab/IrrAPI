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
		
		/** ��ʾmodel�ڵ�������ٸ�����*/
		scene.drawText("Material Count: " + model.getMaterialCount(),
				new Vector2i(0, 0), new Color4i());
	}
	
	public void onCreate(Engine engine){
		
		Scene scene = engine.getScene();
		scene.enableLighting(true);
		
		/** Ϊ�����ֲ��ʵĲ�����Ǽ����Ч*/
		scene.addLightSceneNode(
				new Vector3d(0, 10, 0), 30, new Color3i(0xff, 0xff, 0xff), null);
		
		model = scene.addMeshSceneNode(Engine.SYSTEM_MEDIA + "ext_cube.obj", 
				new Vector3d(0, 4, 20), false, null);
		model.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		/** ������еĲ���*/
		model.setTexture(null);
		
		/** ����0�����ʸ�Ϊext_tex1.png���ȵ�*/
		model.setTexture(Engine.SYSTEM_MEDIA + "ext_tex1.png", 0);
		model.setTexture(Engine.SYSTEM_MEDIA + "grass.png", 1);
		model.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp", 2);
		
		/** �õ��������ʵ�������ɫ��Ϊ��ɫ*/
		model.setDiffuseColor(new Color4i(0xff, 0xff, 0x00, 0xff), 3);
		
		cube1 = scene.addCubeSceneNode(new Vector3d(-4, -4, 20), 5, null);
		
		/** ��cube1����Ϊ��ֻ��һ�����ʣ����в���Ӧ��ƽ����ɫ*/
		cube1.setSmoothShade(true, 0);
		cube1.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		cube1.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		cube2 = scene.addCubeSceneNode(new Vector3d(4, -4, 20), 5, null);
		
		/** ��cube2����Ϊ��ֻ��һ�����ʣ����в���Ӧ�÷�ƽ����ɫ*/
		cube2.setSmoothShade(false, 0);
		cube2.setTexture(Engine.SYSTEM_MEDIA + "b&w.bmp");
		
		/** �ر���cube2����ͼ��˫�����˲�*/
		cube2.setMaterialFlag(MeshSceneNode.EMF_BILINEAR_FILTER, false);
		cube2.addRotationAnimator(new Vector3d(0.1, 0.5, 0.0));
		
		/** ����������ǿ�����������Щ���ע�⣬CubeSceneNode��Ϊû�й涨��������
		 * ����Ӧ�ù��պ��Ƚ����		
		 */
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
