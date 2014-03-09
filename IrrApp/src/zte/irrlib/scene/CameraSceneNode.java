package zte.irrlib.scene;

import zte.irrlib.core.Vector2i;
import zte.irrlib.core.Vector3d;

/**
 * 相机节点类
 * @author Fxx
 *
 */
public class CameraSceneNode extends SceneNode{

	/**
	 * 设置相裁剪面。
	 * @param nearClip 近端裁剪面的z值
	 * @param farClip 远端裁剪面的z值
	 */
	public void setRendererClippingPlaneLimits(double nearClip, double farClip){
		nativeSetClipPlain(nearClip, farClip, getId());
	}
	
	/**
	 * 设置相机朝向坐标。
	 * @param vec 相机朝点的坐标
	 */
	public void setLookAt(Vector3d vec){
		mLookAt.copy(vec);
		nativeSetLookAt(vec.X, vec.Y, vec.Z, getId());
	}
	
	/**
	 * 设置相机的up vector。
	 * @param vec 新的up vector值
	 */
	public void setUpVector(Vector3d vec){
		nativeSetUpVector(vec, getId());
	}
	
	/**
	 * 设置视口的宽高比。（默认值：4.0f / 3.0f）
	 * @param ratio 视口宽高比的值
	 */
	public void setAspectRatio(double ratio){
		nativeSetAspectRatio(ratio, getId());
	}
	
	/**
	 * 设置视弧度值。（默认值：PI/2.5f）
	 * @param fovy 视域弧度值
	 */
	public void setFovy(double fovy){
		nativeSetFovy(fovy, getId());
	}
	
	/**
	 * 返回相机是否发生位置变化。
	 * @return 若相机发生过位置变化则返回true，否则返回false
	 */
	public boolean isPositionChanged(){
		return mIsPosChanged;
	}
	
	/**
	 * 重置相机位置状态，设置相机位置变化状态值为false。
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
	 * 在Java层存储和初始化相机的位置、朝向和父节点参数。
	 * @param pos 相机位置坐标值
	 * @param lookAt 相机朝向坐标值
	 * @param parent 相机父节点对象
	 */
	public void javaLoadDataAndInit(Vector3d pos, Vector3d lookAt, SceneNode parent){
		super.javaLoadDataAndInit(pos, parent);
		mLookAt = new Vector3d(lookAt);
		Vector2i size = mScene.getRenderSize();
		setAspectRatio((float)size.X/size.Y);
	}
	
	/**
	 * 返回相机朝向坐标值
	 * @return 相机朝向坐标值
	 */
	public Vector3d getLookAt(){
		return new Vector3d(mLookAt);
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
