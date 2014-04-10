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
		
		/** ���ú�ɫ������ǿ��ɫ͸��Ч�� */
		scene.setClearColor(new Color4i(0,0,0,255));
		
		/** ���÷������ĸ�������ֵ*/
		em = new BoxEmitter();
		em.MaxEmitRate = 100;
		em.MinEmitRate = 50;
		em.MinSize = new Vector2d(1.0,1.0);
		em.MaxSize = new Vector2d(1.0,1.0);
		em.MinLifeTime = 8000;
		em.MaxLifeTime = 10000;
		em.InitialDirection = (new Vector3d(0,0.0,0.0));
		em.BBox = new double[]{-2,-2,-1,2,2,1};
		
		/** ��ʼ������ϵͳ�ڵ� */
		circles = new ParticleSystemSceneNode[5];
		
		for(int i=0;i<5;++i){
			/**�������ϵͳ�ڵ�*/
			circles[i] = scene.addParticleSystemSceneNode(new Vector3d(0,10,0), true, null);
			
			/**������ϵͳ�ڵ�ͷ�������*/
			circles[i].setBoxEmitter(em);
			
			/**���ü�ɫ͸���Ĳ������ͽ����ʺ�ɫ������Ϊ͸��*/
			circles[i].setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
			
			/**���ò�����ͼ*/
			circles[i].setTexture(Engine.SYSTEM_MEDIA + "portal1.bmp");
		}
		
		/** ���г��廷ͼ�� ,����Ϊÿ������ϵͳ�ڵ������תӰ������
		 * ʹ�������˶��켣ΪΧ��Բ�ĵĻ�״�˶� */
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
