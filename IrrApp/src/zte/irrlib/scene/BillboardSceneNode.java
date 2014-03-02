package zte.irrlib.scene;

import zte.irrlib.core.Color4i;

public class BillboardSceneNode extends MeshSceneNode{
	
	/**
	 * 唯一构造函数
	 */
	public BillboardSceneNode(){
		super();
		mNodeType = TYPE_BILLBOARD;
	}
	
	/**
	 * 设置公告板的颜色
	 * @param colorFront 公告板正面的颜色
	 * @param colorBack 公告板反面的颜色
	 */
	public void setColor(Color4i colorFront, Color4i colorBack){
		nativeSetColor(colorFront.r(), colorFront.g(), colorFront.b(), colorFront.a(),
				colorBack.r(), colorBack.g(), colorBack.b(), colorBack.a(), getId());
	}
	
	private native void nativeSetColor(int r1, int g1, int b1, int a1, int r2, int g2, int b2, int a2, int id);
}
