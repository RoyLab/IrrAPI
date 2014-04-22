 package zte.irrlib.scene;

import zte.irrlib.core.SLight;
import zte.irrlib.core.Vector3d;

/**
 * �ƹ�ڵ��࣬Ĭ��Ϊ���Դ���͡�<br>
 * �� �ڵ��{@link #setRotation(Vector3d, int)}���ڸ�����ƹ����÷���
 * @author Fxx
 *
 */
public class LightSceneNode extends SceneNode {
	/**
	 * ���¹�Դ����ֵ���ڸı�ƹ����{@link #LightData}��ʹ��
	 */
	public void uploadLightData(){
		nativeSendLightData(LightData, getId());
	}
	
	/**
	 * ��ȡ��Դ����ֵ��ͨ������£��û�����Ҫ���ø÷���
	 */
	void downloadLightData(){
		nativeGetLightData(LightData, getId());
	}
	
	/**
	 * ���ع�Դ����
	 * @return ��Դ����:
	 *		���Դ�� 	POINT_LIGHT = 0x01;
	 *		 �۹�ƣ�	SPOT_LIGHT = 0x02;
	 * 		ƽ�й⣺	DIRECTIONAL_LIGHT = 0x03;
	 */
	public int getLightType(){
		return LightData.Type;
	}
	
	/**
	 * ��Դ����
	 */
	public SLight LightData;
	
	/**
	 * Ψһ���캯��
	 */
	LightSceneNode(){
		super();
		mNodeType = TYPE_LIGHT;
		LightData = new SLight();
	}

	void javaLoadDataAndInit(Vector3d pos, SceneNode parent, double radius){
		super.javaLoadDataAndInit(pos, parent);
		LightData.setRadius(radius);
		downloadLightData();
	}
	
	@Override
	public LightSceneNode clone(){
		LightSceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	protected LightSceneNode softClone(){
		LightSceneNode res = new LightSceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected LightSceneNode(LightSceneNode node){
		super(node);
	}
	
	private native int nativeSendLightData(SLight data, int id);
	private native int nativeGetLightData(SLight data, int id);
}
