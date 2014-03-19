package zte.irrlib.scene;

import zte.irrlib.core.Color4i;

/**
 * 公告板节点类
 * @author Fxx
 *
 */
public class BillboardSceneNode extends MeshSceneNode{
	
	/**
	 * 设置公告板的颜色。注意，在灯光开启的情况下，此设置无效，
	 * 应使用{@link MeshSceneNode#setDiffuseColor(Color4i, int)}替代
	 * @param colorFront 公告板正面的颜色
	 * @param colorBack 公告板反面的颜色
	 */
	public void setColor(Color4i colorFront, Color4i colorBack){
		nativeSetColor(colorFront.r(), colorFront.g(), colorFront.b(), colorFront.a(),
				colorBack.r(), colorBack.g(), colorBack.b(), colorBack.a(), getId());
	}
	
	@Override
	public BillboardSceneNode clone(){
		BillboardSceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	protected BillboardSceneNode softClone(){
		BillboardSceneNode res = new BillboardSceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected BillboardSceneNode(BillboardSceneNode node){
		super(node);
	}
	
	BillboardSceneNode(){
		super();
		mNodeType = TYPE_BILLBOARD;
	}
	
	private native void nativeSetColor(int r1, int g1, int b1, int a1, int r2, int g2, int b2, int a2, int id);
}
