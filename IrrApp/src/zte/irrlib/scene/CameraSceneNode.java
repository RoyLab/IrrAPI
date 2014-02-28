package zte.irrlib.scene;

import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;

public class CameraSceneNode extends SceneNode{
	
	public void setRendererClippingPlaneLimits(double nearClip, double farClip){
		nativeSetClipPlain(nearClip, farClip, getId());
	}
	
	public void setLookAt(Vector3d vec){
		mLookAt.copy(vec);
		nativeSetLookAt(vec.x, vec.y, vec.z, getId());
	}
	
	public void setUpVector(Vector3d vec){
		nativeSetUpVector(vec, getId());
	}
	
	public void setAspectRatio(double ratio){
		nativeSetAspectRatio(ratio, getId());
	}
	
	public void setFovy(double fovy){
		nativeSetFovy(fovy, getId());
	}
	
	public boolean isPositionChanged(){
		return mIsPosChanged;
	}
	
	public void resetPosChangedFlag(){
		mIsPosChanged = false;
	}
	
	@Override
	public void setPosition(Vector3d para, int mode){
		super.setPosition(para, mode);
		mIsPosChanged = true;
	}
	
	public void javaLoadDataAndInit(Vector3d pos, Vector3d lookAt, SceneNode parent){
		super.javaLoadDataAndInit(pos, parent);
		mLookAt = new Vector3d(lookAt);
		Vector2i size = mScene.getRenderSize();
		setAspectRatio((float)size.x/size.y);
	}
	
	CameraSceneNode(){
		super();
		mNodeType = TYPE_CAMERA;
	}
	
	public Vector3d getLookAt(){
		return new Vector3d(mLookAt);
	}
	
	private Vector3d mLookAt;
	private boolean mIsPosChanged;
	
	private native int nativeSetLookAt(double x, double y, double z, int id);
	private native int nativeSetClipPlain(double nearClip, double farClip, int id);
	private native int nativeSetAspectRatio(double ratio, int id);
	private native int nativeSetFovy(double fovy, int id);
	private native int nativeSetUpVector(Vector3d upVec, int id);
}
