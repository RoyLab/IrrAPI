package zte.irrlib.scene;

import zte.irrlib.core.Color4i;

/**
 * �����ڵ���
 * @author Fxx
 *
 */
public class BillboardSceneNode extends MeshSceneNode{
	
	/**
	 * ���ù�������ɫ��ע�⣬�ڵƹ⿪��������£���������Ч��
	 * Ӧʹ��{@link MeshSceneNode#setDiffuseColor(Color4i, int)}���
	 * @param colorFront ������������ɫ
	 * @param colorBack ����巴�����ɫ
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
