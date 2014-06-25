package zte.irrlib.scene;

import zte.irrlib.core.Vector3d;

/**
 * 动态多边形节点类
 * @author Fxx
 *
 */
public class AnimateMeshSceneNode extends MeshSceneNode {
	
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
	
	public void setFrameLoop(int begin, int end){
		nativeSetFrameLoop(begin, end, getId());
	}
	
	@Override
	public AnimateMeshSceneNode clone(){
		AnimateMeshSceneNode res = softClone();
		cloneInNativeAndSetupNodesId(res);
		return res;
	}
	
	@Override
	protected AnimateMeshSceneNode softClone(){
		AnimateMeshSceneNode res = new AnimateMeshSceneNode(this);
		softCopyChildren(res);
		return res;
	}
	
	protected AnimateMeshSceneNode(AnimateMeshSceneNode node){
		super(node);
	}
	
	/**
	 * 唯一的构造函数。
	 */
	AnimateMeshSceneNode(Vector3d pos, SceneNode parent){
		super(pos, parent);
		mNodeType = TYPE_ANIMATE_MESH;
	}

	private native int nativeGetStartFrame(int Id);
	private native int nativeGetEndFrame(int Id);
	private native int nativeGetFrameNumber(int Id);
	private native int nativeSetCurrentFrame(int frame, int Id);
	private native int nativeSetAnimationSpeed(double fps, int Id);
	private native int nativeSetLoopMode(boolean loop, int Id);
	private native int nativeSetFrameLoop(int begin, int end, int Id);
}