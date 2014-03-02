package zte.irrlib.scene;


public class AnimateMeshSceneNode extends MeshSceneNode {
	
	/**
	 * 唯一的构造函数。
	 */
	AnimateMeshSceneNode(){
		super();
		mNodeType = TYPE_ANIMATE_MESH;
	}
	
	/**
	 * 返回动画模型初始帧帧号。
	 * @return 初始帧帧号
	 */
	public int getStartFrame(){
		return nativeGetStartFrame(getId());
	}
	
	/**
	 * 返回动画模型结束帧帧号。
	 * @return 结束帧帧号
	 */
	public int getEndFrame(){
		return nativeGetEndFrame(getId());
	}
	
	/**
	 * 返回动画模帧数。
	 * @return 动画模帧数
	 */
	public int getFrameNumber(){
		return nativeGetFrameNumber(getId());
	}
	
	/**
	 * 设置动画模型当前帧号。
	 * @param frame 目标帧帧号。帧号必须是对应动画模型的有效帧号。
	 */
	public void setCurrentFrame(int frame){
		nativeSetCurrentFrame(frame, getId());
	}
	
	/**
	 * 设置动画模型的运动速度。
	 * @param fps 动画速度参数，单位frame/second
	 */
	public void setAnimationSpeed(double fps){
		nativeSetAnimationSpeed(fps, getId());
	}
	
	/**
	 * 设置动画是否循环播放。
	 * @param loop 值为true是循环播放，否则单次播放
	 */
	public void setLoopMode(boolean loop){
		nativeSetLoopMode(loop, getId());
	}
	
	private native int nativeGetStartFrame(int id);
	private native int nativeGetEndFrame(int id);
	private native int nativeGetFrameNumber(int id);
	private native int nativeSetCurrentFrame(int frame, int id);
	private native void nativeSetAnimationSpeed(double fps, int id);
	private native void nativeSetLoopMode(boolean loop, int id);
}