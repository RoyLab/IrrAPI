 package zte.irrlib.scene;

import zte.irrapp.WLog;
import zte.irrlib.core.SLight;

public class LightSceneNode extends SceneNode {
	/**
	 * Ψһ���캯��
	 */
	LightSceneNode(){
		super();
		mNodeType = TYPE_LIGHT;
		LightData = new SLight();
	}
	
	/**
	 * ���¹�Դ����ֵ
	 */
	public void upLoadLightData(){
		nativeSendLightData(LightData, getId());
	}
	
	/**
	 * ��ȡ��Դ����ֵ
	 */
	public void downloadLightData(){
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
	
	private native int nativeSendLightData(SLight data, int id);
	private native int nativeGetLightData(SLight data, int id);
}
