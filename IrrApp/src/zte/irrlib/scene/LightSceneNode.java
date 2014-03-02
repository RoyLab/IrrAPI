 package zte.irrlib.scene;

import zte.irrapp.WLog;
import zte.irrlib.core.SLight;

public class LightSceneNode extends SceneNode {
	/**
	 * 唯一构造函数
	 */
	LightSceneNode(){
		super();
		mNodeType = TYPE_LIGHT;
		LightData = new SLight();
	}
	
	/**
	 * 更新光源参数值
	 */
	public void upLoadLightData(){
		nativeSendLightData(LightData, getId());
	}
	
	/**
	 * 获取光源参数值
	 */
	public void downloadLightData(){
		nativeGetLightData(LightData, getId());
	}
	
	/**
	 * 返回光源类型
	 * @return 光源类型:
	 *		点光源： 	POINT_LIGHT = 0x01;
	 *		 聚光灯：	SPOT_LIGHT = 0x02;
	 * 		平行光：	DIRECTIONAL_LIGHT = 0x03;
	 */
	public int getLightType(){
		return LightData.Type;
	}
	
	/**
	 * 光源参数
	 */
	public SLight LightData;
	
	private native int nativeSendLightData(SLight data, int id);
	private native int nativeGetLightData(SLight data, int id);
}
