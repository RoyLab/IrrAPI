package zte.irrlib.scene;

import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;

/**
 * ����ڵ���
 * @author Fxx
 *
 */
public class CameraSceneNode extends SceneNode{

	/**
	 * ������ü��档
	 * @param nearClip ���˲ü����zֵ
	 * @param farClip Զ�˲ü����zֵ
	 */
	public void setRendererClippingPlaneLimits(double nearClip, double farClip){
		nativeSetClipPlain(nearClip, farClip, getId());
	}
	
	/**
	 * ��������������ꡣ
	 * @param vec ������������
	 */
	public void setLookAt(Vector3d vec){
		mLookAt.copy(vec);
		nativeSetLookAt(vec.X, vec.Y, vec.Z, getId());
	}
	
	/**
	 * ���������up vector��
	 * @param vec �µ�up vectorֵ
	 */
	public void setUpVector(Vector3d vec){
		nativeSetUpVector(vec, getId());
	}
	
	/**
	 * �����ӿڵĿ�߱�
	 * @param ratio �ӿڿ�߱ȵ�ֵ
	 */
	public void setAspectRatio(double ratio){
		nativeSetAspectRatio(ratio, getId());
	}
	
	/**
	 * �Ӿ����ŽǵĻ���ֵ��Ĭ��ֵ��PI/2.5f��
	 * @param fovy �Ӿ����ŽǵĻ���ֵ
	 */
	public void setFovy(double fovy){
		nativeSetFovy(fovy, getId());
	}
	
	/**
	 * ��������Ƿ���λ�ñ仯��
	 * @return �����������λ�ñ仯�򷵻�true�����򷵻�false
	 */
	public boolean isPositionChanged(){
		return mIsPosChanged;
	}
	
	/**
	 * �������λ��״̬���������λ�ñ仯״ֵ̬Ϊfalse��
	 */
	public void resetPosChangedFlag(){
		mIsPosChanged = false;
	}
	
	@Override
	public void setPosition(Vector3d para, int mode){
		super.setPosition(para, mode);
		mIsPosChanged = true;
	}
	
	/**
	 * ��Java��洢�ͳ�ʼ�������λ�á�����͸��ڵ������
	 * @param pos ���λ������ֵ
	 * @param lookAt �����������ֵ
	 * @param parent ������ڵ����
	 */
	void javaLoadDataAndInit(Vector3d pos, Vector3d lookAt, SceneNode parent){
		super.javaLoadDataAndInit(pos, parent);
		mLookAt = new Vector3d(lookAt);
		Vector2i size = mScene.getRenderSize();
		setAspectRatio((float)size.X/size.Y);
	}
	
	/**
	 * ���������������ֵ
	 * @return �����������ֵ
	 */
	public Vector3d getLookAt(){
		return new Vector3d(mLookAt);
	}
	
	@Override
	public CameraSceneNode clone(){
		CameraSceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	public boolean remove(){
		if (super.remove()){
			if (mScene.getActiveCamera() == this){
				mScene.setActiveCamera(null);
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected CameraSceneNode softClone(){
		CameraSceneNode res = new CameraSceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected CameraSceneNode(CameraSceneNode node){
		super(node);
		mLookAt = new Vector3d(node.mLookAt);
		mIsPosChanged = node.mIsPosChanged;
	}
	
	CameraSceneNode(){
		super();
		mNodeType = TYPE_CAMERA;
	}

	private Vector3d mLookAt;
	private boolean mIsPosChanged;
	
	private native int nativeSetLookAt(double x, double y, double z, int id);
	private native int nativeSetClipPlain(double nearClip, double farClip, int id);
	private native int nativeSetAspectRatio(double ratio, int id);
	private native int nativeSetFovy(double fovy, int id);
	private native int nativeSetUpVector(Vector3d upVec, int id);
}
