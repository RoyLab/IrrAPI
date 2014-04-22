 package zte.irrlib.scene;

import zte.irrlib.core.SLight;
import zte.irrlib.core.Vector3d;

/**
 * 灯光节点类，默认为点光源类型。<br>
 * 该 节点的{@link #setRotation(Vector3d, int)}用于给有向灯光设置方向。
 * @author Fxx
 *
 */
public class LightSceneNode extends SceneNode {
	/**
	 * 更新光源参数值，在改变灯光参数{@link #LightData}后使用
	 */
	public void uploadLightData(){
		nativeSendLightData(LightData, getId());
	}
	
	/**
	 * 获取光源参数值，通常情况下，用户不需要调用该方法
	 */
	void downloadLightData(){
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
	
	/**
	 * 唯一构造函数
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
