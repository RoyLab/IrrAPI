package zte.irrlib.scene;


public class AnimateMeshSceneNode extends MeshSceneNode {
	
	/**
	 * Ψһ�Ĺ��캯����
	 */
	AnimateMeshSceneNode(){
		super();
		mNodeType = TYPE_ANIMATE_MESH;
	}
	
	/**
	 * ���ض���ģ�ͳ�ʼ֡֡�š�
	 * @return ��ʼ֡֡��
	 */
	public int getStartFrame(){
		return nativeGetStartFrame(getId());
	}
	
	/**
	 * ���ض���ģ�ͽ���֡֡�š�
	 * @return ����֡֡��
	 */
	public int getEndFrame(){
		return nativeGetEndFrame(getId());
	}
	
	/**
	 * ���ض���ģ֡����
	 * @return ����ģ֡��
	 */
	public int getFrameNumber(){
		return nativeGetFrameNumber(getId());
	}
	
	/**
	 * ���ö���ģ�͵�ǰ֡�š�
	 * @param frame Ŀ��֡֡�š�֡�ű����Ƕ�Ӧ����ģ�͵���Ч֡�š�
	 */
	public void setCurrentFrame(int frame){
		nativeSetCurrentFrame(frame, getId());
	}
	
	/**
	 * ���ö���ģ�͵��˶��ٶȡ�
	 * @param fps �����ٶȲ�������λframe/second
	 */
	public void setAnimationSpeed(double fps){
		nativeSetAnimationSpeed(fps, getId());
	}
	
	/**
	 * ���ö����Ƿ�ѭ�����š�
	 * @param loop ֵΪtrue��ѭ�����ţ����򵥴β���
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