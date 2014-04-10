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
		
		Scene scene = engine.getScene();
		
		/** ���ú�ɫ������ǿ��ɫ͸��Ч�� */
		scene.setClearColor(new Color4i(0,0,0,255));
		
		/** ���û��淢�����ĸ�������ֵ*/
		em = new BoxEmitter();
		em.MaxEmitRate = 1000;
		em.MinEmitRate = 500;
		em.MinSize = new Vector2d(1,1);
		em.MaxSize = new Vector2d(2.0,2.0);
		em.MinLifeTime = 100;
		em.MaxLifeTime = 2000;
		em.InitialDirection = (new Vector3d(0,0.004,0.0));	//y������û��������ȼ�յ�����
		em.MaxAngleDegrees = 30;								//��������һ���ĳ���Ƕȸ�������Ȼ�������������
		em.BBox = new double[]{-0.5,-2,-2,0.5,-1.5,2};		//��Χ����x���ϵķ�Χ��СӦȡСһЩ��ʹ�����һ���㷢��
		
		flame = scene.addParticleSystemSceneNode(new Vector3d(30,-15,50), true, null);
		flame.setBoxEmitter(em);			//�����ӷ�����������ϵͳ�ڵ��
		
		/**Ϊ�������ò��ʲ����ݲ���ͼ�����ò������ͣ���͸������ɫ�ı�������*
		 * �����������Ϊ��alphaͨ����ͼƬ����.png��ʽ���������Ͷ�Ӧ��ΪEMT_TRANSPARENT_ALPHA_CHANNEL*/
		flame.setTexture(Engine.SYSTEM_MEDIA+"fire.bmp");
		flame.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
		
		/**Ϊ������ӽ���Ч����ȡ��ֱ����ʧ��������Ȼ��������ʧ����*/
		flame.addFadeOutAffectorParticleAffector(new Color4i(0,0,0,0), 400);
		
		/**�û��������и����͵Ĺ��̣�ʹ֮��Ϊ����*/
		flame.addScaleParticleAffector(new Vector2d(2,2));
		
		/**�û���������������ʼ���Ϸ���ĳ�����£��ʹ���治���ڳʷ�ɢ״*/
		flame.addAttractionParticleAffector(new Vector3d(30,-5,50), 3, true, true, true, true);
		
		/**���������β�˶�Ч�����������һ��ˮƽ�˶���������Ϊ����*/
		comet = scene.addSphereSceneNode(new Vector3d(0,0,30), 0.5, 16, null);
		comet.addFlyStraightAnimator(new Vector3d(40,5,30), new Vector3d(-40,5,30), 4000, true, false);
		
		/**�����β���ӽڵ㣬�����丸�ڵ��Ϊ����ӵ����ǽڵ㣬ʹ��β�����ǵ��˶����˶�*/
		cometTail = scene.addCometTailSceneNode(new Vector3d(1.0,0,0),1.0, 10.0, new Vector3d(0.01,0.0,0.0), comet);
		
		cometTail.setTexture(Engine.SYSTEM_MEDIA+"portal1.bmp");	//Ϊ�������ò���
		
		/**Ĭ�ϵĲ�������EMT_TRANSPARENT_ADD_COLOR��
		 * �����������Ϊ��alphaͨ����ͼƬ����.png��ʽ���������Ͷ�Ӧ��ΪEMT_TRANSPARENT_ALPHA_CHANNEL*/
		//cometTail.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
		/**��ӱ�ըЧ��������ϵͳ�ڵ�*/
		explosion = scene.addExplosionParticleSceneNode(new Vector3d(-30,-10,50), 1.0, 10.0, null);
		explosion.setTexture(Engine.SYSTEM_MEDIA+"fire.bmp");		//Ϊ�������ò���
		
		/**Ĭ�ϵĲ�������EMT_TRANSPARENT_ADD_COLOR��
		 * �����������Ϊ��alphaͨ����ͼƬ����.png��ʽ���������Ͷ�Ӧ��ΪEMT_TRANSPARENT_ALPHA_CHANNEL*/
		//explosion.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
		/**����ǹ�Ч��������ϵͳ�ڵ�*/
		stars = scene.addStarsParticleSceneNode(new Vector3d(0,0,50), 1000, null);
		stars.setTexture(Engine.SYSTEM_MEDIA+"portal1.bmp");		//Ϊ�������ò���
		
		/**Ĭ�ϵĲ�������EMT_TRANSPARENT_ADD_COLOR��
		 * �����������Ϊ��alphaͨ����ͼƬ����.png��ʽ���������Ͷ�Ӧ��ΪEMT_TRANSPARENT_ALPHA_CHANNEL*/
		//stars.setMaterialType(MeshSceneNode.E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
		
	}
	
	public void onResize(Engine engine, int width, int height) {
		
	}
	
}
