package zte.irrlib;

import zte.irrlib.core.Vector3d;
import zte.irrlib.scene.CameraSceneNode;
import zte.irrlib.scene.SceneNode;


/**
 * <p>将一个{@link CameraSceneNode}包装为第一人称相机，这个相机的顶矢量(Up Vector)
 * 始终指向（0,1,0）。 
 * <p>在该类工作期间，需要维护相机节点的视角数据与该包装类视角数据的一致性。
 * {@link CameraSceneNode#setPosition(Vector3d, int)}和
 * {@link CameraSceneNode#setLookAt(Vector3d)}将会破坏这种一致性。
 * 因此，请尽量避免调用这两个方法。如必须调用，请使用{@link #uploadSettings()}或
 * {@link #downloadSettings()}来维护数据的一致性。
 * <p>请不要使用{@link CameraSceneNode#setUpVector(Vector3d)}，以
 * 保证在该类工作时，相机始终保持顶部朝上的状态。
 */
public class CameraFPSWrapper {
	
	/**
	 * <p>唯一的构造函数，将{@link CameraSceneNode}包装为第一人称相机。
	 * @param cam 被包装的相机
	 */
	public CameraFPSWrapper(CameraSceneNode cam){
		mCamera = cam;
		mCamera.setUpVector(new Vector3d(0,1,0));
		pLeft = new Vector3d();
		pLook = new Vector3d();
		downloadSettings();
	}
	
	/**
	 * 保持相机的朝向，向左移动x距离。
	 * @param x 移动的距离。如x<0，则向右移动
	 */
	public void goLeft(double x){
		mCamera.mark();
		mCamera.setPosition(pLeft.multi(x), SceneNode.TRANS_RELATIVE);
		uploadSettings();
	}
	
	/**
	 * 保持相机的朝向，向前移动x距离。
	 * @param x 移动的距离，如x<0，则向后移动
	 */
	public void goAhead(double x){
		mCamera.mark();
		mCamera.setPosition(pLook.multi(x), SceneNode.TRANS_RELATIVE);
		uploadSettings();
	}
	
	/**
	 * 保持相机的位置，向左转动x弧度。
	 * @param x 转动的角度（弧度制），如x<0，则向右转动
	 */
	public void turnLeft(double x){
		mXita -= x;
		recalculate();
		uploadSettings();
	}
	
	/**
	 * 保持相机的位置，向下转动x弧度。
	 * @param x 转动的角度（弧度制），如x<0，则向上转动
	 */
	public void turnUp(double x){
		mFai -= x;
		recalculate();
		uploadSettings();
	}
	
	/**
	 * 将视角数据上传到相机节点中。
	 */
	public void uploadSettings(){
		mCamera.setLookAt(mCamera.getPosition().plus(pLook));
	}
	
	/**
	 * 从相机节点下载视角数据。
	 */
	public void downloadSettings(){
		Vector3d d = mCamera.getLookAt().minus(mCamera.getPosition());
		if (d.isZero()){
			d.Z = 1.0;
		}
		d.normalize();
		
		mFai = Math.acos(d.Y);
		
		mXita = Math.atan(d.X/d.Z);
		if (d.X < 0){
			mXita += Math.PI;
		}
		
		recalculate();
	}
	
	private void recalculate(){
		if (mFai > Math.PI - 0.1){
			mFai = Math.PI - 0.1;
		}
		if (mFai < 0.1){
			mFai = 0.1;
		}
		
		pLook.X = Math.sin(mFai) * Math.sin(mXita);
		pLook.Z = Math.sin(mFai) * Math.cos(mXita);
		pLook.Y = Math.cos(mFai);
		
		pLeft = pLook.cross(new Vector3d(0,1,0));
		pLeft.normalize();
	}

	private CameraSceneNode mCamera;
	private double mFai/*0~PI*/, mXita;
	private Vector3d pLook, pLeft;
}
